package com.tienhuynh.user_service.repository;

import com.tienhuynh.user_service.enums.RegisterStatus;
import com.tienhuynh.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    public User getUserById(UUID id);
    public User findByMail(String mail);

    void deleteAllByIsVerifiedAndCreatedAtBefore(RegisterStatus registerStatus, LocalDateTime localDateTime);
}
