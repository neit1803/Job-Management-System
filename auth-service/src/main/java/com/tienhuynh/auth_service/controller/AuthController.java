package com.tienhuynh.auth_service.controller;

import com.tienhuynh.auth_service.payload.AuthRequest;
import com.tienhuynh.auth_service.payload.RegisterRequest;
import com.tienhuynh.auth_service.service.AuthService;
import jakarta.validation.Valid;
import jakarta.ws.rs.HeaderParam;
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
    public ResponseEntity login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity refreshToken(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.refreshToken(authHeader));
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.logout(authHeader));
    }

    @GetMapping("/me")
    public ResponseEntity getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.getEmailFromToken(authHeader));
    }
}
