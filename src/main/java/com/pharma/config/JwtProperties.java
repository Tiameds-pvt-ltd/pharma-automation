package com.pharma.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
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
}

