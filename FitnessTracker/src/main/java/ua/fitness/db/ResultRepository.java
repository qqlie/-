package ua.fitness.db;

import ua.fitness.exercise.Exercise;
import ua.fitness.model.ExerciseResult;
import ua.fitness.model.User;
import ua.fitness.service.Persistable;
import ua.fitness.service.FitnessService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResultRepository implements Persistable {

    private final DatabaseManager db;

    private final UserRepository userRepo;

    private FitnessService service;

    public ResultRepository(UserRepository userRepo) {
        this.db = DatabaseManager.getInstance();
        this.userRepo = userRepo;
    }

    public void setService(FitnessService service) {
        this.service = service;
    }

    public void saveResult(ExerciseResult result) {
        String sql = """
                INSERT INTO exercise_results
                    (user_id, exercise_key, exercise_name, result, grade, recorded_at)
                VALUES (?, ?, ?, ?, ?, ?)""";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, result.getUser().getId());
            ps.setString(2, getKeyForExercise(result.getExercise()));
            ps.setString(3, result.getExercise().getName());
            ps.setDouble(4, result.getResult());
            ps.setString(5, result.getGrade());
            ps.setString(6, result.getRecordedAt().toString());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) result.setId((int) rs.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println("Помилка збереження результату: " + e.getMessage());
        }
    }

    public List<ExerciseResult> findByUserId(int userId) {
        List<ExerciseResult> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise_results WHERE user_id = ? ORDER BY recorded_at DESC";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExerciseResult er = mapRow(rs);
                    if (er != null) list.add(er);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка завантаження результатів: " + e.getMessage());
        }
        return list;
    }

    public List<ExerciseResult> findAll() {
        List<ExerciseResult> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise_results ORDER BY recorded_at DESC";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ExerciseResult er = mapRow(rs);
                if (er != null) list.add(er);
            }
        } catch (SQLException e) {
            System.err.println("Помилка завантаження всіх результатів: " + e.getMessage());
        }
        return list;
    }

    private ExerciseResult mapRow(ResultSet rs) throws SQLException {
        int userId = (int) rs.getLong("user_id");
        String exerciseKey = rs.getString("exercise_key");
        double result = rs.getDouble("result");
        String grade = rs.getString("grade");
        LocalDateTime recordedAt = LocalDateTime.parse(rs.getString("recorded_at"));

        User user = userRepo.findById(userId).orElse(null);
        if (user == null || service == null) return null;

        Exercise exercise = service.getExercises().get(exerciseKey);
        if (exercise == null) return null;

        return new ExerciseResult((int) rs.getLong("id"), user, exercise, result, grade, recordedAt);
    }

    private String getKeyForExercise(Exercise exercise) {
        if (service == null) return "unknown";
        return service.getExercises().entrySet().stream()
                .filter(e -> e.getValue() == exercise)
                .map(java.util.Map.Entry::getKey)
                .findFirst()
                .orElse("unknown");
    }

    @Override
    public void save() {  }

    @Override
    public void load(int id) {  }
}
