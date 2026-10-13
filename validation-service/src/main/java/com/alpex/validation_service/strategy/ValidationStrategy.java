package com.alpex.validation_service.strategy;

import com.alpex.validation_service.dto.FinancialInstrumentDTO;

/**
 * Open/Closed Principle contract for data cleansing rules.
 */
public interface ValidationStrategy {
    void execute(FinancialInstrumentDTO data);
}
