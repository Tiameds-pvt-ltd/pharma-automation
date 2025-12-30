# Generate RSA Keys - Quick Guide

## Method 1: Run Java Class (Easiest)

1. **Open IntelliJ IDEA**
2. **Navigate to:** `src/main/java/com/pharma/utils/GenerateRsaKeys.java`
3. **Right-click** on the file → **Run 'GenerateRsaKeys.main()'**
4. Keys will be generated in `src/main/resources/keys/`

## Method 2: Using Maven

```powershell
# From project root
mvn exec:java -Dexec.mainClass="com.pharma.utils.GenerateRsaKeys"
```

## Method 3: Install OpenSSL (Windows)

### Option A: Using Chocolatey
```powershell
choco install openssl
```

### Option B: Download from:
https://slproweb.com/products/Win32OpenSSL.html

Then run:
```powershell
openssl genpkey -algorithm RSA -out src\main\resources\keys\private_key.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in src\main\resources\keys\private_key.pem -out src\main\resources\keys\public_key.pem
```

## After Generating Keys

1. Verify files exist:
   - `src/main/resources/keys/private_key.pem`
   - `src/main/resources/keys/public_key.pem`

2. **Restart your Spring Boot application**

3. You should see in logs:
   ```
   RSA keys loaded successfully. Using RS256 algorithm for JWT signing.
   ```

## Troubleshooting

- **Keys not found**: Make sure keys are in `src/main/resources/keys/` (not `src/main/resources/keys/keys/`)
- **Permission denied**: Check file permissions
- **Invalid key format**: Regenerate keys using the Java utility



