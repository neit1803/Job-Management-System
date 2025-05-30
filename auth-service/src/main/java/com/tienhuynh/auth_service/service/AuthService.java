package com.tienhuynh.auth_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienhuynh.auth_service.dto.UserDTO;
import com.tienhuynh.auth_service.payload.AuthRequest;
import com.tienhuynh.auth_service.payload.LoginResponse;
import com.tienhuynh.auth_service.payload.RegisterRequest;
import com.tienhuynh.auth_service.payload.UserPayload;
import com.tienhuynh.auth_service.rabbitmq.RabbitMQProducer;
import com.tienhuynh.auth_service.redis.RedisService;
import com.tienhuynh.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

        UserPayload user;
        try {
            user = jsonObjectMapper.readValue(resp, UserPayload.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid response from user service: " + e.getMessage());
        }
        return ResponseEntity.ok(generateToken(user.getMail(), user.getRole(), "Successfully logged in"));
    }

    public ResponseEntity register(RegisterRequest req) {
        req.pwd_hash = passwordEncoder.encode(req.pwd_hash);
        String resp = rabbitMQProducer.saveUser(req);
        if (resp.equals("SUCCESS")) {
            return ResponseEntity.ok().body(generateToken(req.getMail(), req.getRole(),  "Successfully registered"));
        }
        return ResponseEntity.badRequest().body(resp);
    }

    public ResponseEntity logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        String token = authHeader.split("Bearer ")[1];
        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        String userMail = jwtUtil.getEmailFromToken(token);
        String key = "refresh_token:" + userMail;

        if (!redisService.hasKey(key)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No active session found for user");
        }

        redisService.deleteValue(key);
        return ResponseEntity.ok().body("Successfully logged out " + userMail);
    }

    public LoginResponse generateToken(String mail, String role, String msg) {
        String accessToken = jwtUtil.generateAccessToken(mail, role);
        String refreshToken = jwtUtil.generateRefreshToken(mail, role);
        String tokenType = "Bearer";

        long expiresIn = jwtUtil.ACCESS_TOKEN_EXPIRE_MILLISECOND;

        redisService.setValue(
                "refresh_token:" + mail,
                refreshToken,
                jwtUtil.REFRESH_TOKEN_EXPIRE_MILLISECOND
        );

        return new LoginResponse(accessToken, refreshToken, tokenType, expiresIn, msg);
    }

    public ResponseEntity getEmailFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        String token = authHeader.split("Bearer ")[1];
        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        String userMail = jwtUtil.getEmailFromToken(token);
        String resp = rabbitMQProducer.getUser(new AuthRequest(userMail, ""));
        UserDTO user;
        try {
            user = jsonObjectMapper.readValue(resp, UserDTO.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid response from user service: " + e.getMessage());
        }
        return ResponseEntity.ok(user);
    }

    public ResponseEntity refreshToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        String token = authHeader.split("Bearer ")[1];
        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        String userMail = jwtUtil.getEmailFromToken(token);
        String storedRefreshToken = redisService.getValue("refresh_token:"+userMail).toString();
        String resp = rabbitMQProducer.getUser(new AuthRequest(userMail, ""));
        UserDTO user;
        try {
            user = jsonObjectMapper.readValue(resp, UserDTO.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid response from user service: " + e.getMessage());
        }

        if (storedRefreshToken == null || !storedRefreshToken.equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not recognized or revoked");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user.getMail(), user.getRole());
        String tokenType = "Bearer";
        long expiresIn = jwtUtil.ACCESS_TOKEN_EXPIRE_MILLISECOND;

        return ResponseEntity.ok(new LoginResponse(newAccessToken, token, tokenType, expiresIn, "Successfully refreshed token " + userMail));
    }
}