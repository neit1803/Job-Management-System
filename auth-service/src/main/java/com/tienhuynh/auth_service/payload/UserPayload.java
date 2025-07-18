package com.tienhuynh.auth_service.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPayload implements Serializable {
    private String id;

    private String mail;

    @JsonProperty("pwd_hash")
    private String pwdHash;

    @JsonProperty("full_name")
    private String fullName;

    private String phone;

    private String role;

    @JsonProperty("verified_status")
    private String status;
}
