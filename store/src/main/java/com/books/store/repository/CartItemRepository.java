package com.books.store.repository;

import com.books.store.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    Optional<CartItem> findByCartIdAndBookId(Long cartId, Long bookId);
    void deleteByCartIdAndBookId(Long cartId, Long bookId);
}
