package com.mysupermarket.supermarket.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    private Integer memberId; // 外键，关联Members表

    private Integer cashierEmployeeId; // 外键，关联Employees表

    @Column(length = 50)
    private String paymentMethod;

    @Column(length = 20)
    private String orderStatus;

    @Column(precision = 10, scale = 2)
    private BigDecimal rebateEarned;

    public Order() {}

    // Getter 和 Setter
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public Integer getCashierEmployeeId() { return cashierEmployeeId; }
    public void setCashierEmployeeId(Integer cashierEmployeeId) { this.cashierEmployeeId = cashierEmployeeId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public BigDecimal getRebateEarned() { return rebateEarned; }
    public void setRebateEarned(BigDecimal rebateEarned) { this.rebateEarned = rebateEarned; }
} 