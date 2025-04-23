package com.tienhuynh.user_service.model;

import jakarta.persistence.*;
import lombok.*;
import com.tienhuynh.user_service.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
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
    private Role role = Role.ROLE_CANDIDATE;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false, unique = true)
    private String phone;

    private String address;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

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
