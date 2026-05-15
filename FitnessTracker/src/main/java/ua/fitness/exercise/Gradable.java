package ua.fitness.exercise;

public interface Gradable {

    String getGrade(double result, int ageGroup, String gender);

    String getStandardName();
}
