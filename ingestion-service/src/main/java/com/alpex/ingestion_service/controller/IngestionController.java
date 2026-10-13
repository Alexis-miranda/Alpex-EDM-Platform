package com.alpex.ingestion_service.controller;

import com.alpex.ingestion_service.scheduler.MarketDataIngestionJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingestion")
public class IngestionController {

    private final MarketDataIngestionJob ingestionJob;

    public IngestionController(MarketDataIngestionJob ingestionJob) {
        this.ingestionJob = ingestionJob;
    }

    /**
     * REST endpoint to manually trigger the morning ingestion pipeline.
     * Accessible locally at: http://localhost:8081/api/v1/ingestion/trigger
     */
    @GetMapping("/trigger")
    public String triggerIngestionManually() {
        System.out.println("LOG: Manual trigger received via REST API.");
        // Forcing the execution of the morning job
        ingestionJob.executeMorningIngestion();
        return "Alpex-EDM: Ingestion pipeline manually triggered successfully!";
    }
}
