package com.tienhuynh.auth_service.service;

import com.tienhuynh.auth_service.model.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    JwtService jwtService;

    public String login(AuthRequest req) {
        return null;
    }

    public String register(AuthRequest req) {
        return null;
    }

    public String refreshToken(String token) {
        return null;
    }

    public String logout(String token) {
        return null;
    }
}
