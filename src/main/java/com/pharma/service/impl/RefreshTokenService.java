package com.pharma.service.impl;

import com.pharma.entity.RefreshTokenEntity;
import com.pharma.repository.RefreshTokenRepository;
import com.pharma.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepo;
    private final JwtUtil jwtUtil;

//    public String createRefreshToken(Long userId, String username) {
//
//        String token = jwtUtil.generateRefreshToken(username);
//
//        RefreshTokenEntity entity = new RefreshTokenEntity();
//        entity.setUserId(userId);
//        entity.setToken(token);
//        entity.setExpiryDate(LocalDateTime.now().plusDays(30));
//
//        refreshTokenRepo.save(entity);
//
//        return token;
//    }

    public String createRefreshToken(Long userId, String username) {

        String token = jwtUtil.generateRefreshToken(username);

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUserId(userId);
        entity.setToken(token);
        entity.setExpiryDate(LocalDateTime.now().plusDays(1)); // or 30
        entity.setRevoked(false);

        refreshTokenRepo.save(entity);

        return token;
    }



    public RefreshTokenEntity validateRefreshToken(String token) {

        RefreshTokenEntity entity = refreshTokenRepo
                .findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (entity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return entity;
    }

    public void revokeToken(String token) {
        refreshTokenRepo.findByTokenAndRevokedFalse(token)
                .ifPresent(t -> {
                    t.setRevoked(true);
                    refreshTokenRepo.save(t);
                });
    }

//    public String rotateRefreshToken(RefreshTokenEntity oldToken) {
//
//        // 1️⃣ Revoke old token
//        oldToken.setRevoked(true);
//        refreshTokenRepo.save(oldToken);
//
//        // 2️⃣ Create new refresh token
//        String newToken = jwtUtil.generateRefreshToken(
//                jwtUtil.extractUsername(oldToken.getToken())
//        );
//
//        RefreshTokenEntity newEntity = new RefreshTokenEntity();
//        newEntity.setUserId(oldToken.getUserId());
//        newEntity.setToken(newToken);
//        newEntity.setExpiryDate(LocalDateTime.now().plusDays(30));
//        newEntity.setRevoked(false);
//
//        // 3️⃣ Save new token
//        refreshTokenRepo.save(newEntity);
//
//        // 4️⃣ Return new token
//        return newToken;
//    }

    public String rotateRefreshToken(RefreshTokenEntity oldToken) {

        String newToken = jwtUtil.generateRefreshToken(
                jwtUtil.extractUsername(oldToken.getToken())
        );

        oldToken.setToken(newToken);
        oldToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        oldToken.setRevoked(false);

        refreshTokenRepo.save(oldToken);

        return newToken;
    }

}

