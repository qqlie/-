package ua.fitness.exercise;

public class ShuttleRunExercise extends Exercise implements Gradable {

    private static final double[][] STANDARDS = {
        {29.0, 30.0, 31.0, 32.0, 33.0, 34.0},
        {28.0, 29.0, 30.0, 31.0, 32.0, 33.0},
        {27.0, 28.0, 29.0, 30.0, 31.0, 32.0}
    };

    public ShuttleRunExercise() {
        super("Човниковий біг 10×10 м", "секунд");
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
        if (result <= STANDARDS[2][idx]) return "Відмінно";
        if (result <= STANDARDS[1][idx]) return "Добре";
        if (result <= STANDARDS[0][idx]) return "Задовільно";
        return "Не зараховано";
    }

    @Override
    public String getStandardName() {
        return name;
    }
}
