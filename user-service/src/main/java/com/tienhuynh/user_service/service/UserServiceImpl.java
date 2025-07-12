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
        User user = userRepository.findByMail(mail);
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
    public UserDto update(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Chỉ cập nhật các field cho phép
        if (user.getFullName() != null) {
            existingUser.setFullName(user.getFullName());
        }

        if (user.getPwdHash() != null) {
            existingUser.setPwdHash(user.getPwdHash());
        }

        if (user.getAddress() != null) {
            existingUser.setAddress(user.getAddress());
        }

        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }

        if (user.getSub() != null) {
            existingUser.setSub(user.getSub());
        }

        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }

        if (user.getVerifiedStatus() != null) {
            existingUser.setVerifiedStatus(user.getVerifiedStatus());
        }

        // Gender là boolean primitive (default = false), nên kiểm tra rõ ràng
        existingUser.setMale(user.isMale());

        // Cập nhật thời gian
        existingUser.setUpdatedAt(LocalDateTime.now());

        // Lưu lại
        User updatedUser = userRepository.save(existingUser);

        // Trả về DTO
        return mapper.apply(updatedUser);
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?") // chạy hàng ngày lúc 3h sáng
    public void cleanExpiredUsers() {
//        userRepository.deleteAllByIsVerifiedAndCreatedAtBefore(RegisterStatus.EXPIRED, LocalDateTime.now().minusDays(3));
    }
}
