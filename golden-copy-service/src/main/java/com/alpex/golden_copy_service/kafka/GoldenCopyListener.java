package com.alpex.golden_copy_service.kafka;

import com.alpex.golden_copy_service.model.DataExceptionEntity;
import com.alpex.golden_copy_service.model.GoldenInstrument;
import com.alpex.golden_copy_service.repository.DataExceptionRepository;
import com.alpex.golden_copy_service.repository.GoldenInstrumentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/***
 * GoldenCopyListener
 * Listens for golden copy events and triggers the corresponding processing logic.
 */
@Component
public class GoldenCopyListener {


    private final GoldenInstrumentRepository instrumentRepository;
    private final DataExceptionRepository exceptionRepository;


    public GoldenCopyListener(GoldenInstrumentRepository instrumentRepository, DataExceptionRepository exceptionRepository) {
        this.instrumentRepository = instrumentRepository;
        this.exceptionRepository = exceptionRepository;
    }

    /**
     *  Save the clean, purified data.
     */
    @KafkaListener(topics = "data-validated", groupId = "alpex-golden-copy-group")
    @Transactional
    public void consumeAndPersistGoldenCopy(Map<String, Object> cleanData) {
        System.out.println("LOG: Clean event caught from Kafka for ticker: " + cleanData.get("ticker"));

        GoldenInstrument instrument = new GoldenInstrument();
        instrument.setIsin((String) cleanData.get("isin"));
        instrument.setTicker((String) cleanData.get("ticker"));
        instrument.setName((String) cleanData.get("name"));

        Double priceRaw = (Double) cleanData.get("price");
        instrument.setPrice(BigDecimal.valueOf(priceRaw));

        instrument.setCurrency((String) cleanData.get("currency"));
        instrument.setMasteredBy("alpex-golden-copy-service");
        instrument.setPrimarySource((String) cleanData.get("provider"));
        instrument.setUpdatedAt(LocalDateTime.now());

        instrumentRepository.save(instrument);
        System.out.println("DATABASE_SUCCESS: Golden Copy committed to pgAdmin for ISIN: " + instrument.getIsin());
    }

    /**
     *  Capture and store rejected exceptions for auditing in Tableau.
     */
    @KafkaListener(topics = "data-exceptions", groupId = "alpex-golden-copy-group")
    @Transactional
    public void consumeAndPersistExceptions(Map<String, Object> dirtyData) {
        System.out.println("LOG_WARNING: Dirty record caught from exceptions channel for ticker: " + dirtyData.get("ticker"));

        DataExceptionEntity exception = new DataExceptionEntity();
        exception.setIsin((String) dirtyData.get("isin"));
        exception.setTicker((String) dirtyData.get("ticker"));

        if (dirtyData.get("price") != null) {
            Double priceRaw = (Double) dirtyData.get("price");
            exception.setPrice(BigDecimal.valueOf(priceRaw));
        }

        exception.setCurrency((String) dirtyData.get("currency"));
        exception.setPrimarySource((String) dirtyData.get("provider"));

        // Simulación lógica de motivo de auditoría corporativa
        exception.setRejectionReason("CRITICAL_AUDIT: Structural constraint or negative metadata threshold anomaly.");
        exception.setRejectedAt(LocalDateTime.now());

        exceptionRepository.save(exception);
        System.out.println("DATABASE_AUDIT: Dirty exception successfully logged in pgAdmin for evaluation.");
    }
}
