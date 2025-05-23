package com.tienhuynh.user_service.service;

import com.tienhuynh.user_service.dto.UserDto;
import com.tienhuynh.user_service.dto.UserDtoMapper;
import com.tienhuynh.user_service.enums.RegisterStatus;
import com.tienhuynh.user_service.exception.UserNotFoundException;
import com.tienhuynh.user_service.model.User;
import com.tienhuynh.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDtoMapper mapper;

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(UUID id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return mapper.apply(user);
    }

    @Override
    public User getUserByMail(String mail) {
        User user = userRepository.getUserByMail(mail);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public UserDto save(User user) {
        userRepository.save(user);
        return mapper.apply(user);
    }

    @Override
    public UserDto update(UUID id, User user) {
        return userRepository.
                findById(id)
                .map(mapper)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public UserDto changePassword(UUID id, String password) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPwdHash(password);
        userRepository.save(user);
        return mapper.apply(user);
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?") // chạy hàng ngày lúc 3h sáng
    public void cleanExpiredUsers() {
        userRepository.deleteAllByIsVerifiedAndCreatedAtBefore(RegisterStatus.EXPIRED, LocalDateTime.now().minusDays(3));
    }
}
