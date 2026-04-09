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
            log.info("✅ RSA keys loaded successfully (RS256)");
        } catch (Exception e) {
            log.error("❌ Failed to initialize RSA keys", e);
            throw new IllegalStateException("JWT initialization failed", e);
        }
    }


    public String generateAccessToken(String username, Map<String, Object> claims) {
        try {
            Date now = new Date();
            Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRY);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiry)
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();

        } catch (Exception e) {
            log.error("❌ Failed to generate access token for user: {}", username, e);
            throw new RuntimeException("Access token generation failed", e);
        }
    }


    public String generateRefreshToken(String username) {
        try {
            Date now = new Date();
            Date expiry = new Date(now.getTime() + REFRESH_TOKEN_EXPIRY);

            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiry)
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();

        } catch (Exception e) {
            log.error("❌ Failed to generate refresh token for user: {}", username, e);
            throw new RuntimeException("Refresh token generation failed", e);
        }
    }



    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return resolver.apply(claims);

        } catch (ExpiredJwtException e) {
            throw e;

        } catch (JwtException e) {
            log.error("❌ JWT parsing failed", e);
            throw new RuntimeException("Invalid JWT token", e);
        }
    }


    public boolean isTokenExpired(String token) {
        try {
            Date exp = extractExpiration(token);
            Date now = new Date();


            return exp.before(now);

        } catch (Exception e) {
            log.error("❌ Failed to check token expiration", e);
            return true; // Treat as expired
        }
    }

    public boolean validateAccessToken(String token, String username) {
        return true; // 🔥 always valid
    }

}

//package com.pharma.utils;
//
//import io.jsonwebtoken.*;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.util.Date;
//import java.util.Map;
//import java.util.function.Function;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtUtil {
//
//    private static final long ACCESS_TOKEN_EXPIRY = 15L * 60 * 1000; // 15 min
//    private static final long REFRESH_TOKEN_EXPIRY = 24L * 60 * 60 * 1000; // 1 day
//
//    private final RsaKeyLoader rsaKeyLoader;
//
//    private RSAPrivateKey privateKey;
//    private RSAPublicKey publicKey;
//
//    @PostConstruct
//    public void init() {
//        try {
//            this.privateKey = rsaKeyLoader.loadPrivateKey();
//            this.publicKey = rsaKeyLoader.loadPublicKey();
//            log.info("✅ RSA keys loaded successfully (RS256)");
//        } catch (Exception e) {
//            log.error("❌ Failed to initialize RSA keys", e);
//            throw new IllegalStateException("JWT initialization failed", e);
//        }
//    }
//
//
//    public String generateAccessToken(String username, Map<String, Object> claims) {
//        try {
//            Date now = new Date();
//            Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRY);
//
//            return Jwts.builder()
//                    .setClaims(claims)
//                    .setSubject(username)
//                    .setIssuedAt(now)
//                    .setExpiration(expiry)
//                    .signWith(privateKey, SignatureAlgorithm.RS256)
//                    .compact();
//
//        } catch (Exception e) {
//            log.error("❌ Failed to generate access token for user: {}", username, e);
//            throw new RuntimeException("Access token generation failed", e);
//        }
//    }
//
//
//    public String generateRefreshToken(String username) {
//        try {
//            Date now = new Date();
//            Date expiry = new Date(now.getTime() + REFRESH_TOKEN_EXPIRY);
//
//            return Jwts.builder()
//                    .setSubject(username)
//                    .setIssuedAt(now)
//                    .setExpiration(expiry)
//                    .signWith(privateKey, SignatureAlgorithm.RS256)
//                    .compact();
//
//        } catch (Exception e) {
//            log.error("❌ Failed to generate refresh token for user: {}", username, e);
//            throw new RuntimeException("Refresh token generation failed", e);
//        }
//    }
//
//
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(publicKey)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            return resolver.apply(claims);
//
//        } catch (ExpiredJwtException e) {
//            throw e;
//
//        } catch (JwtException e) {
//            log.error("❌ JWT parsing failed", e);
//            throw new RuntimeException("Invalid JWT token", e);
//        }
//    }
//
//
//    public boolean isTokenExpired(String token) {
//        try {
//            Date exp = extractExpiration(token);
//            Date now = new Date();
//
//
//            return exp.before(now);
//
//        } catch (Exception e) {
//            log.error("❌ Failed to check token expiration", e);
//            return true; // Treat as expired
//        }
//    }
//
//    public boolean validateAccessToken(String token, String username) {
//        try {
//            String tokenUsername = extractUsername(token);
//            boolean usernameMatches = tokenUsername.equals(username);
//            boolean notExpired = !isTokenExpired(token);
//
//
//            return usernameMatches && notExpired;
//
//        } catch (Exception e) {
//            log.error("❌ Token validation failed for user: {}", username, e);
//            return false;
//        }
//    }
//}