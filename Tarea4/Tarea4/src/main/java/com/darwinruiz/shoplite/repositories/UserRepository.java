package com.darwinruiz.shoplite.repositories;

import com.darwinruiz.shoplite.database.DbConnection;
import com.darwinruiz.shoplite.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserRepository implements IUserRepository {
    private static UserRepository instance;
    private final DbConnection dbConnection;

    private UserRepository() {
        this.dbConnection = DbConnection.getInstance();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, role, active, created_at FROM users WHERE username = ? AND active = TRUE";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por username", e);
        }
        
        return null;
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        User user = findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public User getUserByUsername(String username) {
        return findByUsername(username);
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String role = rs.getString("role");
        Boolean active = rs.getBoolean("active");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        
        return new User(id, username, password, role, active, createdAt);
    }
}
