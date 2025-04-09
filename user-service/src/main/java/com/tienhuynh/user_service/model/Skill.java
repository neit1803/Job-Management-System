package com.tienhuynh.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "Employment")
@Data @NoArgsConstructor @AllArgsConstructor
public class Skill {
    @Id
    private UUID id;

    @Column(nullable = false, length = 50)
    private String skill;

    private int level = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    private CandidateProfile cid;
}
