package com.tienhuynh.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RecruiterProfileDto implements UserProfileDto{
    private UUID id;
    private String taxCode;
    private String website;
    private String description;
}
