package com.tienhuynh.user_service.dto;

import com.tienhuynh.user_service.model.Education;
import com.tienhuynh.user_service.model.Employment;
import com.tienhuynh.user_service.model.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileDto implements UserProfileDto{
    private UUID id;
    private String resume_url;
    private List<Education> educationList;
    private List<Employment> employmentList;
    private List<Skill> skillList;
}
