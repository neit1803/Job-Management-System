package com.tienhuynh.auth_service.payload;

import jakarta.persistence.Column;
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

    @Column(name = "pwd_hash")
    private String pwdHash;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    private String role;
}
