package com.pharma.repository;

import com.pharma.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByTokenAndRevokedFalse(String token);

    List<RefreshTokenEntity>
    findAllByUserIdAndRevokedFalseAndExpiryDateAfter(
            Long userId,
            LocalDateTime now
    );


    void deleteByUserId(Long userId);

    boolean existsByUserId(Long userId);

}
