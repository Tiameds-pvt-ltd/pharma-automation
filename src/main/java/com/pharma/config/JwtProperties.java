package com.pharma.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    /**
     * RSA Private Key for signing JWT tokens (RS256)
     * Can be provided as:
     * - Environment variable: JWT_PRIVATE_KEY
     * - Base64 encoded string
     * - PEM format string (with headers/footers)
     */
    private String privateKey;

    /**
     * RSA Public Key for verifying JWT tokens (RS256)
     * Can be provided as:
     * - Environment variable: JWT_PUBLIC_KEY
     * - Base64 encoded string
     * - PEM format string (with headers/footers)
     */
    private String publicKey;

    /**
     * Path to private key file (classpath or file system)
     * Example: classpath:keys/private_key.pem
     */
    private String privateKeyPath;

    /**
     * Path to public key file (classpath or file system)
     * Example: classpath:keys/public_key.pem
     */
    private String publicKeyPath;

    /**
     * Legacy secret key (for backward compatibility during migration)
     * Will be ignored if privateKey/publicKey are provided
     */
    private String secret;

    @PostConstruct
    public void logProperties() {
        log.info("JwtProperties loaded - privateKeyPath: {}, publicKeyPath: {}, privateKey: {}, publicKey: {}", 
            privateKeyPath, publicKeyPath, 
            privateKey != null ? "set" : "null", 
            publicKey != null ? "set" : "null");
    }
}

