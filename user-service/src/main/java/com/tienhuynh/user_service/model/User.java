package com.tienhuynh.user_service.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tienhuynh.user_service.enums.RegisterStatus;
import jakarta.persistence.*;
import lombok.*;
import com.tienhuynh.user_service.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "pwd_hash", nullable = false)
    private String pwdHash;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CANDIDATE;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false, unique = true)
    private String phone;

    private String address;

    private boolean gender = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_verified", nullable = false)
    private RegisterStatus isVerified = RegisterStatus.PENDING_VERIFICATION;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CandidateProfile candidateProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RecruiterProfile recruiterProfile;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
