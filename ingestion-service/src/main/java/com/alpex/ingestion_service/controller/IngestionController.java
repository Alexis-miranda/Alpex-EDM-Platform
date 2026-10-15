package com.alpex.ingestion_service.controller;

import com.alpex.ingestion_service.scheduler.MarketDataIngestionJob;
import org.springframework.beans.factory.annotation.Autowired;
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



    /**
     * REST endpoint with dynamic stress parameter.
     * Examples:
     * http://localhost:8081/api/v1/ingestion/trigger?amount=100
     */
    @GetMapping("/trigger")
    public String triggerIngestionManually(@RequestParam(value = "amount", required = false) Integer amount) {
        if (amount != null && amount > 0) {
            System.out.println("LOG: Dynamic performance test trigger received for " + amount + " records.");
            ingestionJob.executeStressTest(amount);
            return "Alpex-EDM: Asynchronous performance test pipeline triggered for " + amount + " records successfully!";
        }

        System.out.println("LOG: Standard trigger received via REST API.");
        ingestionJob.executeMorningIngestion();
        return "Alpex-EDM: Standard ingestion pipeline triggered successfully!";
    }
}
