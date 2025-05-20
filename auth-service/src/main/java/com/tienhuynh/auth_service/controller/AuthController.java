package com.tienhuynh.auth_service.controller;

import com.tienhuynh.auth_service.model.AuthRequest;
import com.tienhuynh.auth_service.model.RegisterRequest;
import com.tienhuynh.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req) {
        log.info("Register request: {}", req);
        return ResponseEntity.ok(authService.register(req));
    }
}
