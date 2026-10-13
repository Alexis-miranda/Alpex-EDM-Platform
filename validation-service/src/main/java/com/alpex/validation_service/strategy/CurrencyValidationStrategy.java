package com.alpex.validation_service.strategy;

import com.alpex.validation_service.dto.FinancialInstrumentDTO;
import org.springframework.stereotype.Component;

@Component
public class CurrencyValidationStrategy implements ValidationStrategy{

    @Override
    public void execute(FinancialInstrumentDTO data) {
        if (data.getCurrency() != null) {
            data.setCurrency(data.getCurrency().trim().toUpperCase());
        }
    }
}
