package com.tienhuynh.auth_service.controller;

import com.tienhuynh.auth_service.model.AuthRequest;
import com.tienhuynh.auth_service.model.AuthResponse;
import com.tienhuynh.auth_service.model.RegisterRequest;
import com.tienhuynh.auth_service.rabbitmq.RabbitMQProducer;
import com.tienhuynh.auth_service.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    private RabbitMQProducer producer;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(new AuthResponse());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
        producer.sendCheckUserRequest(req);
        return ResponseEntity.ok("Hello");
    }
}
