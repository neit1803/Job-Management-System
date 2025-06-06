package com.tienhuynh.user_service.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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
