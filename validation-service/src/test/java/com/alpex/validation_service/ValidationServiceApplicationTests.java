package com.alpex.validation_service;

import com.alpex.validation_service.dto.FinancialInstrumentDTO;
import com.alpex.validation_service.strategy.CurrencyValidationStrategy;
import com.alpex.validation_service.strategy.IsinValidationStrategy;
import com.alpex.validation_service.strategy.PriceValidationStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ValidationServiceApplicationTests {
	private final IsinValidationStrategy isinStrategy = new IsinValidationStrategy();
	private final PriceValidationStrategy priceStrategy = new PriceValidationStrategy();
	private final CurrencyValidationStrategy currencyStrategy = new CurrencyValidationStrategy();

	@Test
	void shouldConvertNegativePriceToAbsoluteValue() {
		FinancialInstrumentDTO mockInstrument = new FinancialInstrumentDTO("US5949181045", "MSFT", -350.00, "USD", "Reuters");
		priceStrategy.execute(mockInstrument);
		assertEquals(350.00, mockInstrument.getPrice());
	}

	@Test
	void shouldThrowExceptionWhenIsinIsEmptyOrNull() {
		FinancialInstrumentDTO corruptInstrument = new FinancialInstrumentDTO(null, "BAD_TICK", 100.0, "EUR", "Bloomberg");
		assertThrows(IllegalArgumentException.class, () -> isinStrategy.execute(corruptInstrument));
	}

	@Test
	void shouldConvertCurrencyToUppercaseAndTrimSpaces() {

		FinancialInstrumentDTO mockInstrument = new FinancialInstrumentDTO("US0378331005", "AAPL", 175.50, "  usd  ", "Bloomberg");

		currencyStrategy.execute(mockInstrument);

		assertEquals("USD", mockInstrument.getCurrency(), "La divisa debió ser limpiada y pasada a mayúsculas.");
	}

	@Test
	void shouldThrowExceptionWhenPriceIsZero() {
		FinancialInstrumentDTO zeroPriceInstrument = new FinancialInstrumentDTO("CH0012221716", "NESN", 0.0, "CHF", "Bloomberg");
		assertThrows(IllegalArgumentException.class, () -> priceStrategy.execute(zeroPriceInstrument));
	}
}
