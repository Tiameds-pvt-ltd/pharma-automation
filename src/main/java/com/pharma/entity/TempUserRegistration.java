package com.pharma.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "pharma_user_registration_tmp")
public class TempUserRegistration {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Lob
    @Column(nullable = false)
    private String payloadJson;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
