package com.tienhuynh.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class JwtUtil {

    @Value("${spring.jwt.private-key}")
    private String privateKeyPath;

    @Value("${spring.jwt.public-key}")
    private String publicKeyPath;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public final long ACCESS_TOKEN_EXPIRE_MILLISECOND = 15 * 60 * 1000;
    public final long REFRESH_TOKEN_EXPIRE_MILLISECOND = 7 * 24 * 60 * 60 * 1000;

    @PostConstruct
    public void init() {
        try {
            this.privateKey = loadPrivateKey(privateKeyPath);
            this.publicKey = loadPublicKey(publicKeyPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load RSA key pair from PEM files", e);
        }
    }

    private PrivateKey loadPrivateKey(String path) throws Exception {
        String keyPem = new String(Files.readAllBytes(Paths.get(path)));
        String privateKeyPEM = keyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    private PublicKey loadPublicKey(String path) throws Exception {
        String keyPem = new String(Files.readAllBytes(Paths.get(path)));
        String publicKeyPEM = keyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }

    // Access token: valid for 15 minutes
    public String generateAccessToken(String mail, String role) {
        return generateToken(mail, role, ACCESS_TOKEN_EXPIRE_MILLISECOND); // 15 minutes
    }

    // Refresh token: valid for 7 days
    public String generateRefreshToken(String mail, String role) {
        return generateToken(mail, role, REFRESH_TOKEN_EXPIRE_MILLISECOND); // 7 days
    }

    private String generateToken(String mail, String role, long expirationMillis) {
        return Jwts.builder()
                .setSubject(mail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .claim("role", role)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error( e.getMessage());
        }
        return false;
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
