package com.smartlib.smartlib.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "reservation",
    indexes = {
        @Index(name = "idx_student_status", columnList = "student_email, status")
    }
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= BOOK DETAILS =================
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @Column(name = "book_author", nullable = false)
    private String bookAuthor;

    @Column(name = "book_category", nullable = false)
    private String bookCategory;

    // ================= STUDENT DETAILS =================
    @Column(name = "student_email", nullable = false)
    private String studentEmail;

    // PENDING / APPROVED / CANCELLED
    @Column(name = "status", nullable = false)
    private String status;

    // ================= META =================
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // ================= CONSTRUCTOR =================
    public Reservation() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    }
    
}