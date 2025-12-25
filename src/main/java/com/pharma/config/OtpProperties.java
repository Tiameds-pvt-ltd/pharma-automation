package com.pharma.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.otp")
public class OtpProperties {

    private int expiryMinutes;
    private int maxAttempts;
    private int resendCooldownSeconds;
    private int maxResendCount;
}
