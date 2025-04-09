package com.tienhuynh.user_service.dto;

import com.tienhuynh.user_service.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor
public class UserDto {
    private UUID id;
    private String fullName;
    private Role role;
    private String mail;
    private String phone;
    private String address;
    private boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CandidateProfileDto candidateProfile;
    private RecruiterProfileDto recruiterProfile;
}
