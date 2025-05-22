package com.tienhuynh.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EmploymentDto implements UserProfileDto {
    private UUID id;
    private String position;
    private String employer;
    private String city;
    private boolean present = false;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

}
