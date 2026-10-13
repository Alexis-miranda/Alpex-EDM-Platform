package com.alpex.golden_copy_service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_exceptions")
public class DataExceptionEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exception_id")
    private Integer exceptionId;

    @Column(name = "isin", length = 12)
    private String isin;

    @Column(name = "ticker", length = 10)
    private String ticker;

    @Column(name = "price", precision = 15, scale = 4)
    private BigDecimal price;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "primary_source", length = 50)
    private String primarySource;

    @Column(name = "rejection_reason", length = 255, nullable = false)
    private String rejectionReason;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt = LocalDateTime.now();

    // Constructor por defecto
    public DataExceptionEntity() {}

    // Getters and Setters
    public Integer getExceptionId() { return exceptionId; }
    public void setExceptionId(Integer exceptionId) { this.exceptionId = exceptionId; }

    public String getIsin() { return isin; }
    public void setIsin(String isin) { this.isin = isin; }

    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPrimarySource() { return primarySource; }
    public void setPrimarySource(String primarySource) { this.primarySource = primarySource; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LocalDateTime getRejectedAt() { return rejectedAt; }
    public void setRejectedAt(LocalDateTime rejectedAt) { this.rejectedAt = rejectedAt; }

}
