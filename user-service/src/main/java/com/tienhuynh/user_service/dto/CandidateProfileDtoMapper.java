package com.tienhuynh.user_service.dto;

import com.tienhuynh.user_service.model.CandidateProfile;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CandidateProfileDtoMapper implements Function<CandidateProfile, CandidateProfileDto> {
    @Override
    public CandidateProfileDto apply(CandidateProfile profile) {
        return new CandidateProfileDto(
                profile.getId(),
                profile.getResume_url(),
                profile.getEducationList(),
                profile.getEmploymentList(),
                profile.getSkillList()
        );
    }
}