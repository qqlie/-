package com.example.ui_practical.repository;

import com.example.ui_practical.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
