package com.tienhuynh.auth_service.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2Request {
    private String code;
    private String loginType;
    private String role;
}
