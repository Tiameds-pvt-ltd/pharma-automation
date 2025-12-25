package com.pharma.controller.auth;

import com.pharma.dto.auth.SendLoginOtpRequest;
import com.pharma.dto.auth.VerifyLoginOtpRequest;
import com.pharma.service.auth.LoginOtpService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth/loginOtp")
@RequiredArgsConstructor
public class LoginOtpAuthController {
    private final LoginOtpService loginOtpService;
    @PostMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestBody VerifyLoginOtpRequest req,
            HttpServletResponse response
    ) {

        Map<String, Object> tokens =
                loginOtpService.verifyOtp(
                        req.getUsername(),
                        req.getOtp()
                );

        ResponseCookie cookie = ResponseCookie.from(
                        "refreshToken",
                        tokens.get("refreshToken").toString()
                )
                .httpOnly(true)
                .secure(false) // true in prod
                .sameSite("Lax")
                .path("/")
                .maxAge(30L * 24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of(
                "accessToken", tokens.get("accessToken"),
                "expiresIn", tokens.get("expiresIn")
        ));
    }
}
