package com.alpex.ingestion_service.scheduler;

import com.alpex.ingestion_service.dto.FinancialInstrumentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;


@Component
public class MarketDataIngestionJob {
    // KafkaTemplate triggers the producer logic to send messages to Docker

    private final KafkaTemplate<String, FinancialInstrumentDTO> kafkaTemplate;
    private final RestClient restClient;

    private static final String TOPIC = "data-ingested";



    @Value("${market.api.url}")
    private String marketDataUrl;

    public MarketDataIngestionJob(KafkaTemplate<String, FinancialInstrumentDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.restClient = RestClient.create();;
    }

    /**
     * AUTOMATED MORNING INGESTION ENGINE
     * Runs automatically Monday to Friday exactly at 08:00 AM (Europe/Zurich Timezone).
     * Fully autonomous, zero human intervention required.
     */
    @Scheduled(cron = "0 0 8 * * MON-FRI", zone = "Europe/Zurich")
    public void executeMorningIngestion() {
        System.out.println("LOG: 08:00 AM - Swiss Market Opens. Fetching Yahoo Finance Live Streams...");

        try {
            Map<String, Object> apiRawResponse = restClient.get()
                    .uri(marketDataUrl)
                    .retrieve()
                    .body(Map.class);

            if (apiRawResponse != null && apiRawResponse.containsKey("quoteResponse")) {
                Map<String, Object> quoteResponse = (Map<String, Object>) apiRawResponse.get("quoteResponse");
                List<Map<String, Object>> resultList = (List<Map<String, Object>>) quoteResponse.get("result");

                ObjectMapper mapper = new ObjectMapper();

                for (Map<String, Object> assetMap : resultList) {
                    String ticker = (String) assetMap.get("symbol");
                    String currency = (String) assetMap.get("currency");

                    FinancialInstrumentDTO record = mapper.convertValue(assetMap, FinancialInstrumentDTO.class);

                    // Data cleansing and metadata enrichment
                    record.setTicker(ticker.replace(".SW", ""));
                    record.setIsin("CH_" + record.getTicker() + "_GOLD");
                    record.setCurrency(currency != null ? currency : "CHF");
                    record.setPrimary_source("YahooFinance");

                    kafkaTemplate.send(TOPIC, record.getIsin(), record);
                }
                System.out.println("LOG: Success! Injected " + resultList.size() + " live Yahoo Finance entries into Kafka.");
            }
        } catch (Exception e) {
            System.out.println("LOG_CRITICAL_ERROR: Failure consuming market gateway: " + e.getMessage());
            triggerEmergencyFallback();
        }
    }

    private void triggerEmergencyFallback() {
        FinancialInstrumentDTO systemAlert = new FinancialInstrumentDTO(
                "SYS_ERR", "ERR_YAHOO_API", 0.0, "CHF", "SYSTEM_SHIELD", 0.0, 0L, 0.0
        );
        kafkaTemplate.send("data-exceptions", systemAlert.getIsin(), systemAlert);
    }



}
