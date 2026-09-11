package com.books.store.controller;

import com.books.store.dto.OrderItemResponse;
import com.books.store.dto.OrderResponse;
import com.books.store.entity.Order;
import com.books.store.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @RequestParam Long userId) {

        List<Order> orders = orderService.getMyOrders(userId);

        List<OrderResponse> responses = orders.stream()
                .map(order -> {

                    List<OrderItemResponse> items = order.getItems()
                            .stream()
                            .map(item -> new OrderItemResponse(
                                    item.getBook().getId(),
                                    item.getBook().getTitle(),
                                    item.getPrice(),
                                    item.getQuantity()
                            ))
                            .toList();

                    return new OrderResponse(
                            order.getId(),
                            order.getTotal(),
                            order.getStatus(),
                            order.getOrderDate(),
                            items
                    );
                })
                .toList();

        return ResponseEntity.ok(responses);
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<Order> orders = orderService.getAllOrders();

        List<OrderResponse> responses = orders.stream()
                .map(this::convertToResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/admin/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {

        Order order = orderService.updateStatus(orderId, status);

        return ResponseEntity.ok(convertToResponse(order));
    }
    private OrderResponse convertToResponse(Order order) {

        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotal(),
                order.getStatus(),
                order.getOrderDate(),
                items
        );
    }
}