package com.pharma.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class LoginOtpGenerator {

    private final SecureRandom random = new SecureRandom();

    public String generate() {
        return String.valueOf(100000 + random.nextInt(900000));
    }
}
