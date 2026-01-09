package com.pharma.service.impl;

import com.pharma.entity.RefreshTokenEntity;
import com.pharma.entity.User;
import com.pharma.exception.UserAlreadyLoggedInException;
import com.pharma.repository.RefreshTokenRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepo;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

//    public void assertUserNotLoggedIn(Long userId) {
//
//        List<RefreshTokenEntity> activeTokens =
//                refreshTokenRepo.findAllByUserIdAndRevokedFalseAndExpiryDateAfter(
//                        userId,
//                        LocalDateTime.now()
//                );
//
//        if (!activeTokens.isEmpty()) {
//            throw new UserAlreadyLoggedInException();
//        }
//    }

    public void assertUserNotLoggedIn(Long userId) {

        boolean exists = refreshTokenRepo.existsByUserId(userId);

        if (exists) {
            throw new UserAlreadyLoggedInException();
        }
    }


    public String createRefreshToken(Long userId, String username) {

        refreshTokenRepo.deleteByUserId(userId);

        String token = jwtUtil.generateRefreshToken(username);

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUserId(userId);
        entity.setToken(token);
        entity.setExpiryDate(LocalDateTime.now().plusDays(30));
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

    @Transactional
    public void revokeToken(String token) {
        try {
            refreshTokenRepo.findByTokenAndRevokedFalse(token)
                    .ifPresent(t -> refreshTokenRepo.deleteByUserId(t.getUserId()));
        } catch (Exception e) {
            // logout must always succeed
        }
    }

    public String rotateRefreshToken(RefreshTokenEntity oldToken) {

        refreshTokenRepo.deleteByUserId(oldToken.getUserId());

        String newToken =
                jwtUtil.generateRefreshToken(
                        jwtUtil.extractUsername(oldToken.getToken())
                );

        RefreshTokenEntity newEntity = new RefreshTokenEntity();
        newEntity.setUserId(oldToken.getUserId());
        newEntity.setToken(newToken);
        newEntity.setExpiryDate(LocalDateTime.now().plusDays(30));
        newEntity.setRevoked(false);

        refreshTokenRepo.save(newEntity);
        return newToken;
    }
}




//package com.pharma.service.impl;
//
//import com.pharma.entity.RefreshTokenEntity;
//import com.pharma.repository.RefreshTokenRepository;
//import com.pharma.utils.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//
//@Service
//@RequiredArgsConstructor
//public class RefreshTokenService {
//
//    private final RefreshTokenRepository refreshTokenRepo;
//    private final JwtUtil jwtUtil;
//
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
//
//    public RefreshTokenEntity validateRefreshToken(String token) {
//
//        RefreshTokenEntity entity = refreshTokenRepo
//                .findByTokenAndRevokedFalse(token)
//                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
//
//        if (entity.getExpiryDate().isBefore(LocalDateTime.now())) {
//            throw new RuntimeException("Refresh token expired");
//        }
//
//        return entity;
//    }
//
//    public void revokeToken(String token) {
//        refreshTokenRepo.findByTokenAndRevokedFalse(token)
//                .ifPresent(t -> {
//                    t.setRevoked(true);
//                    refreshTokenRepo.save(t);
//                });
//    }
//
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
//
//}
//
