package ua.fitness.exercise;

public class KettlebellExercise extends Exercise implements Gradable {

    private static final int[][] STANDARDS_UNDER_70 = {
        {6, 5, 4, 3, 2, 1},
        {8, 7, 6, 5, 4, 3},
        {10, 9, 8, 7, 6, 5}
    };

    private static final int[][] STANDARDS_OVER_70 = {
        {8, 7, 6, 5, 4, 3},
        {10, 9, 8, 7, 6, 5},
        {12, 11, 10, 9, 8, 7}
    };

    private final String weightCategory;

    public KettlebellExercise(String weightCategory) {
        super(
            "Жим гирі 24 кг однією рукою (" + weightCategory + " кг)",
            "разів"
        );
        this.weightCategory = weightCategory;
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
        int[][] std = weightCategory.contains("до70") ? STANDARDS_UNDER_70 : STANDARDS_OVER_70;
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
