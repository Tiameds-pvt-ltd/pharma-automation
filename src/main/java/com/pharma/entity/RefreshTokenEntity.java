package com.pharma.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "pharma_refresh_token",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "userId")
        }
)
@Getter
@Setter
public class RefreshTokenEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean revoked = false;
}
