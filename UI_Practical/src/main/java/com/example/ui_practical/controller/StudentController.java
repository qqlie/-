package com.example.ui_practical.controller;

import com.example.ui_practical.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping({"/", "/students"})
    public String students(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "students";
    }
}
