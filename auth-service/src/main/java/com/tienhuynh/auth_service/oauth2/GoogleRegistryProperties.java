package com.tienhuynh.auth_service.oauth2;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public class GoogleRegistryProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private List<String> scope;

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String authorizationUri;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoUri;
    @Value("${spring.security.oauth2.client.provider.google.user-name-attribute}")
    private String userNameAttribute;
}
