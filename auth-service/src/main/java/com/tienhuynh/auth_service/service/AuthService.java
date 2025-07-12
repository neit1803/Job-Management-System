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
import org.springframework.web.server.ResponseStatusException;

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

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token format");
        }
        return authHeader.substring(7).trim();
    }

    private String validateAndExtractMailFromRefreshToken(String token) {
        try {
            return jwtUtil.getMailFromToken(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or malformed token");
        }
    }

    public ResponseEntity<?> login(AuthRequest req) {
        String resp = rabbitMQProducer.getUser(req.getMail());

        UserPayload user;
        try {
            user = jsonObjectMapper.readValue(resp, UserPayload.class);
            if (passwordEncoder.matches(req.pwd_hash, user.getPwd_Hash())){
                return ResponseEntity.ok(generateToken(user.getMail(), user.getRole(), "Successfully logged in"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid mail");
        }
        return ResponseEntity.badRequest().body("Invalid password");
    }

    public ResponseEntity<?> register(RegisterRequest req) {
        req.pwd_hash = passwordEncoder.encode(req.pwd_hash);
        String resp = rabbitMQProducer.saveUser(req);
        if (resp.equals("SUCCESSFULLY REGISTERED")) {
            return ResponseEntity.ok().body(generateToken(req.getMail(), req.getRole(),  "Successfully registered"));
        }
        return ResponseEntity.badRequest().body(resp);
    }

    public ResponseEntity<?> logout(String authHeader) {
        String token = extractToken(authHeader);
        String email = validateAndExtractMailFromRefreshToken(token);

        String key = "refresh_token:" + email;
        Object storedTokenObj = redisService.getValue(key);

        if (storedTokenObj == null || !token.equals(storedTokenObj.toString())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token not recognized or revoked");
        }

        redisService.deleteValue(key);
        return ResponseEntity.ok("Successfully logged out " + email);
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

    public ResponseEntity<?> getEmailFromToken(String authHeader) {
        String token = extractToken(authHeader);
        String mail;
        try {
            mail = jwtUtil.getMailFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or malformed token");
        }

        String resp = rabbitMQProducer.getUser(mail);
        UserDTO user;
        try {
            user = jsonObjectMapper.readValue(resp, UserDTO.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid response from user service: " + e.getMessage());
        }

        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> refreshToken(String authHeader) {
        String token = extractToken(authHeader);
        String mail = validateAndExtractMailFromRefreshToken(token);

        // Lấy user từ User-Service qua RabbitMQ
        String response = rabbitMQProducer.getUser(mail);
        UserDTO user;
        try {
            user = jsonObjectMapper.readValue(response, UserDTO.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid response from user service: " + e.getMessage());
        }

        String newAccessToken = jwtUtil.generateAccessToken(user.getMail(), user.getRole());
        return ResponseEntity.ok(new LoginResponse(
                newAccessToken,
                token,
                "Bearer",
                jwtUtil.ACCESS_TOKEN_EXPIRE_MILLISECOND,
                "Successfully refreshed token for " + mail
        ));
    }
}