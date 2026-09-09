package com.books.store.service;

import com.books.store.entity.Book;
import com.books.store.exception.BookNotFoundException;
import com.books.store.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private BookRepository bookRepository;
    public BookService(BookRepository bookRepository){
        this.bookRepository=bookRepository;
    }
    public Book createBook(Book book){
        return bookRepository.save(book);

    }
    public List<Book> getAllBook(){
        return bookRepository.findAll();
    }
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found with id: " + id));
    }

    public Book updateBook(Long id,Book book){
        Optional<Book> updated=bookRepository.findById(id);
        if (updated.isEmpty()) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        Book newUpdate=updated.get();
        newUpdate.setTitle(book.getTitle());
        newUpdate.setAuthor(book.getAuthor());
        newUpdate.setPrice(book.getPrice());
        newUpdate.setDescription(book.getDescription());
        newUpdate.setStock(book.getStock());
        newUpdate.setCategory(book.getCategory());
        return bookRepository.save(book);
    }
    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }
}
