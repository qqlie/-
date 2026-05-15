package ua.fitness.exercise;

public class Run100mExercise extends Exercise implements Gradable {

    private static final double[][] MALE_STANDARDS = {
        {14.5, 15.0, 15.5, 16.0, 16.5, 17.0},
        {14.0, 14.5, 15.0, 15.5, 16.0, 16.5},
        {13.5, 14.0, 14.5, 15.0, 15.5, 16.0}
    };

    private static final double[][] FEMALE_STANDARDS = {
        {16.6, 17.6, 18.6, 19.6, 20.6, 21.6},
        {15.8, 16.8, 17.8, 18.8, 19.8, 20.8},
        {15.0, 16.0, 17.0, 18.0, 19.0, 20.0}
    };

    public Run100mExercise() {
        super("Біг 100 м", "секунд");
    }

    @Override
    public String evaluate(double result, int ageGroup, String gender) {
        return getGrade(result, ageGroup, gender);
    }

    @Override
    public String getGrade(double result, int ageGroup, String gender) {
        int idx = ageGroup - 1;
        double[][] std = "Ж".equalsIgnoreCase(gender) ? FEMALE_STANDARDS : MALE_STANDARDS;
        if (result <= std[2][idx]) return "Відмінно";
        if (result <= std[1][idx]) return "Добре";
        if (result <= std[0][idx]) return "Задовільно";
        return "Не зараховано";
    }

    @Override
    public String getStandardName() {
        return name;
    }
}
