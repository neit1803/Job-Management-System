package com.tienhuynh.user_service.dto;

import com.tienhuynh.user_service.model.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDtoMapper implements Function<User, UserDto<? extends UserProfileDto>> {

    private final CandidateProfileDtoMapper candidateProfileDtoMapper;
    private final RecruiterProfileDtoMapper recruiterProfileDtoMapper;

    public UserDtoMapper(CandidateProfileDtoMapper candidateProfileDtoMapper, RecruiterProfileDtoMapper recruiterProfileDtoMapper) {
        this.candidateProfileDtoMapper = candidateProfileDtoMapper;
        this.recruiterProfileDtoMapper = recruiterProfileDtoMapper;
    }

    @Override
    public UserDto<? extends UserProfileDto> apply(User user) {
        UserProfileDto profileDto = switch (user.getRole()) {
            case RECRUITER -> recruiterProfileDtoMapper.apply(user.getRecruiterProfile());
            case CANDIDATE -> candidateProfileDtoMapper.apply(user.getCandidateProfile());
            default -> null;
        };

        return new UserDto<>(
                user.getId(),
                user.getFullName(),
                user.getMail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                profileDto
        );
    }
}
