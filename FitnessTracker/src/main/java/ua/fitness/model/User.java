package ua.fitness.model;

import java.time.LocalDateTime;

public class User {

    private int id;
    private String name;
    private int age;
    private String gender;
    private double weight;
    private final LocalDateTime createdAt;

    public User(String name, int age, String gender, double weight) {
        this.name = name;
        this.age = age;
        this.gender = gender.toUpperCase();
        this.weight = weight;
        this.createdAt = LocalDateTime.now();
    }

    public User(int id, String name, int age, String gender, double weight, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender.toUpperCase();
        this.weight = weight;
        this.createdAt = createdAt;
    }

    public int getAgeGroup() {
        int[] limits = {30, 35, 40, 45, 50};
        return calculateGroup(age, limits, 0);
    }

    private int calculateGroup(int age, int[] limits, int index) {
        if (index >= limits.length || age < limits[index]) {
            return index + 1;
        }
        return calculateGroup(age, limits, index + 1);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public double getWeight() { return weight; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setWeight(double weight) { this.weight = weight; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Вік: %d (група. %d) | Стать: %s | Вага: %.1f кг | Зареєстровано: %s",
                id, name, age, getAgeGroup(), gender, weight,
                createdAt.toLocalDate());
    }
}
