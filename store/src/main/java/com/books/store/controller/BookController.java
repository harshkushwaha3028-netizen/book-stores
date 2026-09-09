package com.books.store.controller;

import com.books.store.entity.Book;
import com.books.store.exception.BookNotFoundException;
import com.books.store.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private BookService bookService;
    public BookController(BookService bookService){
        this.bookService=bookService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody @Valid Book book) {
        Book savedBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestBody Book book) {

        Book updatedBook = bookService.updateBook(id, book);

        return ResponseEntity.ok(updatedBook);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }
}
