package com.alpex.golden_copy_service.kafka;

import com.alpex.golden_copy_service.model.DataExceptionEntity;
import com.alpex.golden_copy_service.model.FinancialInstrumentDTO;
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

    public GoldenCopyListener(GoldenInstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    /*
     * Consume verified events from Kafka and persist into golden_instruments catalog
     */
    @KafkaListener(topics = "data-ingested", groupId = "alpex-golden-copy-group")
    @Transactional
    public void consumeAndPersistGoldenCopy(FinancialInstrumentDTO dto) {
        System.out.println("LOG: Cleansed market event caught for ticker: " + dto.getTicker());

        try {
            GoldenInstrument instrument = new GoldenInstrument();

            // Map standard database master fields
            instrument.setIsin(dto.getIsin());
            instrument.setTicker(dto.getTicker());
            instrument.setPrice(dto.getPrice());
            instrument.setCurrency(dto.getCurrency());
            instrument.setPrimary_source(dto.getPrimary_source());

            // Map native numeric metrics from real-time Yahoo Finance payload
            instrument.setOpenPrice(dto.getOpenPrice());
            instrument.setVolume(dto.getVolume());
            instrument.setPercentChange(dto.getPercentChange());

            instrumentRepository.save(instrument);
            System.out.println("DATABASE_SUCCESS: Golden Copy committed to pgAdmin for ISIN: " + instrument.getIsin());

        } catch (Exception e) {
            System.out.println("LOG_ERROR: Database persistence write rejected: " + e.getMessage());
        }
    }

}
