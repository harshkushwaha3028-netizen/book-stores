package com.books.store.controller;

import com.books.store.dto.CartItemResponse;
import com.books.store.dto.CartResponse;
import com.books.store.entity.Cart;
import com.books.store.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Add book to cart
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam Integer quantity) {

        Cart cart = cartService.addToCart(userId, bookId, quantity);

        return ResponseEntity.ok(convertToResponse(cart));
    }

    // Get cart
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestParam Long userId) {

        Cart cart = cartService.getCart(userId);

        return ResponseEntity.ok(convertToResponse(cart));
    }

    // Remove book from cart
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(
            @RequestParam Long userId,
            @RequestParam Long bookId) {

        cartService.removeFromCart(userId, bookId);

        return ResponseEntity.noContent().build();
    }

    // Update quantity
    @PutMapping("/update")
    public ResponseEntity<Void> updateQuantity(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam Integer quantity) {

        cartService.updateQuantity(userId, bookId, quantity);

        return ResponseEntity.noContent().build();
    }

    // Convert Cart entity to DTO
    private CartResponse convertToResponse(Cart cart) {

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(item -> new CartItemResponse(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getBook().getPrice(),
                        item.getQuantity()
                ))
                .toList();

        BigDecimal total = cart.getItems()
                .stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(
                cart.getId(),
                items,
                total
        );
    }
}