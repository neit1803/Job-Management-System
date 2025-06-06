package com.tienhuynh.api_gateway.config;

import com.tienhuynh.api_gateway.filters.JwtAuthFilter;
import lombok.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {
            "/auth/login",
            "/auth/register",
            "/users/**"
    };

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(WHITE_LIST_URL).permitAll()
                        .pathMatchers(HttpMethod.GET, "/users/**").hasAnyRole("ADMIN", "RECRUITER", "SYSTEM")
                        .anyExchange().authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, JwtAuthFilter jwtAuthFilter, ServiceUriConfig config) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/refresh-token", "/auth/me", "/auth/logout")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri(config.getAuthServiceUri()))
                .build();
    }
}
