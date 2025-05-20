package com.tienhuynh.user_service.repository;

import com.tienhuynh.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    public User getUserById(UUID id);
    public User getUserByMail(String email);
}
