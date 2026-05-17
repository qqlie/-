package com.example.ui_practical.config;

import com.example.ui_practical.model.Student;
import com.example.ui_practical.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;

    public DataLoader(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() > 0) {
            return;
        }

        studentRepository.save(new Student("Іван", "Петренко", "ivan.petrenko@example.com", "КН-21", 19));
        studentRepository.save(new Student("Марія", "Коваленко", "maria.kovalenko@example.com", "КН-21", 18));
        studentRepository.save(new Student("Олег", "Шевченко", "oleh.shevchenko@example.com", "КН-22", 20));
    }
}
