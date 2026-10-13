package com.alpex.validation_service.strategy;

import com.alpex.validation_service.dto.FinancialInstrumentDTO;
import org.springframework.stereotype.Component;

@Component
public class IsinValidationStrategy  implements ValidationStrategy{
    @Override
    public void execute(FinancialInstrumentDTO data) {
        // Validation check for empty or missing identifiers
        if (data.getIsin() == null || data.getIsin().trim().isEmpty()) {
            throw new IllegalArgumentException("CRITICAL: Rejected due to missing or empty ISIN identifier.");
        }
        // Clean
        data.setIsin(data.getIsin().trim().toUpperCase());
    }
}
