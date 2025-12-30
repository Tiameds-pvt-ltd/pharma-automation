# JWT RS256 Setup Guide

## Overview
This project now uses **RS256** algorithm with RSA key pairs for JWT token signing and verification.

## Key Generation

### Generate RSA Key Pair (2048-bit recommended for production)

```bash
# Generate private key
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048

# Extract public key from private key
openssl rsa -pubout -in private_key.pem -out public_key.pem
```

### For Production (4096-bit - stronger security)
```bash
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:4096
openssl rsa -pubout -in private_key.pem -out public_key.pem
```

## Configuration by Environment

### Development
- Place keys in: `src/main/resources/keys/`
  - `private_key.pem`
  - `public_key.pem`
- Configuration: `application-dev.yml` loads from classpath

### Test/Production
- Store keys in **AWS Secrets Manager**:
  - `JWT_PRIVATE_KEY` - Full PEM content (including headers)
  - `JWT_PUBLIC_KEY` - Full PEM content (including headers)
- Configuration: `application-prod.yml` loads from environment variables

## AWS Secrets Manager Setup

1. Add secrets to AWS Secrets Manager:
   ```bash
   # Private Key (keep secure!)
   aws secretsmanager put-secret-value \
     --secret-id tiamedlabsecret-SM3XcQ \
     --secret-string '{"JWT_PRIVATE_KEY":"-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----"}'
   
   # Public Key
   aws secretsmanager put-secret-value \
     --secret-id tiamedlabsecret-SM3XcQ \
     --secret-string '{"JWT_PUBLIC_KEY":"-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----"}'
   ```

2. Update ECS Task Definition to include:
   ```json
   {
     "name": "JWT_PRIVATE_KEY",
     "valueFrom": "arn:aws:secretsmanager:ap-south-1:977098986605:secret:tiamedlabsecret-SM3XcQ:JWT_PRIVATE_KEY::"
   },
   {
     "name": "JWT_PUBLIC_KEY",
     "valueFrom": "arn:aws:secretsmanager:ap-south-1:977098986605:secret:tiamedlabsecret-SM3XcQ:JWT_PUBLIC_KEY::"
   }
   ```

## Key Format
Keys can be provided in:
- **PEM format** (with headers/footers): `-----BEGIN PRIVATE KEY-----...-----END PRIVATE KEY-----`
- **Base64 encoded** (without headers)
- **Environment variables**: `JWT_PRIVATE_KEY`, `JWT_PUBLIC_KEY`
- **File paths**: `classpath:keys/private_key.pem` or filesystem paths

## Security Notes
⚠️ **CRITICAL**: 
- Never commit private keys to Git (already in `.gitignore`)
- Use different keys for each environment
- Rotate keys periodically
- Private key should only be accessible to the application server
- Public key can be shared (used for verification only)

## Migration Impact
- All existing HS256 tokens will become invalid
- Users will need to re-authenticate after deployment
- Refresh tokens in database should be cleared or users will need to login again

