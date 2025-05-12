package com.tienhuynh.auth_service.model;

import java.util.Map;

public class RegisterRequest {
    public String password;
    public String email;
    public String role;
    public Map<String,Object> profile;
}
