package com.pharma.service.impl;

import com.pharma.entity.PasswordResetTokenEntity;
import com.pharma.entity.User;
import com.pharma.repository.PasswordResetTokenRepository;
import com.pharma.repository.RefreshTokenRepository;
import com.pharma.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${frontend.reset-password-url}")
    private String resetPasswordUrl;

    @Transactional
    public void sendResetLink(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String rawToken = UUID.randomUUID().toString();

        String tokenHash = passwordEncoder.encode(rawToken);

        PasswordResetTokenEntity tokenEntity = new PasswordResetTokenEntity();
        tokenEntity.setUserId(user.getId());
        tokenEntity.setTokenHash(tokenHash);
        tokenEntity.setExpiryAt(LocalDateTime.now().plusMinutes(15));
        tokenEntity.setUsed(false);

        resetTokenRepository.save(tokenEntity);

        String resetLink = resetPasswordUrl + "?token=" + rawToken;

        sendEmail(user.getEmail(), resetLink);
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {

        List<PasswordResetTokenEntity> validTokens =
                resetTokenRepository.findAllValidTokens(LocalDateTime.now());

        PasswordResetTokenEntity token = validTokens.stream()
                .filter(t -> passwordEncoder.matches(rawToken, t.getTokenHash()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        refreshTokenRepository.deleteByUserId(user.getId());
        token.setUsed(true);
        resetTokenRepository.save(token);
    }

    private void sendEmail(String to, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset your password");
        message.setText(
                "You requested a password reset.\n\n" +
                        "Click the link below to reset your password:\n" +
                        resetLink + "\n\n" +
                        "This link is valid for 15 minutes.\n\n" +
                        "If you did not request this, please ignore this email."
        );

        mailSender.send(message);
    }
}
