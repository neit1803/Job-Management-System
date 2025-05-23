package com.tienhuynh.auth_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienhuynh.auth_service.model.AuthRequest;
import com.tienhuynh.auth_service.model.LoginResponse;
import com.tienhuynh.auth_service.model.RegisterRequest;
import com.tienhuynh.auth_service.model.UserDTO;
import com.tienhuynh.auth_service.rabbitmq.RabbitMQProducer;
import com.tienhuynh.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private ObjectMapper jsonObjectMapper;

    public ResponseEntity login(AuthRequest req) {
        String resp = rabbitMQProducer.getUser(req);
        UserDTO user = jsonObjectMapper.convertValue(resp, UserDTO.class);

        if (user == null || !passwordEncoder.matches(req.getPwd_hash(), user.getPwdHash())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        return ResponseEntity.ok(generateToken(new AuthRequest(req.getMail(), req.getPwd_hash())));
    }

    public ResponseEntity register(RegisterRequest req) {
        req.pwd_hash = passwordEncoder.encode(req.pwd_hash);
        String resp = rabbitMQProducer.saveUser(req);
        if (resp.equals("SUCCESS")) {
            return ResponseEntity.ok().body(generateToken(new AuthRequest(req.getMail(), req.getPwd_hash())));
        }
        return ResponseEntity.badRequest().body(resp);
    }

    public LoginResponse generateToken(AuthRequest req) {
        String accessToken = jwtUtil.generateAccessToken(req.getMail(), req.getPwd_hash());
        String refreshToken = jwtUtil.generateRefreshToken(req.getMail(), req.getPwd_hash());
        String tokenType = "Bearer";
        int expiresIn = 15 * 60;

        return new LoginResponse(accessToken, refreshToken, tokenType, expiresIn, "");
    }
}