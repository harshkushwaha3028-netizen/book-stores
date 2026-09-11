package com.books.store.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long orderId;
    private BigDecimal total;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderItemResponse> items;

    public OrderResponse(
            Long orderId,
            BigDecimal total,
            String status,
            LocalDateTime orderDate,
            List<OrderItemResponse> items) {

        this.orderId = orderId;
        this.total = total;
        this.status = status;
        this.orderDate = orderDate;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }
}
