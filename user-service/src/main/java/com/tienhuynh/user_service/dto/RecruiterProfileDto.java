package com.tienhuynh.user_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor
public class RecruiterProfileDto {
    private UUID id;
    private UserDto user;
    private String taxCode;
    private String website;
    private String description;
}
