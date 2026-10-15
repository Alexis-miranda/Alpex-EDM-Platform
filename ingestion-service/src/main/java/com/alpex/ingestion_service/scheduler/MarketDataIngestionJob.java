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

    /**
     * High-velocity performance testing engine to simulate live market data bursts.
     */
    public void executeStressTest(int totalRecords) {
        System.out.println("LOG: Initiating performance test burst of " + totalRecords + " messages...");

        String[] providers = {"Bloomberg", "Reuters"};
        String[] currencies = {"chf", "usd", "eur", "  usd  ", "CHF"};

        for (int i = 0; i < totalRecords; i++) {
            String ticker = "STRESS_" + i;
            String provider = providers[i % 2];
            String currency = currencies[i % 5];

            // Simulación de anomalías matemáticas (Precios negativos aleatorios)
            double price = (Math.random() * 400) - 50; // Precios entre -50 y 350

            // Provocar ISINs nulos a propósito cada 20 registros para alimentar la tabla de errores
          //  String isin = (i % 20 == 0) ? null : "US_STRESS_" + i;

            // S_ + 999 = 5 caracteres máximo. Totalmente inmune al límite de VARCHAR(12) de pgAdmin
            String isin = (i % 20 == 0) ? null : "S_" + i;

            FinancialInstrumentDTO record = new FinancialInstrumentDTO(isin, ticker, price, currency, provider);

            // Envío asíncrono inmediato a Kafka
            kafkaTemplate.send(TOPIC, record.getIsin(), record);
        }
        System.out.println("LOG: Performance burst successfully dispatched into Kafka cluster.");
    }


}
