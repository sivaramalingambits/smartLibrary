package com.smartlib.smartlib.controller;

import com.smartlib.smartlib.model.Book;
import com.smartlib.smartlib.service.BookService;
import com.smartlib.smartlib.repository.BookRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/librarian/books")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadBook(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String category,
            @RequestParam MultipartFile pdf) {

        try {
            bookService.uploadBook(title, author, category, pdf);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("status", "error"));
        }
}

}
