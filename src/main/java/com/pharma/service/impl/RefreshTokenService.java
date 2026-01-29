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

    public String createRefreshToken(Long userId, String username) {

        String token = jwtUtil.generateRefreshToken(username);

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUserId(userId);
        entity.setToken(token);
        entity.setExpiryDate(LocalDateTime.now().plusDays(1));
        entity.setRevoked(false);

        refreshTokenRepo.save(entity);
        return token;
    }

//    public RefreshTokenEntity validateRefreshToken(String token) {
//
//        RefreshTokenEntity entity = refreshTokenRepo
//                .findByTokenAndRevokedFalse(token)
//                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
//
//        if (entity.getExpiryDate().isBefore(LocalDateTime.now())) {
//            entity.setRevoked(true);
//            refreshTokenRepo.save(entity);
//            throw new RuntimeException("Refresh token expired");
//        }
//
//        return entity;
//    }

    public RefreshTokenEntity validateRefreshToken(String token) {

        RefreshTokenEntity entity = refreshTokenRepo
                .findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (entity.getExpiryDate().isBefore(LocalDateTime.now())) {
            entity.setRevoked(true);
            refreshTokenRepo.save(entity);
            throw new RuntimeException("Refresh token expired");
        }

        // 🔥 SLIDING EXPIRY
        entity.setExpiryDate(LocalDateTime.now().plusDays(1));
        refreshTokenRepo.save(entity);

        return entity;
    }


    public void revokeToken(String token) {
        refreshTokenRepo.findByTokenAndRevokedFalse(token)
                .ifPresent(t -> {
                    t.setRevoked(true);
                    refreshTokenRepo.save(t);
                });
    }

    public void cleanupExpiredTokens() {
        refreshTokenRepo.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}

