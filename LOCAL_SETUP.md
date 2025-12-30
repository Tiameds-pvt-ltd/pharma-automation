# Local Development Setup - RS256 JWT

## Quick Start Guide

### Option 1: Using Key Files (Recommended for Local Dev)

#### Step 1: Generate RSA Key Pair

**Windows (PowerShell):**
```powershell
# Navigate to project root
cd C:\Users\abhis\IdeaProjects\pharma-automation

# Generate private key (2048-bit for local dev)
openssl genpkey -algorithm RSA -out src\main\resources\keys\private_key.pem -pkeyopt rsa_keygen_bits:2048

# Extract public key
openssl rsa -pubout -in src\main\resources\keys\private_key.pem -out src\main\resources\keys\public_key.pem
```

**Linux/Mac:**
```bash
# Generate private key
openssl genpkey -algorithm RSA -out src/main/resources/keys/private_key.pem -pkeyopt rsa_keygen_bits:2048

# Extract public key
openssl rsa -pubout -in src/main/resources/keys/private_key.pem -out src/main/resources/keys/public_key.pem
```

#### Step 2: Verify Files Created

Check that these files exist:
- `src/main/resources/keys/private_key.pem`
- `src/main/resources/keys/public_key.pem`

#### Step 3: Run Application

The application is already configured in `application-dev.yml` to load keys from:
- `classpath:keys/private_key.pem`
- `classpath:keys/public_key.pem`

Just start your Spring Boot application normally!

---

### Option 2: Using Environment Variables

If you prefer environment variables over files:

#### Step 1: Generate Keys (same as above)

#### Step 2: Read Key Contents

**Windows PowerShell:**
```powershell
# Read private key (replace \n with actual newlines)
$privateKey = Get-Content src\main\resources\keys\private_key.pem -Raw
$publicKey = Get-Content src\main\resources\keys\public_key.pem -Raw

# Set environment variables
$env:JWT_PRIVATE_KEY = $privateKey
$env:JWT_PUBLIC_KEY = $publicKey
```

**Linux/Mac:**
```bash
export JWT_PRIVATE_KEY="$(cat src/main/resources/keys/private_key.pem)"
export JWT_PUBLIC_KEY="$(cat src/main/resources/keys/public_key.pem)"
```

#### Step 3: Update application-dev.yml

Uncomment these lines in `application-dev.yml`:
```yaml
jwt:
  # Use environment variables instead of file paths
  private-key: ${JWT_PRIVATE_KEY:}
  public-key: ${JWT_PUBLIC_KEY:}
  # Comment out the file paths
  # private-key-path: classpath:keys/private_key.pem
  # public-key-path: classpath:keys/public_key.pem
```

---

## Verification

After starting the application, check the logs. You should see:
```
RSA keys loaded successfully. Using RS256 algorithm for JWT signing.
```

If you see an error, check:
1. Keys are in the correct location
2. Keys are valid PEM format
3. File permissions allow reading

---

## Troubleshooting

### Error: "RSA Private Key not found"
- Make sure keys are in `src/main/resources/keys/`
- Check file names are exactly: `private_key.pem` and `public_key.pem`
- Verify keys are valid PEM format

### Error: "Failed to parse key"
- Ensure keys are valid RSA keys
- Check for extra whitespace or encoding issues
- Regenerate keys if needed

### Keys not loading from classpath
- Make sure you're running with `dev` profile: `spring.profiles.active=dev`
- Check that keys directory is in `src/main/resources/keys/` (not `src/main/resources/keys/keys/`)

---

## Security Reminder

✅ **Safe for Local Development:**
- Keys in `src/main/resources/keys/` are gitignored
- These are development-only keys
- Never commit private keys

❌ **Never Do:**
- Use production keys locally
- Commit private keys to Git
- Share private keys with others

