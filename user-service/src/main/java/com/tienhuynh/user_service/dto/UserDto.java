package com.tienhuynh.user_service.dto;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import java.util.UUID;
@Data
@NoArgsConstructor @AllArgsConstructor
public class UserDto {
    private UUID id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String mail;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String address;
    private boolean isVerified;
}
