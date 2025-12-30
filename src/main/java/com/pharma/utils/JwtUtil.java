package com.pharma.utils;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final long ACCESS_TOKEN_EXPIRY = 15L * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_EXPIRY = 24L * 60 * 60 * 1000; // 1 day

    private final RsaKeyLoader rsaKeyLoader;
    
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            this.privateKey = rsaKeyLoader.loadPrivateKey();
            this.publicKey = rsaKeyLoader.loadPublicKey();
            log.info("RSA keys loaded successfully. Using RS256 algorithm for JWT signing.");
        } catch (Exception e) {
            log.error("Failed to initialize RSA keys", e);
            throw new IllegalStateException("JWT initialization failed: " + e.getMessage(), e);
        }
    }

    /**
     * Generates access token using RS256 algorithm
     */
    public String generateAccessToken(String username, Map<String, Object> claims) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            log.error("Failed to generate access token for user: {}", username, e);
            throw new RuntimeException("Token generation failed", e);
        }
    }

    /**
     * Generates refresh token using RS256 algorithm
     */
    public String generateRefreshToken(String username) {
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            log.error("Failed to generate refresh token for user: {}", username, e);
            throw new RuntimeException("Token generation failed", e);
        }
    }

    /**
     * Extracts username from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts expiration date from token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from token
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return resolver.apply(claims);
        } catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            log.error("JWT parsing failed", e);
            throw new RuntimeException("Invalid token", e);
        }
    }

    /**
     * Checks if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            log.error("Failed to check token expiration", e);
            return true; // Treat as expired if we can't parse
        }
    }

    /**
     * Validates access token by checking username match and expiration
     */
    public boolean validateAccessToken(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            boolean usernameMatches = tokenUsername.equals(username);
            boolean notExpired = !isTokenExpired(token);
            return usernameMatches && notExpired;
        } catch (Exception e) {
            log.error("Token validation failed for user: {}", username, e);
            return false;
        }
    }
}
