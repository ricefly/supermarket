package com.mysupermarket.supermarket.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Product_Loss_Records")
public class ProductLossRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lossRecordId;

    private Integer productId; // 外键，关联Products表

    private Integer quantityLost;

    @Column(length = 255)
    private String lossReason;

    private LocalDateTime lossDate;

    private Integer recordedByEmployeeId; // 外键，关联Employees表

    public ProductLossRecord() {}

    // Getter 和 Setter
    public Integer getLossRecordId() { return lossRecordId; }
    public void setLossRecordId(Integer lossRecordId) { this.lossRecordId = lossRecordId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public Integer getQuantityLost() { return quantityLost; }
    public void setQuantityLost(Integer quantityLost) { this.quantityLost = quantityLost; }
    public String getLossReason() { return lossReason; }
    public void setLossReason(String lossReason) { this.lossReason = lossReason; }
    public LocalDateTime getLossDate() { return lossDate; }
    public void setLossDate(LocalDateTime lossDate) { this.lossDate = lossDate; }
    public Integer getRecordedByEmployeeId() { return recordedByEmployeeId; }
    public void setRecordedByEmployeeId(Integer recordedByEmployeeId) { this.recordedByEmployeeId = recordedByEmployeeId; }
} 