package com.alpex.ingestion_service.controller;

import com.alpex.ingestion_service.scheduler.MarketDataIngestionJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingestion")
public class IngestionController {

    private final MarketDataIngestionJob ingestionJob;

    public IngestionController(MarketDataIngestionJob ingestionJob) {
        this.ingestionJob = ingestionJob;
    }



    /*
     * Manual trigger endpoint to simulate the 08:00 AM scheduled market stream
     */
    @GetMapping("/trigger")
    public ResponseEntity<String> triggerIngestion() {
        try {
            ingestionJob.executeMorningIngestion();
            return ResponseEntity.ok("INGESTION_TRIGGERED: Manual market integration stream completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("INGESTION_FAILED: " + e.getMessage());
        }
    }
}
