package com.books.store.service;

import com.books.store.entity.Book;
import com.books.store.entity.Cart;
import com.books.store.entity.CartItem;
import com.books.store.entity.User;
import com.books.store.repository.BookRepository;
import com.books.store.repository.CartItemRepository;
import com.books.store.repository.CartRepository;
import com.books.store.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            BookRepository bookRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Cart addToCart(Long userId, Long bookId, Integer quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (book.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository
                .findByCartIdAndBookId(cart.getId(), bookId)
                .orElse(null);

        if (cartItem != null) {
            int newQuantity = cartItem.getQuantity() + quantity;

            if (newQuantity > book.getStock()) {
                throw new RuntimeException("Not enough stock");
            }

            cartItem.setQuantity(newQuantity);

        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(quantity);

            cart.getItems().add(cartItem);   // IMPORTANT
        }

        cartItemRepository.save(cartItem);

        return cart;
    }
    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
    public void removeFromCart(Long userId, Long bookId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cartItemRepository
                .findByCartIdAndBookId(cart.getId(), bookId)
                .orElseThrow(() -> new RuntimeException("Book not found in cart"));

        cartItemRepository.delete(item);
    }
    public void updateQuantity(Long userId, Long bookId, Integer quantity) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cartItemRepository
                .findByCartIdAndBookId(cart.getId(), bookId)
                .orElseThrow(() -> new RuntimeException("Book not found in cart"));

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (quantity > item.getBook().getStock()) {
            throw new RuntimeException("Not enough stock");
        }

        item.setQuantity(quantity);

        cartItemRepository.save(item);
    }
}