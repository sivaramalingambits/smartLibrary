package com.smartlib.smartlib.controller;

import com.smartlib.smartlib.model.Book;
import com.smartlib.smartlib.model.Student;
import com.smartlib.smartlib.service.StudentService;
import com.smartlib.smartlib.service.ReservationService;
import com.smartlib.smartlib.repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final BookRepository bookRepository;

    public StudentController(StudentService studentService,
                             BookRepository bookRepository,
                             ReservationService reservationService) {
        this.studentService = studentService;
        this.bookRepository = bookRepository;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public Map<String, String> registerStudent(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password) {

        Map<String, String> response = new HashMap<>();

        boolean success = studentService.register(name, email, password);

        if (success) {
            response.put("status", "success");
            response.put("message", "Registration successful!");
        } else {
            response.put("status", "error");
            response.put("message", "Email already exists!");
        }

        return response;
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public Map<String, String> loginStudent(
            @RequestParam String email,
            @RequestParam String password) {

        Map<String, String> response = new HashMap<>();

        Student student = studentService.authenticate(email, password);

        if (student != null) {
            response.put("status", "success");
            response.put("email", student.getEmail());
        } else {
            response.put("status", "error");
            response.put("message", "Invalid email or password");
        }

        return response;
    }

    // ================= CATALOG =================
    @GetMapping("/books")
    public List<Book> getCatalog() {
        return bookRepository.findAll();
    }

}
