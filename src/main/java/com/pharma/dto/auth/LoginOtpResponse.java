package com.pharma.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginOtpResponse {
    private String accessToken;
    private long expiresIn;
}
