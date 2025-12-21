package com.pharma.controller;

import com.pharma.dto.ResendOtpDto;
import com.pharma.dto.VerifyOtpDto;
import com.pharma.dto.auth.RegisterRequest;
import com.pharma.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerInit(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.registerInit(registerRequest);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/register/verify")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpDto verifyOtpDto) {
        authService.verifyOtpAndRegister(verifyOtpDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register/resend-otp")
    public ResponseEntity<?> resendOtp(@Valid @RequestBody ResendOtpDto dto) {
        authService.resendOtp(dto.getEmail());
        return ResponseEntity.ok("OTP resent successfully");
    }

}
