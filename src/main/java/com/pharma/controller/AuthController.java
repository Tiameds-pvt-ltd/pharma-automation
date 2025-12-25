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

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request,
//                                   HttpServletResponse response) {
//
//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );
//
//        CustomUserDetails userDetails =
//                (CustomUserDetails) auth.getPrincipal();
//
//        String accessToken = jwtUtil.generateAccessToken(
//                userDetails.getUsername(),
//                Map.of("roles", userDetails.getAuthorities().stream()
//                        .map(a -> a.getAuthority())
//                        .toList())
//        );
//
//        String refreshToken = refreshTokenService.createRefreshToken(
//                userDetails.getUserId(),
//                userDetails.getUsername()
//        );
//// For Prod
//        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
//                .httpOnly(true)
//                .secure(true)              // 🔒 HTTPS only
//                .sameSite("None")          // 🔥 REQUIRED for subdomains
//                .domain(".tiameds.ai")     // 🔥 SHARE ACROSS SUBDOMAINS
//                .path("/")                 // 🔥 available everywhere
//                .maxAge(30L * 24 * 60 * 60)
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//
//
//        // For Dev
////        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
////                .httpOnly(true)
////                .secure(false)      // ✅ localhost fix
////                .sameSite("Lax")    // ✅ Postman/browser friendly
////                .path("/")          // ✅ send cookie to all endpoints
////                .maxAge(30 * 24 * 60 * 60)
////                .build();
////
////
////        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//
//        return ResponseEntity.ok(Map.of(
//                "accessToken", accessToken,
//                "expiresIn", 900
//        ));
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 🔐 STEP-UP: send OTP only
        loginOtpService.sendOtpAfterPasswordAuth(request.getUsername());

        return ResponseEntity.ok(Map.of(
                "otpRequired", true,
                "message", "OTP sent to registered email"
        ));
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(401).body("Refresh token missing");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(401).body("Refresh token missing");
        }

        RefreshTokenEntity tokenEntity =
                refreshTokenService.validateRefreshToken(refreshToken);

        User user = userRepository.findById(tokenEntity.getUserId())
                .orElseThrow();

        String newAccessToken = jwtUtil.generateAccessToken(
                user.getUsername(),
                Map.of(
                        "roles",
                        user.getRoles().stream()
                                .map(r -> "ROLE_" + r.getName())
                                .toList()
                )
        );

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "expiresIn", 900
        ));
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    HttpServletResponse response) {

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken != null) {
            refreshTokenService.revokeToken(refreshToken);
        }

        // For Prod
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .domain(".tiameds.ai")
                .path("/")
                .maxAge(0)          // 🔥 DELETE COOKIE
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());



        // For Dev
//        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
//                .httpOnly(true)
//                .secure(false)      // ✅ localhost fix
//                .sameSite("Lax")    // ✅ Postman/browser friendly
//                .path("/")          // ✅ send cookie to all endpoints
//                .maxAge(30 * 24 * 60 * 60)
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

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
