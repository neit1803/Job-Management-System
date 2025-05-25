package com.tienhuynh.auth_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienhuynh.auth_service.payload.AuthRequest;
import com.tienhuynh.auth_service.payload.LoginResponse;
import com.tienhuynh.auth_service.payload.RegisterRequest;
import com.tienhuynh.auth_service.payload.UserDTO;
import com.tienhuynh.auth_service.rabbitmq.RabbitMQProducer;
import com.tienhuynh.auth_service.redis.RedisService;
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
    private RedisService redisService;

    @Autowired
    private ObjectMapper jsonObjectMapper;

    public ResponseEntity login(AuthRequest req) {
        String resp = rabbitMQProducer.getUser(req);
        UserDTO user = jsonObjectMapper.convertValue(resp, UserDTO.class);

        if (user == null || !passwordEncoder.matches(req.getPwd_hash(), user.getPwdHash())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        return ResponseEntity.ok(generateToken(new AuthRequest(req.getMail(), req.getPwd_hash()), "Successfully logged in"));
    }

    public ResponseEntity register(RegisterRequest req) {
        req.pwd_hash = passwordEncoder.encode(req.pwd_hash);
        String resp = rabbitMQProducer.saveUser(req);
        if (resp.equals("SUCCESS")) {
            return ResponseEntity.ok().body(generateToken(new AuthRequest(req.getMail(), req.getPwd_hash()),  "Successfully registered"));
        }
        return ResponseEntity.badRequest().body(resp);
    }

    public LoginResponse generateToken(AuthRequest req, String msg) {
        String accessToken = jwtUtil.generateAccessToken(req.getMail(), req.getPwd_hash());
        String refreshToken = jwtUtil.generateRefreshToken(req.getMail(), req.getPwd_hash());
        String tokenType = "Bearer";

        long expiresIn = jwtUtil.ACCESS_TOKEN_EXPIRE_MILLISECOND;

        redisService.setValue(
                "refresh_token:" + req.getMail(),
                refreshToken,
                jwtUtil.REFRESH_TOKEN_EXPIRE_MILLISECOND
        );

        return new LoginResponse(accessToken, refreshToken, tokenType, expiresIn, msg);
    }
}