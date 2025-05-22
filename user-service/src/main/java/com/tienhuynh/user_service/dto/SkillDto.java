package com.tienhuynh.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SkillDto {
    private UUID id;
    private String skill;
    private int level = 1;
}
