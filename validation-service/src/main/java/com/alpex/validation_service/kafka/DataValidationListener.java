package com.alpex.validation_service.kafka;

import com.alpex.validation_service.dto.FinancialInstrumentDTO;
import com.alpex.validation_service.strategy.ValidationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataValidationListener {

    private final KafkaTemplate<String, FinancialInstrumentDTO> kafkaTemplate;
    private final List<ValidationStrategy> validationStrategies;

    // Target topics for routing data based on the quality validation results
    private static final String VALIDATED_TOPIC = "data-validated";
    private static final String EXCEPTIONS_TOPIC = "data-exceptions";

    // Constructor Injection
    public DataValidationListener(KafkaTemplate<String, FinancialInstrumentDTO> kafkaTemplate,
                                  List<ValidationStrategy> validationStrategies) {
        this.kafkaTemplate = kafkaTemplate;
        this.validationStrategies = validationStrategies;
    }

    /**
     * Real-time event listener. Consumes raw financial records from Kafka,
     * applies business data cleansing rules, and routes the output accordingly.
     */
    @KafkaListener(topics = "data-ingested", groupId = "alpex-validation-group")
    public void processAndValidateMarketData(FinancialInstrumentDTO rawData) {
        System.out.println("LOG: Event received from topic 'data-ingested' for ticker: "
                + rawData.getTicker());

        try {
            // Loop through all data cleansing strategies sequentially
            for (ValidationStrategy strategy : validationStrategies) {
                strategy.execute(rawData);
            }

            // If no exception occurs, the record is successfully purified
            kafkaTemplate.send(VALIDATED_TOPIC, rawData.getIsin(), rawData);
            System.out.println("LOG: Record successfully validated and published to 'data-validated' for ISIN: " + rawData.getIsin());

        } catch (IllegalArgumentException e) {
            // Catches any business rule violation and safely reroutes the corrupted payload
            System.out.println("LOG_ERROR: " + e.getMessage() + " Routing record to exception pipeline.");
            kafkaTemplate.send(EXCEPTIONS_TOPIC, rawData.getIsin(), rawData);
        }
    }


}
