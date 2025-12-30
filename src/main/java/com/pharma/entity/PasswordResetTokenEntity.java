package com.pharma.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "pharma_password_reset_token")
public class PasswordResetTokenEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "expiry_at", nullable = false)
    private LocalDateTime expiryAt;

    @Column(nullable = false)
    private boolean used = false;
}
