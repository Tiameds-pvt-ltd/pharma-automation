package com.pharma.service.impl;

import com.pharma.entity.RefreshTokenEntity;
import com.pharma.repository.RefreshTokenRepository;
import com.pharma.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

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
        entity.setExpiryDate(LocalDateTime.now().plusDays(30));

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
}

