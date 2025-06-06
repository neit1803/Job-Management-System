package com.tienhuynh.api_gateway.config;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String msg;

        String exMsg = ex.getMessage().toLowerCase();

        if (ex instanceof AuthenticationCredentialsNotFoundException) {
            msg = "Missing Authorization Header";
        } else if (exMsg.contains("expired")) {
            msg = "Token has expired";
        } else if (exMsg.contains("malformed")) {
            msg = "Malformed token";
        } else if (exMsg.contains("signature")) {
            msg = "Invalid token signature";
        } else {
            msg = "Invalid or unauthorized token";
        }

        String body = "{\"error\": \"Unauthorized - " + msg + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}
