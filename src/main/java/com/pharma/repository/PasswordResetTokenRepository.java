package com.pharma.repository;

import com.pharma.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, UUID> {

    @Query("""
        SELECT t FROM PasswordResetTokenEntity t
        WHERE t.used = false
          AND t.expiryAt > :now
    """)
    List<PasswordResetTokenEntity> findAllValidTokens(LocalDateTime now);

    void deleteByUserId(Long userId);
}
