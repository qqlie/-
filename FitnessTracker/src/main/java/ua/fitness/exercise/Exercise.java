package ua.fitness.exercise;

public abstract class Exercise {

    protected final String name;

    protected final String unit;

    public Exercise(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public abstract String evaluate(double result, int ageGroup, String gender);

    @Override
    public String toString() {
        return name + " (" + unit + ")";
    }
}
