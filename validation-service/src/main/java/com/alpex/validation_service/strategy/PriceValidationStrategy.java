package com.alpex.validation_service.strategy;

import com.alpex.validation_service.dto.FinancialInstrumentDTO;
import org.springframework.stereotype.Component;

@Component
public class PriceValidationStrategy implements ValidationStrategy {
    @Override
    public void execute(FinancialInstrumentDTO data) {
        if (data.getPrice() == null || data.getPrice() <= 0) {
            if (data.getPrice() != null && data.getPrice() < 0) {
                System.out.println("WARN: Negative price detected for " + data.getIsin() + ". Applying absolute value calculation.");
                data.setPrice(Math.abs(data.getPrice()));
            } else {
                throw new IllegalArgumentException("REJECTED: Price field is zero or missing.");
            }
        }
    }
}
