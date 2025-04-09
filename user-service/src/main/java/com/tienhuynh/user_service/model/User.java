package com.tienhuynh.user_service.model;

import com.tienhuynh.user_service.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "User")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    private UUID id;

    @Column(name = "pwd_hash", nullable = false)
    private String pwdHash;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false, unique = true)
    private String phone;

    private String address;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cid")
    private CandidateProfile candidateProfile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rid")
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
