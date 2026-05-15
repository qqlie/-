package ua.fitness;

import ua.fitness.db.DatabaseManager;
import ua.fitness.exception.InvalidResultException;
import ua.fitness.model.ExerciseResult;
import ua.fitness.model.User;
import ua.fitness.service.FitnessService;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::closeInstance));

        try (Scanner scanner = new Scanner(System.in)) {
            FitnessService service = new FitnessService();

            System.out.println("╔══════════════════════════════════════════════════════╗");
            System.out.println("║    ТРЕКЕР ФІЗИЧНОЇ ПІДГОТОВКИ ТА НОРМАТИВІВ          ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");

            boolean running = true;
            while (running) {
                showMenu();
                System.out.print("Ваш вибір: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> addUser(scanner, service);
                    case "2" -> listUsers(service);
                    case "3" -> recordResult(scanner, service);
                    case "4" -> showResults(scanner, service);
                    case "5" -> showStats(scanner, service);
                    case "0" -> {
                        running = false;
                        System.out.println("\nЗавершення роботи...");
                    }
                    default -> System.out.println("Невідома команда. Спробуйте ще раз.\n");
                }
            }
        } finally {
            DatabaseManager.closeInstance();
        }
    }

    private static void showMenu() {
        System.out.println("""
                
                ┌─────────────────────────────────┐
                │  1. Додати користувача          │
                │  2. Список користувачів         │
                │  3. Внести результат вправи     │
                │  4. Результати користувача      │
                │  5. Статистика користувача      │
                │  0. Вихід                       │
                └─────────────────────────────────┘""");
    }

    private static void addUser(Scanner scanner, FitnessService service) {
        System.out.println("\n── Реєстрація користувача ──");
        try {
            System.out.print("ПІБ: ");
            String name = scanner.nextLine().trim();
            if (name.isBlank()) {
                System.out.println("Ім'я не може бути порожнім.");
                return;
            }

            System.out.print("Вік: ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Стать (М/Ж): ");
            String gender = scanner.nextLine().trim().toUpperCase();

            System.out.print("Вага (кг): ");
            double weight = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));

            User user = service.registerUser(name, age, gender, weight);
            System.out.printf("Зареєстровано: %s | Медико-вікова група: %d%n",
                    user.getName(), user.getAgeGroup());

        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть коректне число.");
        } catch (InvalidResultException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private static void listUsers(FitnessService service) {
        System.out.println("\n── Список користувачів ──");
        List<User> users = service.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Користувачів не знайдено.");
            return;
        }
        for (User u : users) {
            System.out.println(u);
        }
    }

    private static void recordResult(Scanner scanner, FitnessService service) {
        System.out.println("\n── Внесення результату ──");

        List<User> users = service.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Спочатку зареєструйте користувача (пункт 1).");
            return;
        }
        System.out.println("Оберіть користувача (введіть id):");
        users.forEach(u -> System.out.printf("  [%d] %s (група.%d, %s)%n",
                u.getId(), u.getName(), u.getAgeGroup(), u.getGender()));
        System.out.print("ID: ");

        int userId;
        try {
            userId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Некоректний id.");
            return;
        }

        System.out.println("\nОберіть вправу:");
        Map<String, String> exerciseMenu = new LinkedHashMap<>();
        exerciseMenu.put("pullup",         "1. Підтягування на перекладині (разів) [лише чол.]");
        exerciseMenu.put("pushup",         "2. Згинання/розгинання рук з упору лежачи (разів)");
        exerciseMenu.put("kettlebell_u70", "3. Жим гирі 24 кг, вагова кат. до 70 кг (разів) [лише чол.]");
        exerciseMenu.put("kettlebell_o70", "4. Жим гирі 24 кг, вагова кат. понад 70 кг (разів) [лише чол.]");
        exerciseMenu.put("shuttle",        "5. Човниковий біг 10×10 м (секунд) [лише чол.]");
        exerciseMenu.put("run100",         "6. Біг 100 м (секунд, напр. 13.5)");
        exerciseMenu.put("run1000",        "7. Біг 1000 м (хв.сек, напр. 3.40 = 3 хв 40 сек)");
        exerciseMenu.put("run3000",        "8. Біг 3000 м (хв.сек, напр. 12.30) [лише чол., гр. I–IV]");
        exerciseMenu.put("complex",        "9. Комплексна силова вправа (разів за хв) [лише чол.]");

        exerciseMenu.values().forEach(v -> System.out.println("  " + v));

        System.out.print("Номер: ");
        String numStr = scanner.nextLine().trim();

        String[] keys = {"pullup","pushup","kettlebell_u70","kettlebell_o70",
                          "shuttle","run100","run1000","run3000","complex"};
        int num;
        try {
            num = Integer.parseInt(numStr) - 1;
            if (num < 0 || num >= keys.length) {
                System.out.println("Некоректний вибір.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Введіть число від 1 до 9.");
            return;
        }

        String exerciseKey = keys[num];

        String hint = exerciseKey.startsWith("run1000") || exerciseKey.startsWith("run3000")
                ? "(формат хв.сек, напр. 4.05 = 4 хв 05 сек)"
                : exerciseKey.equals("run100") || exerciseKey.equals("shuttle")
                ? "(секунди, напр. 13.5)"
                : "(кількість разів, ціле число)";

        System.out.print("Результат " + hint + ": ");
        try {
            double value = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            ExerciseResult result = service.recordResult(userId, exerciseKey, value);
            System.out.printf("Збережено | Вправа: %s | Результат: %s | Оцінка: %s%n",
                    result.getExercise().getName(),
                    result.getFormattedResult(),
                    result.getGrade());
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть коректне число.");
        } catch (InvalidResultException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private static void showResults(Scanner scanner, FitnessService service) {
        System.out.println("\n── Результати користувача ──");
        System.out.print("Введіть ID користувача: ");
        try {
            int userId = Integer.parseInt(scanner.nextLine().trim());
            List<ExerciseResult> results = service.getUserResults(userId);

            if (results.isEmpty()) {
                System.out.println("Результатів не знайдено.");
                return;
            }

            System.out.println("─".repeat(100));

            results.forEach(r -> System.out.println(r));
            System.out.println("─".repeat(100));
            System.out.printf("Всього записів: %d%n", results.size());

        } catch (NumberFormatException e) {
            System.out.println("Некоректний id.");
        }
    }

    private static void showStats(Scanner scanner, FitnessService service) {
        System.out.println("\n── Статистика ──");
        System.out.print("Введіть ID користувача: ");
        try {
            int userId = Integer.parseInt(scanner.nextLine().trim());
            service.getAllUsers().stream()
                    .filter(u -> u.getId() == userId)
                    .findFirst()
                    .ifPresentOrElse(
                            u -> {
                                System.out.println(u);
                                System.out.println(service.getUserStatsSummary(userId));
                            },
                            () -> System.out.println("Користувача не знайдено.")
                    );
        } catch (NumberFormatException e) {
            System.out.println("Некоректний id.");
        }
    }
}
