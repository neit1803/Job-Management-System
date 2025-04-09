package com.tienhuynh.user_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor
public class CandidateProfileDto {
    private UUID id;
    private UserDto user;
    private String resume_url;
    private List<EducationDto> educationList;
    private List<EmploymentDto> employmentList;
    private List<SkillDto> skillList;

}
