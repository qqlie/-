package com.example.ui_practical.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String groupName;
    private Integer age;

    public Student() {
    }

    public Student(String firstName, String lastName, String email, String groupName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.groupName = groupName;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGroupName() {
        return groupName;
    }

    public Integer getAge() {
        return age;
    }

}
