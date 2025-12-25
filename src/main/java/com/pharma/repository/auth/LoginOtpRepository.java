package com.pharma.repository.auth;

import com.pharma.entity.auth.LoginOtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LoginOtpRepository extends JpaRepository<LoginOtpEntity, UUID>{

    Optional<LoginOtpEntity> findTopByEmailAndUsedFalseOrderByCreatedAtDesc(String email);

    @Modifying
    @Query("""
        UPDATE LoginOtpEntity o 
        SET o.used = true 
        WHERE o.email = :email
    """)
    void invalidateAllByEmail(String email);
}




