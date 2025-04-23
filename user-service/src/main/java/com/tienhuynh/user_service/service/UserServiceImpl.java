package com.tienhuynh.user_service.service;

import com.tienhuynh.user_service.dto.UserDto;
import com.tienhuynh.user_service.dto.UserDtoMapper;
import com.tienhuynh.user_service.exception.UserNotFoundException;
import com.tienhuynh.user_service.model.User;
import com.tienhuynh.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public UserDto getUser(UUID id) {
        return userRepository
                .findById(id)
                .map(mapper)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public UserDto save(User user) {
        userRepository.save(user);
        return mapper.apply(user);
    }

    public UserDto update(UUID id, UserDto userDto) {
        return userRepository.
                findById(id)
                .map(mapper)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
