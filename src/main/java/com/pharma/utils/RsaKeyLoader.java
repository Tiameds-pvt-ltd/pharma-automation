package com.pharma.utils;

import com.pharma.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class RsaKeyLoader {

    private final JwtProperties jwtProperties;

    /**
     * Loads RSA private key from various sources:
     * 1. Environment variable (JWT_PRIVATE_KEY)
     * 2. Configuration property (security.jwt.private-key)
     * 3. File path (security.jwt.private-key-path)
     */
    public RSAPrivateKey loadPrivateKey() {
        try {
            // Priority 1: Environment variable
            String privateKeyContent = System.getenv("JWT_PRIVATE_KEY");
            log.info("Loading RSA private key - Environment variable JWT_PRIVATE_KEY: {}", privateKeyContent != null ? "set" : "not set");
            
            // Priority 2: Configuration property
            if (privateKeyContent == null || privateKeyContent.isEmpty()) {
                privateKeyContent = jwtProperties.getPrivateKey();
                log.info("Configuration property security.jwt.private-key: {}", privateKeyContent != null ? "set" : "not set");
            }

            // Priority 3: File path (classpath or filesystem)
            if (privateKeyContent == null || privateKeyContent.isEmpty()) {
                String keyPath = jwtProperties.getPrivateKeyPath();
                log.info("Configuration property security.jwt.private-key-path: {}", keyPath);
                if (keyPath != null && !keyPath.isEmpty()) {
                    log.info("Attempting to load key from path: {}", keyPath);
                    privateKeyContent = loadKeyFromPath(keyPath);
                    log.info("Key loaded from path: {}", privateKeyContent != null ? "success" : "failed");
                } else {
                    log.warn("privateKeyPath is null or empty!");
                }
            }

            if (privateKeyContent == null || privateKeyContent.isEmpty()) {
                throw new IllegalStateException(
                    "RSA Private Key not found. Provide JWT_PRIVATE_KEY environment variable, " +
                    "security.jwt.private-key property, or security.jwt.private-key-path property."
                );
            }

            return parsePrivateKey(privateKeyContent);
        } catch (Exception e) {
            log.error("Failed to load RSA private key", e);
            throw new IllegalStateException("Failed to load RSA private key: " + e.getMessage(), e);
        }
    }

    /**
     * Loads RSA public key from various sources:
     * 1. Environment variable (JWT_PUBLIC_KEY)
     * 2. Configuration property (security.jwt.public-key)
     * 3. File path (security.jwt.public-key-path)
     */
    public RSAPublicKey loadPublicKey() {
        try {
            // Priority 1: Environment variable
            String publicKeyContent = System.getenv("JWT_PUBLIC_KEY");
            
            // Priority 2: Configuration property
            if (publicKeyContent == null || publicKeyContent.isEmpty()) {
                publicKeyContent = jwtProperties.getPublicKey();
            }

            // Priority 3: File path (classpath or filesystem)
            if (publicKeyContent == null || publicKeyContent.isEmpty()) {
                String keyPath = jwtProperties.getPublicKeyPath();
                if (keyPath != null && !keyPath.isEmpty()) {
                    publicKeyContent = loadKeyFromPath(keyPath);
                }
            }

            if (publicKeyContent == null || publicKeyContent.isEmpty()) {
                throw new IllegalStateException(
                    "RSA Public Key not found. Provide JWT_PUBLIC_KEY environment variable, " +
                    "security.jwt.public-key property, or security.jwt.public-key-path property."
                );
            }

            return parsePublicKey(publicKeyContent);
        } catch (Exception e) {
            log.error("Failed to load RSA public key", e);
            throw new IllegalStateException("Failed to load RSA public key: " + e.getMessage(), e);
        }
    }

    /**
     * Loads key content from file path (supports classpath: and filesystem paths)
     */
    private String loadKeyFromPath(String keyPath) throws IOException {
        if (keyPath.startsWith("classpath:")) {
            // Load from classpath
            String resourcePath = keyPath.substring("classpath:".length());
            Resource resource = new ClassPathResource(resourcePath);
            if (!resource.exists()) {
                throw new IOException("Key file not found in classpath: " + resourcePath);
            }
            return new String(resource.getInputStream().readAllBytes());
        } else {
            // Load from filesystem
            return Files.readString(Paths.get(keyPath));
        }
    }

    /**
     * Parses private key from PEM or base64 format
     */
    private RSAPrivateKey parsePrivateKey(String keyContent) throws Exception {
        String cleanedKey = cleanKeyContent(keyContent, "PRIVATE KEY");
        byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);
        
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        
        if (privateKey instanceof RSAPrivateKey) {
            return (RSAPrivateKey) privateKey;
        }
        throw new IllegalArgumentException("Key is not an RSA private key");
    }

    /**
     * Parses public key from PEM or base64 format
     */
    private RSAPublicKey parsePublicKey(String keyContent) throws Exception {
        String cleanedKey = cleanKeyContent(keyContent, "PUBLIC KEY");
        byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);
        
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        
        if (publicKey instanceof RSAPublicKey) {
            return (RSAPublicKey) publicKey;
        }
        throw new IllegalArgumentException("Key is not an RSA public key");
    }

    /**
     * Cleans key content by removing PEM headers, footers, and whitespace
     */
    private String cleanKeyContent(String keyContent, String keyType) {
        if (keyContent == null) {
            return null;
        }
        
        // Remove PEM headers and footers
        String cleaned = keyContent
            .replace("-----BEGIN " + keyType + "-----", "")
            .replace("-----END " + keyType + "-----", "")
            .replace("-----BEGIN RSA " + keyType + "-----", "")
            .replace("-----END RSA " + keyType + "-----", "")
            .replaceAll("\\s", ""); // Remove all whitespace
        
        return cleaned;
    }
}

