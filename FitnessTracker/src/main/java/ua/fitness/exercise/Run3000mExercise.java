package ua.fitness.exercise;

public class Run3000mExercise extends Exercise implements Gradable {

    private static final int[][] STANDARDS = {
        {780, 810, 840, 870},
        {750, 780, 810, 840},
        {720, 750, 780, 810}
    };

    public Run3000mExercise() {
        super("Біг 3000 м", "хв.сек");
    }

    private int toSeconds(double minSec) {
        int minutes = (int) minSec;
        int seconds = (int) Math.round((minSec - minutes) * 100);
        return minutes * 60 + seconds;
    }

    @Override
    public String evaluate(double result, int ageGroup, String gender) {
        if ("Ж".equalsIgnoreCase(gender)) {
            return "Недоступно для жінок";
        }
        if (ageGroup > 4) {
            return "Не передбачено для групи " + ageGroup;
        }
        return getGrade(result, ageGroup, gender);
    }

    @Override
    public String getGrade(double result, int ageGroup, String gender) {
        if (ageGroup > 4) return "Не передбачено для цієї групи";
        int idx = ageGroup - 1;
        int totalSec = toSeconds(result);
        if (totalSec <= STANDARDS[2][idx]) return "Відмінно";
        if (totalSec <= STANDARDS[1][idx]) return "Добре";
        if (totalSec <= STANDARDS[0][idx]) return "Задовільно";
        return "Не зараховано";
    }

    @Override
    public String getStandardName() {
        return name;
    }
}
