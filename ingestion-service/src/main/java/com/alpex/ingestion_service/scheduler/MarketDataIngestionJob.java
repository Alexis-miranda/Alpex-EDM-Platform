package com.alpex.ingestion_service.scheduler;

import com.alpex.ingestion_service.dto.FinancialInstrumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
@Component
public class MarketDataIngestionJob {
    // KafkaTemplate triggers the producer logic to send messages to Docker

    private final KafkaTemplate<String, FinancialInstrumentDTO> kafkaTemplate;
    private final RestClient restClient;

    private static final String TOPIC = "data-ingested";
    // 🌐 URL Simulación de API Bursátil Real (Yahoo/TwelveData) con datos del SMI suizo


    @Value("${market.api.key}")
    private String apiKey;

    public MarketDataIngestionJob(KafkaTemplate<String, FinancialInstrumentDTO> kafkaTemplate, RestClient restClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.restClient = restClient;
    }

    /**
     * AUTOMATED MORNING INGESTION ENGINE
     * Runs automatically Monday to Friday exactly at 08:00 AM (Europe/Zurich Timezone).
     * Fully autonomous, zero human intervention required.
     */
    @Scheduled(cron = "0 0 8 * * MON-FRI", zone = "Europe/Zurich")
    public void executeMorningIngestion() {
        System.out.println("LOG: 08:00 AM - Swiss Market Opens. Activating autonomous EDM ingest pipeline...");

        // 🌐 CONSTRUCCIÓN DINÁMICA: Concatenamos la URL real usando tu API Key secreta
        String fullApiUrl = "https://mockaroo.com" + apiKey;

        try {
            // Conexión HTTP asíncrona: Llama a internet, descarga el JSON bursátil y lo convierte en DTOs
            FinancialInstrumentDTO[] response = restClient.get()
                    .uri(fullApiUrl)
                    .retrieve()
                    .body(FinancialInstrumentDTO[].class);

            if (response != null && response.length > 0) {
                List<FinancialInstrumentDTO> liveMarketData = Arrays.asList(response);

                // Bombardea el cluster de Kafka con la información fresca de la mañana
                for (FinancialInstrumentDTO record : liveMarketData) {
                    kafkaTemplate.send(TOPIC, record.getIsin(), record);
                }
                System.out.println("LOG: Success! Injected " + liveMarketData.size() + " live market entries into Kafka.");
            }

        } catch (ResourceAccessException e) {
            //   DEFENSIVO: Si el servidor se queda sin internet local o Cloud
            System.out.println("LOG_CRITICAL_ERROR: Cloud Network timeout. Internet connection unavailable.");
            triggerEmergencyFallback("NETWORK_DOWN");

        } catch (Exception e) {
            //  DEFENSIVO : Si el proveedor de la API de la bolsa se cae o el token expira
            System.out.println("LOG_CRITICAL_ERROR: Market Data Provider API failure. Reason: " + e.getMessage());
            triggerEmergencyFallback("PROVIDER_DOWNTIME");
        }
    }

    private void triggerEmergencyFallback(String diagnosticCode) {
        // Registra el fallo de infraestructura de forma asíncrona en el canal de errores
        FinancialInstrumentDTO systemAlert = new FinancialInstrumentDTO(
                "SYS_ERR", "ERR_API", 0.0, "CHF", "SYSTEM_SHIELD"
        );
        kafkaTemplate.send("data-exceptions", systemAlert.getIsin(), systemAlert);
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
