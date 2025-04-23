package com.tienhuynh.user_service.dto;

import com.tienhuynh.user_service.model.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDtoMapper implements Function<User, UserDto> {
    @Override
    public UserDto apply(User user) {
        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.getMail(),
                user.getPhone(),
                user.getAddress(),
                user.isVerified()
        );
    }
}
