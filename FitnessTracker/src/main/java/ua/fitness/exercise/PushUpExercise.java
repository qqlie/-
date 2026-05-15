package ua.fitness.exercise;

public class PushUpExercise extends Exercise implements Gradable {

    private static final int[][] MALE_STANDARDS = {
        {30, 25, 20, 15, 10, 5},
        {35, 30, 25, 20, 15, 10},
        {40, 35, 30, 25, 20, 15}
    };

    private static final int[][] FEMALE_STANDARDS = {
        {14, 11, 8, 5, 3, 2},
        {18, 15, 12, 9, 6, 4},
        {23, 20, 17, 14, 11, 7}
    };

    public PushUpExercise() {
        super("Згинання та розгинання рук з упору лежачи", "разів");
    }

    @Override
    public String evaluate(double result, int ageGroup, String gender) {
        return getGrade(result, ageGroup, gender);
    }

    @Override
    public String getGrade(double result, int ageGroup, String gender) {
        int idx = ageGroup - 1;
        int reps = (int) result;
        int[][] std = "Ж".equalsIgnoreCase(gender) ? FEMALE_STANDARDS : MALE_STANDARDS;
        if (reps >= std[2][idx]) return "Відмінно";
        if (reps >= std[1][idx]) return "Добре";
        if (reps >= std[0][idx]) return "Задовільно";
        return "Не зараховано";
    }

    @Override
    public String getStandardName() {
        return name;
    }
}
