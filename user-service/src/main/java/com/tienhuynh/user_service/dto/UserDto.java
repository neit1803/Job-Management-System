package com.tienhuynh.user_service.dto;

import com.tienhuynh.user_service.enums.RegisterStatus;
import com.tienhuynh.user_service.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import java.util.UUID;
@Data
@NoArgsConstructor @AllArgsConstructor
public class UserDto <T extends UserProfileDto>{
    private UUID id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String mail;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String address;

    private String sub;

    private Role role;

    private RegisterStatus verifiedStatus;

    private boolean male;

    private T profile;
}
