package com.smartlib.smartlib.service;

import com.smartlib.smartlib.model.Book;
import com.smartlib.smartlib.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Value("${book.upload.dir}")
    private String uploadDir;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void uploadBook(String title, String author, String category, MultipartFile pdf) throws Exception {

        if (pdf.isEmpty() || !"application/pdf".equals(pdf.getContentType())) {
            throw new RuntimeException("Invalid PDF file");
        }

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + pdf.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.copy(pdf.getInputStream(), filePath);

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setPdfPath(filePath.toString());

        bookRepository.save(book);
    }

    public void deleteBook(Long id) throws Exception {
    Book book = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found"));

    // delete PDF file
    File file = new File(book.getPdfPath());
    if (file.exists()) {
        file.delete();
    }

    // delete DB record
    bookRepository.deleteById(id);
}

// ================= RECOMMENDATIONS =================
public List<Book> getRecommendations(String category) {
    return bookRepository.findRandomByCategory(category);
}

}
