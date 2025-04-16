package com.tienhuynh.user_service.service;

import com.tienhuynh.user_service.model.User;

import java.util.UUID;

public interface UserService {
    Iterable<User> getAllUsers();

    User getUser(UUID id);

    User save(User user);
}
