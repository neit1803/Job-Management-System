package com.tienhuynh.user_service.dto;

import com.tienhuynh.user_service.model.RecruiterProfile;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RecruiterProfileDtoMapper implements Function<RecruiterProfile, RecruiterProfileDto> {
    @Override
    public RecruiterProfileDto apply(RecruiterProfile profile) {
        return new RecruiterProfileDto(
                profile.getId(),
                profile.getTaxCode(),
                profile.getWebsite(),
                profile.getDescription()
        );
    }
}