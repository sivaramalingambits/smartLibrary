package com.smartlib.smartlib.controller;

import com.smartlib.smartlib.model.Book;
import com.smartlib.smartlib.repository.BookRepository;
import com.smartlib.smartlib.service.BookService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/student/books")
@CrossOrigin
public class StudentBookController {

    private final BookRepository bookRepository;
    private final BookService bookService;

    public StudentBookController(BookRepository bookRepository,
                                 BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    // ================= READ BOOK =================
    @GetMapping("/read/{bookId}")
    public ResponseEntity<Resource> readBook(@PathVariable Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        try {
            Path pdfPath = Paths.get(book.getPdfPath());
            Resource resource = new UrlResource(pdfPath.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + pdfPath.getFileName() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Unable to load PDF");
        }
    }

    // ================= RECOMMENDATIONS =================
    @GetMapping("/recommendations")
    public List<Book> getRecommendations(
            @RequestParam String category) {

        return bookService.getRecommendations(category);
    }
}
