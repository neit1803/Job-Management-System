package com.tienhuynh.user_service.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EducationDto {
    private UUID id;
    private String education;
    private String school;
    private String city;
    private boolean present = false;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
