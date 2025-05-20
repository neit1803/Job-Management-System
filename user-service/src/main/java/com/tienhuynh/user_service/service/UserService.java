package com.tienhuynh.user_service.service;

import com.tienhuynh.user_service.dto.UserDto;
import com.tienhuynh.user_service.model.User;

import java.util.UUID;

public interface UserService {
    Iterable<UserDto> getAllUsers();

    UserDto getUserById(UUID id);

    UserDto save(User user);

    UserDto update(UUID id, User user);

    User getUserByMail(String email);

    UserDto changePassword(UUID id, String password);
}
