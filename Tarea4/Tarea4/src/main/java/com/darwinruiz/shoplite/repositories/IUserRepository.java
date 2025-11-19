package com.darwinruiz.shoplite.repositories;

import com.darwinruiz.shoplite.models.User;

public interface IUserRepository {
    User findByUsername(String username);
    boolean validateCredentials(String username, String password);
    User getUserByUsername(String username);
}

