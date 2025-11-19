package com.darwinruiz.shoplite.services;

import com.darwinruiz.shoplite.models.User;
import com.darwinruiz.shoplite.repositories.IUserRepository;
import com.darwinruiz.shoplite.repositories.UserRepository;

public class UserService {
    private static UserService instance;
    private final IUserRepository userRepository;

    private UserService() {
        this.userRepository = UserRepository.getInstance();
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean validateCredentials(String username, String password) {
        return userRepository.validateCredentials(username, password);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
}

