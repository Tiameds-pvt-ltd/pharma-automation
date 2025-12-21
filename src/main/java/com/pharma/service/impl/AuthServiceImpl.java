package com.pharma.service.impl;

import com.pharma.dto.VerifyOtpDto;
import com.pharma.dto.auth.RegisterRequest;
import com.pharma.entity.TempUserRegistration;
import com.pharma.entity.User;
import com.pharma.entity.UserOtpEntity;
import com.pharma.repository.TempUserRegistrationRepository;
import com.pharma.repository.UserOtpRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.AuthService;
import com.pharma.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserOtpRepository userOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TempUserRegistrationRepository tempUserRegistrationRepository;
    private final ObjectMapper objectMapper;
    private final OtpAttemptService otpAttemptService;
    private final UserService userService;

    private static final int OTP_EXPIRY_MIN = 10;
    private static final int MAX_ATTEMPTS = 5;

    private static final int RESEND_COOLDOWN_SECONDS = 60;
    private static final int MAX_RESEND_COUNT = 5;

    @Transactional
    @Override
    public void registerInit(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        tempUserRegistrationRepository.deleteByEmail(registerRequest.getEmail());

        TempUserRegistration temp = new TempUserRegistration();
        temp.setEmail(registerRequest.getEmail());
        try {
            temp.setPayloadJson(objectMapper.writeValueAsString(registerRequest));
        } catch (Exception e) {
            throw new RuntimeException("Failed to process registration data", e);
        }
        temp.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        tempUserRegistrationRepository.save(temp);

        handleOtpSend(registerRequest.getEmail());

    }

    private void handleOtpSend(String email) {

        userOtpRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .ifPresent(existingOtp -> {

                    if (existingOtp.getLastSentAt()
                            .isAfter(LocalDateTime.now().minusSeconds(RESEND_COOLDOWN_SECONDS))) {
                        throw new RuntimeException("Please wait before requesting another OTP");
                    }

                    if (existingOtp.getResendCount() >= MAX_RESEND_COUNT) {
                        throw new RuntimeException("OTP resend limit exceeded");
                    }
                });

        userOtpRepository.deleteByEmail(email);

        String otp = generateOtp();

        UserOtpEntity otpEntity = new UserOtpEntity();
        otpEntity.setEmail(email);
        otpEntity.setOtpHash(passwordEncoder.encode(otp));
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MIN));
        otpEntity.setLastSentAt(LocalDateTime.now());
        otpEntity.setResendCount(1);

        userOtpRepository.save(otpEntity);
        emailService.sendOtp(email, otp);
    }


    @Transactional
    @Override
    public void verifyOtpAndRegister(VerifyOtpDto dto) {

        UserOtpEntity otpEntity = userOtpRepository
                .findTopByEmailOrderByCreatedAtDesc(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otpEntity.isVerified()) throw new RuntimeException("OTP already used");
        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");
        if (otpEntity.getAttemptCount() >= MAX_ATTEMPTS)
            throw new RuntimeException("Max OTP attempts exceeded");

        if (!passwordEncoder.matches(dto.getOtp(), otpEntity.getOtpHash())) {
            otpAttemptService.incrementAttempt(otpEntity);
            throw new RuntimeException("Invalid OTP");

        }

        otpEntity.setVerified(true);
        userOtpRepository.save(otpEntity);

        TempUserRegistration temp = tempUserRegistrationRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Registration data not found"));

        RegisterRequest request;
        try {
            request = objectMapper.readValue(
                    temp.getPayloadJson(), RegisterRequest.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to read registration data", e);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setZip(request.getZip());
        user.setCountry(request.getCountry());
        user.setVerified(true);
        user.setEnabled(true);

        userService.saveUser(user);

        tempUserRegistrationRepository.deleteByEmail(dto.getEmail());
        userOtpRepository.deleteByEmail(dto.getEmail());
    }

    @Transactional
    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }


    @Transactional
    @Override
    public void resendOtp(String email) {

        // Registration must exist
        tempUserRegistrationRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Registration not found"
                ));

        UserOtpEntity otpEntity = userOtpRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "OTP not found"
                ));

        if (otpEntity.isVerified()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "OTP already verified"
            );
        }

        // Cooldown check (60 seconds)
        if (otpEntity.getLastSentAt()
                .isAfter(LocalDateTime.now().minusSeconds(RESEND_COOLDOWN_SECONDS))) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Please wait 60 seconds before requesting another OTP"
            );
        }

        // Resend limit check
        if (otpEntity.getResendCount() >= MAX_RESEND_COUNT) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "OTP resend limit exceeded"
            );
        }

        // Generate new OTP
        String newOtp = generateOtp();

        otpEntity.setOtpHash(passwordEncoder.encode(newOtp));
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MIN));
        otpEntity.setLastSentAt(LocalDateTime.now());
        otpEntity.setResendCount(otpEntity.getResendCount() + 1);
        otpEntity.setAttemptCount(0);
        otpEntity.setVerified(false);

        userOtpRepository.save(otpEntity);

        // Async email (non-blocking)
        emailService.sendOtp(email, newOtp);
    }


}
