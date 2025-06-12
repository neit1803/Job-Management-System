package com.tienhuynh.auth_service.service;

import com.tienhuynh.auth_service.payload.RegisterRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface CustomOAuthService {
    void logout();
    RegisterRequest fetchUserInfo(OAuth2User user);
}
