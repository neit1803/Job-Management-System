package com.tienhuynh.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "Recruiter_Profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfile {
    @Id
    private UUID id;

    @OneToOne(mappedBy = "recruiterProfile")
    private User user;

    @Column(name = "tax_code", nullable = false, unique = true)
    private String taxCode;

    @Column(nullable = false, unique = true)
    private String website;

    private String description;
}
