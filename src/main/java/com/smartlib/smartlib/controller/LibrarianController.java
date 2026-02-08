package com.smartlib.smartlib.controller;

import com.smartlib.smartlib.service.LibrarianService;
import com.smartlib.smartlib.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private final LibrarianService librarianService;
    private final StudentRepository studentRepository;

    public LibrarianController(LibrarianService librarianService,
                               StudentRepository studentRepository) {
        this.librarianService = librarianService;
        this.studentRepository = studentRepository;
    }

    /* ==========================
       Librarian Login
       ========================== */
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password) {

        boolean authenticated = librarianService.authenticate(email, password);

        if (authenticated) {
            return "redirect:/librarian-dashboard.html";
        } else {
            return "redirect:/librarian-login.html?error=true";
        }
    }

    /* ==========================
       Fetch Students (NO PASSWORD)
       ========================== */
    @GetMapping("/students")
    @ResponseBody
    public List<StudentResponse> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getName(),
                        student.getEmail()
                ))
                .collect(Collectors.toList());
    }

    /* ==========================
       DTO (Response Only)
       ========================== */
    static class StudentResponse {
        private Long id;
        private String name;
        private String email;

        public StudentResponse(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }
}
