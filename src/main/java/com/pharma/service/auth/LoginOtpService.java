package com.pharma.service.auth;

import com.pharma.config.OtpProperties;
import com.pharma.entity.User;
import com.pharma.entity.auth.LoginOtpEntity;
import com.pharma.repository.auth.LoginOtpRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.impl.EmailService;
import com.pharma.service.impl.RefreshTokenService;
import com.pharma.utils.JwtUtil;
import com.pharma.utils.LoginOtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginOtpService {

    private final OtpProperties otpProperties;

    private final LoginOtpRepository loginOtpRepository;
    private final UserRepository userRepository;
    private final LoginOtpGenerator loginOtpGenerator;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


    public void sendOtpAfterPasswordAuth(String username) {

        User user = userRepository.findByUsername(username)
                .filter(User::isEnabled)
                .orElseThrow(() ->
                        new RuntimeException("If user exists, OTP will be sent"));

        String email = user.getEmail();

        // 🔒 Cooldown + resend count check
        loginOtpRepository
                .findTopByEmailAndUsedFalseOrderByCreatedAtDesc(email)
                .ifPresent(existingOtp -> {

                    if (existingOtp.getCreatedAt()
                            .isAfter(LocalDateTime.now()
                                    .minusSeconds(
                                            otpProperties.getResendCooldownSeconds()
                                    ))) {
                        throw new RuntimeException(
                                "Please wait before requesting another OTP"
                        );
                    }

                    if (existingOtp.getAttemptCount()
                            >= otpProperties.getMaxResendCount()) {
                        throw new RuntimeException(
                                "OTP resend limit exceeded"
                        );
                    }
                });

        // Invalidate previous OTPs
        loginOtpRepository.invalidateAllByEmail(email);

        String rawOtp = loginOtpGenerator.generate();

        LoginOtpEntity otp = LoginOtpEntity.builder()
                .email(email)
                .otpHash(passwordEncoder.encode(rawOtp))
                .expiryTime(
                        LocalDateTime.now()
                                .plusMinutes(otpProperties.getExpiryMinutes())
                )
                .attemptCount(0)   // guessing attempts
                .used(false)
                .build();

        loginOtpRepository.save(otp);
        emailService.sendOtp(email, rawOtp);
    }

    public Map<String, Object> verifyOtp(String username, String otp) {

        User user = userRepository.findByUsername(username)
                .filter(User::isEnabled)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        LoginOtpEntity otpEntity = loginOtpRepository
                .findTopByEmailAndUsedFalseOrderByCreatedAtDesc(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpEntity.setUsed(true);
            throw new RuntimeException("OTP expired");
        }

        if (otpEntity.getAttemptCount() >= otpProperties.getMaxAttempts()) {
            otpEntity.setUsed(true);
            throw new RuntimeException("Too many attempts");
        }

        if (!passwordEncoder.matches(otp, otpEntity.getOtpHash())) {
            otpEntity.setAttemptCount(otpEntity.getAttemptCount() + 1);
            throw new RuntimeException("Invalid OTP");
        }

        otpEntity.setUsed(true);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles",
                user.getRoles().stream().map(r -> r.getName()).toList());
        claims.put("userId", user.getId());

        return Map.of(
                "accessToken",
                jwtUtil.generateAccessToken(user.getUsername(), claims),
                "refreshToken",
                refreshTokenService.createRefreshToken(
                        user.getId(), user.getUsername()),
                "expiresIn", 900
        );
    }
}
