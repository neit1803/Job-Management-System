package com.tienhuynh.user_service.repository;

import com.tienhuynh.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    public User getUserById(UUID id);
}
