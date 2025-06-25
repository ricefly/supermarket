package com.mysupermarket.supermarket.backend.dto;

import java.util.List;
import java.math.BigDecimal;

public class OrderSettleRequest {
    private List<OrderItemDTO> items;
    private Integer cashierEmployeeId;
    private String memberPhone;
    private String paymentMethod;

    // Getter 和 Setter
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
    public Integer getCashierEmployeeId() { return cashierEmployeeId; }
    public void setCashierEmployeeId(Integer cashierEmployeeId) { this.cashierEmployeeId = cashierEmployeeId; }
    public String getMemberPhone() { return memberPhone; }
    public void setMemberPhone(String memberPhone) { this.memberPhone = memberPhone; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public static class OrderItemDTO {
        private Integer productId;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal itemTotal;
        // Getter 和 Setter
        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public BigDecimal getItemTotal() { return itemTotal; }
        public void setItemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; }
    }
} 