package com.smartlib.smartlib.service;

import com.smartlib.smartlib.repository.LibrarianRepository;
import org.springframework.stereotype.Service;

@Service
public class LibrarianService {

    private final LibrarianRepository repository;

    public LibrarianService(LibrarianRepository repository) {
        this.repository = repository;
    }

    public boolean authenticate(String email, String password) {
        return repository.findByEmail(email)
                .map(l -> l.getPassword().equals(password))
                .orElse(false);
    }
}
