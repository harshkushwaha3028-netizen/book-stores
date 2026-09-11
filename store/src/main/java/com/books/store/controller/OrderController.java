package com.books.store.controller;

import com.books.store.dto.OrderItemResponse;
import com.books.store.dto.OrderResponse;
import com.books.store.entity.Order;
import com.books.store.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestParam Long userId) {

        Order order = orderService.placeOrder(userId);

        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .toList();

        OrderResponse response = new OrderResponse(
                order.getId(),
                order.getTotal(),
                order.getStatus(),
                order.getOrderDate(),
                items
        );

        return ResponseEntity.ok(response);
    }
}