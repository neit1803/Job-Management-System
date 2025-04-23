package com.tienhuynh.user_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Candidate_Profile")
@Data
@NoArgsConstructor  @AllArgsConstructor
public class CandidateProfile {
    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "uid", unique = true,  nullable = false)
    private User user;

    private String resume_url;

    @OneToMany(mappedBy = "cid", cascade = CascadeType.ALL)
    private List<Education> educationList;

    @OneToMany(mappedBy = "cid", cascade = CascadeType.ALL)
    private List<Employment> employmentList;

    @OneToMany(mappedBy = "cid", cascade = CascadeType.ALL)
    private List<Skill> skillList;
}
