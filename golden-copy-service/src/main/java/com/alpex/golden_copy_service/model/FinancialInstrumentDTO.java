package com.alpex.golden_copy_service.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialInstrumentDTO {

    private String isin;
    private String ticker;

    @JsonProperty("regularMarketPrice")
    private Double price;
    private String currency;
    private String primary_source;

    @JsonProperty("regularMarketOpen")
    private Double openPrice;

    @JsonProperty("regularMarketVolume")
    private Long volume;

    @JsonProperty("regularMarketChangePercent")
    private Double percentChange;

    public FinancialInstrumentDTO() {}

    public FinancialInstrumentDTO(String isin, String ticker, Double price, String currency,
                                  String primary_source, Double openPrice, Long volume, Double percentChange) {
        this.isin = isin;
        this.ticker = ticker;
        this.price = price;
        this.currency = currency;
        this.primary_source = primary_source;
        this.openPrice = openPrice;
        this.volume = volume;
        this.percentChange = percentChange;
    }

    public String getIsin() { return isin; }
    public void setIsin(String isin) { this.isin = isin; }
    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getPrimary_source() { return primary_source; }
    public void setPrimary_source(String primary_source) { this.primary_source = primary_source; }
    public Double getOpenPrice() { return openPrice; }
    public void setOpenPrice(Double openPrice) { this.openPrice = openPrice; }
    public Long getVolume() { return volume; }
    public void setVolume(Long volume) { this.volume = volume; }
    public Double getPercentChange() { return percentChange; }
    public void setPercentChange(Double percentChange) { this.percentChange = percentChange; }

}
