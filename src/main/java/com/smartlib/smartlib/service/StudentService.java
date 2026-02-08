package com.smartlib.smartlib.service;

import com.smartlib.smartlib.model.Student;
import com.smartlib.smartlib.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public boolean register(String name, String email, String password) {
        if (studentRepository.findByEmail(email) != null) {
            return false;
        }

        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);

        studentRepository.save(student);
        return true;
    }

    public Student authenticate(String email, String password) {
        Student student = studentRepository.findByEmail(email);

        if (student != null && student.getPassword().equals(password)) {
            return student;
        }
        return null;
    }
}
