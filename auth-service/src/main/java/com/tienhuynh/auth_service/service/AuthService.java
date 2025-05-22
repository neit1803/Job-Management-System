package com.tienhuynh.auth_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienhuynh.auth_service.model.AuthRequest;
import com.tienhuynh.auth_service.model.RegisterRequest;
import com.tienhuynh.auth_service.model.UserDTO;
import com.tienhuynh.auth_service.rabbitmq.RabbitMQProducer;
import com.tienhuynh.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
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

    public String login(AuthRequest req) {
        String resp = rabbitMQProducer.getUser(req);
        UserDTO user = jsonObjectMapper.convertValue(resp, UserDTO.class);

        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPwdHash())) {
            return "Invalid credentials";
        }

        String accessToken = jwtUtil.generateAccessToken(user.getMail(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getMail(), user.getRole());
        return accessToken;
    }

    public String register(RegisterRequest req) {
        req.pwd_hash = passwordEncoder.encode(req.pwd_hash);
        return rabbitMQProducer.saveUser(req);
    }
}