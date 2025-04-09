package com.tienhuynh.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Education")
@Data  @NoArgsConstructor  @AllArgsConstructor
public class Education {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String education;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private String city;

    private boolean present = false;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private CandidateProfile cid;
}
