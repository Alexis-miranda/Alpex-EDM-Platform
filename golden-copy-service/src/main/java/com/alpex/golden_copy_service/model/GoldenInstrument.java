package com.alpex.golden_copy_service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "golden_instruments")
public class GoldenInstrument {

    @Id
    @Column(name = "isin", length = 12)
    private String isin;

    @Column(name = "ticker", length = 10, nullable = false)
    private String ticker;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "price", nullable = false, precision = 15, scale = 4)
    private BigDecimal price;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    @Column(name = "mastered_by", length = 50, nullable = false)
    private String masteredBy;

    @Column(name = "primary_source", length = 50, nullable = false)
    private String primarySource;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Financial metrics added for Yahoo Finance real-time streams
    private Double openPrice;
    private Long volume;
    private Double percentChange;

    // Default Constructor
    public GoldenInstrument() {}

    public void setPercentChange(Double percentChange) {
        this.percentChange = percentChange;
    }







    // Getters and Setters

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
    }

    public Double getPercentChange() {
        return percentChange;
    }

    public String getIsin() { return isin; }
    public void setIsin(String isin) { this.isin = isin; }

    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMasteredBy() { return masteredBy; }
    public void setMasteredBy(String masteredBy) { this.masteredBy = masteredBy; }

    public String getPrimarySource() { return primarySource; }
    public void setPrimarySource(String primarySource) { this.primarySource = primarySource; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

}
