package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "pharma_user_otp")
public class UserOtpEntity {
    @Id
    @GeneratedValue
    private UUID userOtpId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otpHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean verified = false;

    @Column(nullable = false)
    private int attemptCount = 0;

    @Column(nullable = false)
    private LocalDateTime lastSentAt;

    @Column(nullable = false)
    private int resendCount;


    private LocalDateTime createdAt = LocalDateTime.now();
}
