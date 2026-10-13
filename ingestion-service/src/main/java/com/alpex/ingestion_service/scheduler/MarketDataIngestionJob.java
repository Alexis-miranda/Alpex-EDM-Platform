package com.alpex.ingestion_service.scheduler;

import com.alpex.ingestion_service.dto.FinancialInstrumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class MarketDataIngestionJob {
    // KafkaTemplate triggers the producer logic to send messages to Docker

    private final KafkaTemplate<String, FinancialInstrumentDTO> kafkaTemplate;

    private static final String TOPIC = "data-ingested";

    public MarketDataIngestionJob(KafkaTemplate<String, FinancialInstrumentDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Automated morning cron job. Runs Monday through Friday at 08:00 AM.
     * Cron representation: second, minute, hour, day, month, day-of-week
     */
    @Scheduled(cron = "0 0 8 * * MON-FRI")
    public void executeMorningIngestion() {
        System.out.println("LOG: Starting Alpex-EDM morning market data ingestion pipeline...");

        // Simulating incoming stream from real-world B2B feeds
        List<FinancialInstrumentDTO> rawMarketData = fetchDataFromProviders();

        for (FinancialInstrumentDTO record : rawMarketData) {
            // Using the ISIN property as the message key to maintain partition order
            kafkaTemplate.send(TOPIC, record.getIsin(), record);
            System.out.println("LOG: Successfully published raw data to Kafka for ISIN: " + record.getIsin());
        }
    }

    /**
     * Mock financial data source mimicking dirty provider feeds.
     */
    private List<FinancialInstrumentDTO> fetchDataFromProviders() {
        return List.of(
                new FinancialInstrumentDTO("CH0012221716", "NESN", 85.40, "CHF", "Bloomberg"),
                new FinancialInstrumentDTO("US5949181045", "MSFT", -350.00, "USD", "Reuters"),
                new FinancialInstrumentDTO("  US0378331005  ", "AAPL", 175.50, "usd", "Bloomberg"),

                //THE SUBVERSIVE: Fully corrupt asset without an ISIN identification document
                new FinancialInstrumentDTO(null, "BAD_TICK", 0.0, "EUR", "Bloomberg")
        );
    }
}
