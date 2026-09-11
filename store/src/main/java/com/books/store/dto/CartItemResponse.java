package com.books.store.dto;

import java.math.BigDecimal;

public class CartItemResponse {

    private Long bookId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public CartItemResponse(
            Long bookId,
            String title,
            BigDecimal price,
            Integer quantity) {

        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}