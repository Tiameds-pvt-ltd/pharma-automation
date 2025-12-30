package com.pharma.controller;

import com.pharma.dto.ResendOtpDto;
import com.pharma.dto.VerifyOtpDto;
import com.pharma.dto.auth.LoginRequest;
import com.pharma.dto.auth.RegisterRequest;
import com.pharma.entity.RefreshTokenEntity;
import com.pharma.entity.User;
import com.pharma.repository.auth.UserRepository;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.AuthService;
import com.pharma.service.auth.LoginOtpService;
import com.pharma.service.impl.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.pharma.utils.JwtUtil;
import jakarta.servlet.http.Cookie;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final LoginOtpService loginOtpService;

    @Value("${app.env}")
    private String appEnv;

    @Value("${app.cookie.domain:}")
    private String cookieDomain;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        loginOtpService.sendOtpAfterPasswordAuth(request.getUsername());

        return ResponseEntity.ok(Map.of(
                "otpRequired", true,
                "message", "OTP sent to registered email"
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request,
                                     HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(401).build();
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }

        RefreshTokenEntity tokenEntity =
                refreshTokenService.validateRefreshToken(refreshToken);

        User user = userRepository.findById(tokenEntity.getUserId())
                .orElseThrow();

        boolean isProd = "prod".equalsIgnoreCase(appEnv);

        String newAccessToken = jwtUtil.generateAccessToken(
                user.getUsername(),
                Map.of(
                        "roles",
                        user.getRoles().stream()
                                .map(r -> "ROLE_" + r.getName())
                                .toList()
                )
        );

        String newRefreshToken =
                refreshTokenService.rotateRefreshToken(tokenEntity);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(isProd)
                .sameSite(isProd ? "None" : "Lax")
                .domain(isProd ? cookieDomain : null)
                .path("/")
                .maxAge(15 * 60)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(isProd)
                .sameSite(isProd ? "None" : "Lax")
                .domain(isProd ? cookieDomain : null)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().build();
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                    .filter(c -> "refreshToken".equals(c.getName()))
                    .findFirst()
                    .ifPresent(c -> refreshTokenService.revokeToken(c.getValue()));
        }

        // DEV CONFIG
//        ResponseCookie deleteAccess = ResponseCookie.from("accessToken", "")
//                .httpOnly(true)
//                .secure(false)
//                .sameSite("Lax")
//                .path("/")
//                .maxAge(0)
//                .build();
//
//        ResponseCookie deleteRefresh = ResponseCookie.from("refreshToken", "")
//                .httpOnly(true)
//                .secure(false)
//                .sameSite("Lax")
//                .path("/")
//                .maxAge(0)
//                .build();

        boolean isProd = "prod".equalsIgnoreCase(appEnv);

        ResponseCookie deleteAccess = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(isProd)
                .sameSite(isProd ? "None" : "Lax")
                .domain(isProd ? cookieDomain : null)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie deleteRefresh = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(isProd)
                .sameSite(isProd ? "None" : "Lax")
                .domain(isProd ? cookieDomain : null)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefresh.toString());

        return ResponseEntity.ok().build();
    }


    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(Map.of(
                "id", userDetails.getUserId(),
                "username", userDetails.getUsername(),
                "firstName", userDetails.getFirstName(),
                "lastName", userDetails.getLastName(),
                "roles", userDetails.getRoles()
        ));
    }


    @PostMapping("/login/resend-otp")
    public ResponseEntity<?> resendLoginOtp(@Valid @RequestBody LoginRequest request) {
        loginOtpService.sendOtpAfterPasswordAuth(request.getUsername());
        return ResponseEntity.ok("OTP resent successfully");
    }

}
