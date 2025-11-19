package com.darwinruiz.shoplite.repositories;

import com.darwinruiz.shoplite.models.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static UserRepository instance;
    private List<User> users;

    private UserRepository() {
        users = new ArrayList<>();
        // Usuarios de prueba
        users.add(new User("admin@shoplite.com", "admin123", "ADMIN"));
        users.add(new User("user@shoplite.com", "user123", "USER"));
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public User findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public boolean validateCredentials(String email, String password) {
        User user = findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    public User getUserByEmail(String email) {
        return findByEmail(email);
    }
}

