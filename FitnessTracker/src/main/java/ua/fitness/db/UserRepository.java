package ua.fitness.db;

import ua.fitness.model.User;
import ua.fitness.service.Persistable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Persistable {

    private final DatabaseManager db;

    public UserRepository() {
        this.db = DatabaseManager.getInstance();
    }

    public int saveUser(User user) {
        String sql = "INSERT INTO users (name, age, gender, weight, created_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setInt(2, user.getAge());
            ps.setString(3, user.getGender());
            ps.setDouble(4, user.getWeight());
            ps.setString(5, user.getCreatedAt().toString());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = (int) rs.getLong(1);
                    user.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка збереження користувача: " + e.getMessage());
        }
        return -1;
    }

    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка пошуку користувача: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Помилка отримання користувачів: " + e.getMessage());
        }
        return users;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                (int) rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("gender"),
                rs.getDouble("weight"),
                LocalDateTime.parse(rs.getString("created_at"))
        );
    }

    @Override
    public void save() {
        // делегується до saveUser(User)
    }

    @Override
    public void load(int id) {
        findById(id).ifPresent(u -> System.out.println("Завантажено: " + u));
    }
}
