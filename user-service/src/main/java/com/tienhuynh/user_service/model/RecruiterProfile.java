package com.tienhuynh.user_service.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table(name = "Recruiter_Profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfile {
    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "uid",   unique = true,  nullable = false)
    private User user;

    @Column(name = "tax_code", nullable = false, unique = true)
    private String taxCode;

    @Column(nullable = false, unique = true)
    private String website;

    private String description;
}
