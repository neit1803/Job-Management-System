package com.tienhuynh.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "Skill")
@Data @NoArgsConstructor @AllArgsConstructor
public class Skill {
    @Id
    private UUID id;

    @Column(nullable = false, length = 50)
    private String skill;

    private int level = 1;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cid", nullable = false, unique = true)
    private CandidateProfile cid;
}
