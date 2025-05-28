package com.tienhuynh.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    public final long ACCESS_TOKEN_EXPIRE_MILLISECOND = 15 * 60 * 1000;
    public final long REFRESH_TOKEN_EXPIRE_MILLISECOND = 7 * 24 * 60 * 60 * 1000;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Access token: valid for 1 hour
    public String generateAccessToken(String mail) {
        return generateToken(mail, ACCESS_TOKEN_EXPIRE_MILLISECOND); // 15 minutes
    }

    // Refresh token: valid for 7 days
    public String generateRefreshToken(String mail) {
        return generateToken(mail, REFRESH_TOKEN_EXPIRE_MILLISECOND); // 7 days
    }

    private String generateToken(String mail, long expirationMillis) {
        return Jwts.builder()
                .setSubject(mail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
