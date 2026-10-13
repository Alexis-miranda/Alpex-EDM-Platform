package com.alpex.ingestion_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Required to unlock Spring's automated scheduling features
public class IngestionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IngestionServiceApplication.class, args);
	}

}
