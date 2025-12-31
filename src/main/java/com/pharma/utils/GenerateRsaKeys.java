package com.pharma.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Utility class to generate RSA key pair for JWT signing
 * Run this main method once to generate keys for local development
 */
public class GenerateRsaKeys {

    public static void main(String[] args) {
        try {
            // Create keys directory if it doesn't exist
            String keysDir = "src/main/resources/keys";
            Files.createDirectories(Paths.get(keysDir));

            // Generate RSA key pair (2048-bit)
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // Convert to PEM format
            String privateKeyPem = convertToPEM(privateKey, "PRIVATE KEY");
            String publicKeyPem = convertToPEM(publicKey, "PUBLIC KEY");

            // Write private key
            try (FileWriter writer = new FileWriter(keysDir + "/private_key.pem")) {
                writer.write(privateKeyPem);
            }

            // Write public key
            try (FileWriter writer = new FileWriter(keysDir + "/public_key.pem")) {
                writer.write(publicKeyPem);
            }

            System.out.println("✅ RSA keys generated successfully!");
            System.out.println("Private key: " + keysDir + "/private_key.pem");
            System.out.println("Public key: " + keysDir + "/public_key.pem");
            System.out.println("\nYou can now run your Spring Boot application.");

        } catch (Exception e) {
            System.err.println("❌ Failed to generate keys: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String convertToPEM(java.security.Key key, String keyType) {
        byte[] keyBytes = key.getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN ").append(keyType).append("-----\n");
        
        // Split into 64-character lines
        for (int i = 0; i < base64Key.length(); i += 64) {
            int end = Math.min(i + 64, base64Key.length());
            pem.append(base64Key.substring(i, end)).append("\n");
        }
        
        pem.append("-----END ").append(keyType).append("-----\n");
        
        return pem.toString();
    }
}



