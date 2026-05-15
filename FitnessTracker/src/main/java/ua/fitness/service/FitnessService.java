package ua.fitness.service;

import ua.fitness.db.ResultRepository;
import ua.fitness.db.UserRepository;
import ua.fitness.exception.InvalidResultException;
import ua.fitness.exercise.*;
import ua.fitness.model.ExerciseResult;
import ua.fitness.model.User;

import java.util.*;
import java.util.stream.Collectors;

public class FitnessService {

    private final Map<String, Exercise> exercises;

    private final UserRepository userRepo;

    private final ResultRepository resultRepo;

    public FitnessService() {
        this.userRepo = new UserRepository();
        this.resultRepo = new ResultRepository(userRepo);
        this.resultRepo.setService(this);
        this.exercises = buildExerciseMap();
    }

    public User registerUser(String name, int age, String gender, double weight) {
        if (age < 16 || age > 100) {
            throw new InvalidResultException("Некоректний вік: " + age + ". Допустимо: 16–100.");
        }
        if (weight <= 0 || weight > 300) {
            throw new InvalidResultException("Некоректна вага: " + weight + ". Допустимо: 1–300 кг.");
        }
        if (!gender.equalsIgnoreCase("М") && !gender.equalsIgnoreCase("Ж")) {
            throw new InvalidResultException("Стать повинна бути 'М' або 'Ж'.");
        }
        User user = new User(name, age, gender, weight);
        userRepo.saveUser(user);
        return user;
    }

    public ExerciseResult recordResult(int userId, String exerciseKey, double value) {
        if (value < 0) {
            throw new InvalidResultException("Результат не може бути від'ємним: " + value);
        }
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new InvalidResultException("Користувача з id=" + userId + " не знайдено."));

        Exercise exercise = exercises.get(exerciseKey);
        if (exercise == null) {
            throw new InvalidResultException("Вправа '" + exerciseKey + "' не знайдена.");
        }
        validateExerciseForUser(user, exerciseKey);

        ExerciseResult er = new ExerciseResult(user, exercise, value);
        resultRepo.saveResult(er);
        return er;
    }

    public List<ExerciseResult> getUserResults(int userId) {
        return resultRepo.findByUserId(userId);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Map<String, Exercise> getExercises() {
        return Collections.unmodifiableMap(exercises);
    }

    public String getUserStatsSummary(int userId) {
        List<ExerciseResult> results = getUserResults(userId);
        if (results.isEmpty()) return "Результатів не знайдено.";

        Map<String, Long> gradeCount = results.stream()
                .collect(Collectors.groupingBy(ExerciseResult::getGrade, Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Всього результатів: %d%n", results.size()));
        gradeCount.forEach((grade, count) ->
                sb.append(String.format("  %-15s: %d%n", grade, count)));

        String best = results.stream()
                .map(ExerciseResult::getGrade)
                .filter(g -> g.equals("Відмінно") || g.equals("Добре") || g.equals("Задовільно"))
                .min(Comparator.comparingInt(FitnessService::gradeOrder))
                .orElse("Не зараховано");

        sb.append("Найкраща досягнута оцінка: ").append(best);
        return sb.toString();
    }

    private static int gradeOrder(String grade) {
        return switch (grade) {
            case "Відмінно"   -> 1;
            case "Добре"      -> 2;
            case "Задовільно" -> 3;
            default           -> 4;
        };
    }

    private void validateExerciseForUser(User user, String exerciseKey) {
        if ("kettlebell_u70".equals(exerciseKey) && user.getWeight() > 70) {
            throw new InvalidResultException(
                    "Жим гирі у категорії до 70 кг недоступний для ваги " + user.getWeight() + " кг.");
        }
        if ("kettlebell_o70".equals(exerciseKey) && user.getWeight() <= 70) {
            throw new InvalidResultException(
                    "Жим гирі у категорії понад 70 кг недоступний для ваги " + user.getWeight() + " кг.");
        }
    }

    private Map<String, Exercise> buildExerciseMap() {
        Map<String, Exercise> map = new LinkedHashMap<>();
        map.put("pullup",         new PullUpExercise());
        map.put("pushup",         new PushUpExercise());
        map.put("kettlebell_u70", new KettlebellExercise("до70"));
        map.put("kettlebell_o70", new KettlebellExercise("понад70"));
        map.put("shuttle",        new ShuttleRunExercise());
        map.put("run100",         new Run100mExercise());
        map.put("run1000",        new Run1000mExercise());
        map.put("run3000",        new Run3000mExercise());
        map.put("complex",        new ComplexStrengthExercise());
        return map;
    }
}
