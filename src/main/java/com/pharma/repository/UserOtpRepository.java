package com.pharma.repository;

import com.pharma.entity.UserOtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserOtpRepository extends JpaRepository<UserOtpEntity, UUID> {

    Optional<UserOtpEntity> findTopByEmailOrderByCreatedAtDesc(String email);

    void deleteByEmail(String email);

    @Modifying
    @Query("DELETE FROM UserOtpEntity o WHERE o.expiresAt < :now OR o.attemptCount >= :maxAttempts")
    void deleteExpiredOrLockedOtps(
            @Param("now") LocalDateTime now,
            @Param("maxAttempts") int maxAttempts
    );

}
