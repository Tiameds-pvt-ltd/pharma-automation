package com.pharma.repository;

import com.pharma.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByTokenAndRevokedFalse(String token);

    Optional<RefreshTokenEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    void deleteByExpiryDateBefore(LocalDateTime now);
}
