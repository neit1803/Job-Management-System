package com.tienhuynh.auth_service.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
//                .formLogin(oauth -> oauth.disable())
//                .oauth2Login(oauth -> oauth
//                        .successHandler(new CustomOAuth2SuccessHandler()));
        return http.build();
    }
}