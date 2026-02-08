package com.smartlib.smartlib.repository;

import com.smartlib.smartlib.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
        SELECT b FROM Book b
        WHERE LOWER(b.category) = LOWER(:category)
        ORDER BY function('RAND')
    """)
    List<Book> findRandomByCategory(@Param("category") String category);
}
