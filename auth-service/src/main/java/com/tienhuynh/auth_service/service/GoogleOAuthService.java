package com.tienhuynh.auth_service.service;

import com.tienhuynh.auth_service.payload.RegisterRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GoogleOAuthService implements CustomOAuthService {
    @Override
    public RegisterRequest fetchUserInfo(OAuth2User user) {
        RegisterRequest resp = new RegisterRequest();
        resp.setSub("google-"+ user.getAttribute("sub"));
        resp.setMail(user.getAttribute("email"));
        resp.setFull_name(user.getAttribute("name"));
        resp.setPwd_hash("Password123");
        return resp;
    }

    @Override
    public void logout() {

    }
}
