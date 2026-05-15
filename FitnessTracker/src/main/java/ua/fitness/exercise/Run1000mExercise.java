package ua.fitness.exercise;

public class Run1000mExercise extends Exercise implements Gradable {

    private static final int[][] MALE_STANDARDS = {
        {245, 265, 285, 305, 340, 360},
        {235, 255, 275, 295, 320, 340},
        {220, 240, 260, 280, 300, 320}
    };

    private static final int[][] FEMALE_STANDARDS = {
        {290, 320, 350, 380, 410, 440},
        {270, 300, 330, 360, 390, 420},
        {250, 280, 310, 340, 370, 400}
    };

    public Run1000mExercise() {
        super("Біг 1000 м", "хв.сек");
    }

    private int toSeconds(double minSec) {
        int minutes = (int) minSec;
        int seconds = (int) Math.round((minSec - minutes) * 100);
        return minutes * 60 + seconds;
    }

    @Override
    public String evaluate(double result, int ageGroup, String gender) {
        return getGrade(result, ageGroup, gender);
    }

    @Override
    public String getGrade(double result, int ageGroup, String gender) {
        int idx = ageGroup - 1;
        int totalSec = toSeconds(result);
        int[][] std = "Ж".equalsIgnoreCase(gender) ? FEMALE_STANDARDS : MALE_STANDARDS;
        if (totalSec <= std[2][idx]) return "Відмінно";
        if (totalSec <= std[1][idx]) return "Добре";
        if (totalSec <= std[0][idx]) return "Задовільно";
        return "Не зараховано";
    }

    @Override
    public String getStandardName() {
        return name;
    }
}
