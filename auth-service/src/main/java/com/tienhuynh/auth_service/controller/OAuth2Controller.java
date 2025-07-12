package com.tienhuynh.auth_service.controller;

import com.tienhuynh.auth_service.payload.OAuth2Request;
import com.tienhuynh.auth_service.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/oauth2")
public class OAuth2Controller {

    @Autowired
    private OAuth2Service service;

    @GetMapping("/social-login")
    public ResponseEntity<?> oauthLogin(@RequestParam String loginType) {
        return ResponseEntity.ok(service.generateAuthUrl(loginType.toLowerCase().trim()));
    }

    @PostMapping("/code/{loginType}")
    public ResponseEntity<?> handleCallBack(@RequestBody OAuth2Request req) {
        return ResponseEntity.ok(service.handleProviderCallBack(req));
    }
}
