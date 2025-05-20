package com.tienhuynh.auth_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private String id;
    private String mail;
    private String encodedPassword;
    private String fullName;
    private String phone;
    private String role;
}
