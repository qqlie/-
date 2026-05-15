package ua.fitness.exercise;

public class PullUpExercise extends Exercise implements Gradable {

    private static final int[][] STANDARDS = {
        {8, 6, 5, 4, 3, 2},
        {10, 8, 6, 5, 4, 3},
        {12, 10, 8, 6, 5, 4}
    };

    public PullUpExercise() {
        super("Підтягування на перекладині", "разів");
    }

    @Override
    public String evaluate(double result, int ageGroup, String gender) {
        if ("Ж".equalsIgnoreCase(gender)) {
            return "Недоступно для жінок";
        }
        return getGrade(result, ageGroup, gender);
    }

    @Override
    public String getGrade(double result, int ageGroup, String gender) {
        int idx = ageGroup - 1;
        int reps = (int) result;
        if (reps >= STANDARDS[2][idx]) return "Відмінно";
        if (reps >= STANDARDS[1][idx]) return "Добре";
        if (reps >= STANDARDS[0][idx]) return "Задовільно";
        return "Не зараховано";
    }

    @Override
    public String getStandardName() {
        return name;
    }
}
