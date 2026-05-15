package ua.fitness.exercise;

public class ComplexStrengthExercise extends Exercise implements Gradable {

    private static final int[][] STANDARDS = {
        {40, 35, 30, 25, 20, 15},
        {45, 40, 35, 30, 25, 20},
        {50, 45, 40, 35, 30, 25}
    };

    public ComplexStrengthExercise() {
        super("Комплексна силова вправа", "разів за 1 хв");
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
