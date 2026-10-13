package com.alpex.ingestion_service.dto;

import java.io.Serializable;

public class FinancialInstrumentDTO implements Serializable {

    private String isin;
    private String ticker;
    private Double price;
    private String currency;
    private String provider;

    public FinancialInstrumentDTO() {
    }

    public FinancialInstrumentDTO(String isin, String ticker, Double price, String currency, String provider) {
        this.isin = isin;
        this.ticker = ticker;
        this.price = price;
        this.currency = currency;
        this.provider = provider;
    }

    // Getters and Setters
    public String getIsin() { return isin; }
    public void setIsin(String isin) { this.isin = isin; }

    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
