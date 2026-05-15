package ua.fitness.model;

import ua.fitness.exercise.Exercise;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExerciseResult {

    private int id;
    private final User user;
    private final Exercise exercise;
    private final double result;
    private final String grade;
    private final LocalDateTime recordedAt;

    public ExerciseResult(User user, Exercise exercise, double result) {
        this.user = user;
        this.exercise = exercise;
        this.result = result;
        this.grade = exercise.evaluate(result, user.getAgeGroup(), user.getGender());
        this.recordedAt = LocalDateTime.now();
    }

    public ExerciseResult(int id, User user, Exercise exercise, double result,
                          String grade, LocalDateTime recordedAt) {
        this.id = id;
        this.user = user;
        this.exercise = exercise;
        this.result = result;
        this.grade = grade;
        this.recordedAt = recordedAt;
    }

    public int getId() { return id; }

    public User getUser() { return user; }

    public Exercise getExercise() { return exercise; }

    public double getResult() { return result; }

    public String getGrade() { return grade; }

    public LocalDateTime getRecordedAt() { return recordedAt; }

    public void setId(int id) { this.id = id; }

    public String getFormattedResult() {
        String unit = exercise.getUnit();
        if (unit.equals("хв.сек")) {
            int minutes = (int) result;
            int seconds = (int) Math.round((result - minutes) * 100);
            return String.format("%d хв %02d сек", minutes, seconds);
        }
        if (unit.equals("секунд")) {
            return String.format("%.2f сек", result);
        }
        return String.format("%.0f %s", result, unit);
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return String.format("%-50s | Результат: %-15s | Оцінка: %-15s | %s",
                exercise.getName(),
                getFormattedResult(),
                grade,
                recordedAt.format(fmt));
    }
}
