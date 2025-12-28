package com.pharma.controller.auth;

import com.pharma.dto.auth.VerifyLoginOtpRequest;
import com.pharma.service.auth.LoginOtpService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.env}")
    private String appEnv;


    @PostMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestBody VerifyLoginOtpRequest req,
            HttpServletResponse response
    ) {
        Map<String, Object> tokens =
                loginOtpService.verifyOtp(req.getUsername(), req.getOtp());

        String accessToken = tokens.get("accessToken").toString();
        String refreshToken = tokens.get("refreshToken").toString();

        // DEV CONFIG
//        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
//                .httpOnly(true)
//                .secure(false)       // ✅ MUST be false in localhost
//                .sameSite("Lax")     // ✅ Lax for localhost
//                .path("/")           // ✅ middleware needs this
//                .maxAge(15 * 60)
//                .build();
//
//        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
//                .httpOnly(true)
//                .secure(false)
//                .sameSite("Lax")
//                .path("/")           // 🔥 IMPORTANT for refresh visibility
//                .maxAge(24 * 60 * 60)
//                .build();


        boolean isProd = "prod".equalsIgnoreCase(appEnv);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(isProd)
                .sameSite(isProd ? "None" : "Lax")
                .domain(isProd ? ".tiameds.ai" : null)
                .path("/")
                .maxAge(15 * 60)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(isProd)
                .sameSite(isProd ? "None" : "Lax")
                .domain(isProd ? ".tiameds.ai" : null)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().build();
    }


}
