package com.books.store.service;

import com.books.store.entity.*;
import com.books.store.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public Order placeOrder(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            Book book = cartItem.getBook();
            Integer quantity = cartItem.getQuantity();

            if (book.getStock() < quantity) {
                throw new RuntimeException(
                        "Not enough stock for: " + book.getTitle());
            }

            // Reduce stock
            book.setStock(book.getStock() - quantity);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(book.getPrice());

            order.getItems().add(orderItem);

            BigDecimal itemTotal = book.getPrice()
                    .multiply(BigDecimal.valueOf(quantity));

            total = total.add(itemTotal);
        }

        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }
    public java.util.List<Order> getMyOrders(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserId(userId);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public Order updateStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);

        return orderRepository.save(order);
    }
}