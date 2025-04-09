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

    @OneToOne(mappedBy = "candidateProfile")
    private User user;

    private String resume_url;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cid")
    private List<Education> educationList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cid")
    private List<Employment> employmentList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cid")
    private List<Skill> skillList;
}
