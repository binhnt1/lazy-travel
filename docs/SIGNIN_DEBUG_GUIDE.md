# SignIn 400 Error Debugging Guide

## Summary of Improvements Made

I've added several improvements to help debug and fix the SignIn 400 error:

1. **Email Format Validation**: Added regex-based validation that catches invalid email formats before sending to API
2. **Password Trimming**: Added `.trim()` to password to remove accidental whitespace
3. **Enhanced Error Messages**: Now shows specific error messages about which field is invalid
4. **Improved Debug Logging**: Better structured logs with clear prefixes (DEBUG SignIn:, DEBUG SignUp:, etc.)

## How to Test and Debug

### Step 1: Rebuild and Run the App

```bash
# Clean and rebuild
./gradlew clean build

# Or rebuild in IDE
```

### Step 2: Test with Valid Credentials

After signup is successful, use the EXACT SAME credentials to sign in:

```
Email: test@example.com
Password: YourPassword123
```

### Step 3: Check the Logcat Output

When clicking Sign In button, look for logs like:

```
DEBUG SignIn: signInWithEmail() called
DEBUG SignIn: Email: 'test@example.com' (trimmed: 'test@example.com')
DEBUG SignIn: Password length: 16
DEBUG SignIn: Validation passed, setting Loading state
DEBUG SignIn: Calling PocketBaseApi.signInWithEmail()
DEBUG SignIn: Request email: 'test@example.com', password length: 16
DEBUG SignIn: Got result from API
DEBUG API SignIn: signInWithEmail called with identity: test@example.com
DEBUG API SignIn: Request body: {identity=test@example.com, password=YourPassword123}
DEBUG API SignIn: Response status: 200 OK, body: {...}
```

### Step 4: Identify the Error

#### If you see validation error before API call:

```
DEBUG SignIn: Validation failed - Email không hợp lệ
DEBUG SignIn: Email pattern test: 'invalid-email' matches pattern = false
```

**Action**: Use a valid email format like `user@example.com`

#### If you see 400 error from API:

```
DEBUG API SignIn: Response status: 400 Bad Request, body: {...error details...}
```

**Possible Causes:**
- Email doesn't exist in database (account not created)
- Password is incorrect
- PocketBase validation rule issue

**Check:**
1. Make sure account was created via Signup
2. Use exact same email and password
3. Check PocketBase admin panel to verify account exists

#### If you see 401 error:

```
DEBUG API SignIn: Response status: 401 Unauthorized
```

**Action**: Password is incorrect. Try again with correct password.

#### If you see different error:

Look at the response body in logs for more details:

```
DEBUG API SignIn: Response status: 500 Internal Server Error, body: {...}
```

**Action**: Check PocketBase server logs.

## Common Issues and Solutions

### Issue 1: "Email không hợp lệ" (Invalid Email)

**Problem**: Email validation failed before API call

**Solution**:
- Use format: `something@domain.com`
- Check for spaces: `test@example.com` ✓ vs `test @example.com` ✗
- Check for typos: Missing `@` or domain extension

### Issue 2: Account Created but Can't Sign In

**Problem**: Signed up successfully, but SignIn returns 400

**Solution**:
1. Check PocketBase admin panel (`http://103.159.51.215:8090/_/`)
2. Go to Collections → accounts
3. Verify the account record exists with correct email
4. Make sure collection permissions allow auth-with-password

### Issue 3: Wrong Password

**Problem**: Account exists but password is wrong

**Check**:
1. Same email used for signup and signin? ✓
2. Same password used? (case-sensitive!) ✓
3. No extra spaces in password field?

### Issue 4: Server Connection Error

**Problem**: Can't reach PocketBase server

**Check**:
1. PocketBase server is running: `http://103.159.51.215:8090`
2. Network connectivity to server
3. Check firewall/network settings

## Expected Success Response

When successful, logs should show:

```
DEBUG API SignIn: Response status: 200 OK, body: {
  "record": {
    "id": "...",
    "email": "test@example.com",
    "fullName": "Test User",
    ...
  },
  "token": "eyJhbGc..."
}
DEBUG API SignIn: Sign in successful! Token: eyJhbGc...
DEBUG SignIn: Sign in successful! User ID: xxx
```

## Detailed Testing Checklist

### Before Testing SignIn:

- [ ] Signed up successfully with email `test@example.com` and password `TestPass123`
- [ ] Saw "Đăng ký thành công" (Success) message
- [ ] App cleared the form automatically
- [ ] PocketBase admin shows the account exists

### When Testing SignIn:

- [ ] Entered exactly the same email: `test@example.com`
- [ ] Entered exactly the same password: `TestPass123`
- [ ] Clicked "Đăng nhập" button
- [ ] Watched the logcat output
- [ ] Noted the exact error message/response

### Debug Logs to Provide:

Please provide the full logcat output starting from:
```
DEBUG SignIn: signInWithEmail() called
```

Until:
```
DEBUG API SignIn: Error - ... (if failed)
// or
DEBUG SignIn: Sign in successful (if successful)
```

## Example Debug Log Session

### Successful SignIn:

```
DEBUG SignIn: signInWithEmail() called
DEBUG SignIn: Email: 'test@example.com' (trimmed: 'test@example.com')
DEBUG SignIn: Password length: 12
DEBUG SignIn: Validation passed, setting Loading state
DEBUG SignIn: Calling PocketBaseApi.signInWithEmail()
DEBUG SignIn: Request email: 'test@example.com', password length: 12
DEBUG SignIn: Got result from API
DEBUG API SignIn: signInWithEmail called with identity: test@example.com
DEBUG API SignIn: Request body: {identity=test@example.com, password=TestPass123}
DEBUG API SignIn: Response status: 200 OK, body: {"record":{"id":"xxx","email":"test@example.com",...},"token":"eyJ..."}
DEBUG API SignIn: Sign in successful! Token: eyJh...
DEBUG SignIn: Sign in successful! User ID: xxx
```

### Failed SignIn (Account Doesn't Exist):

```
DEBUG SignIn: signInWithEmail() called
DEBUG SignIn: Email: 'unknown@example.com' (trimmed: 'unknown@example.com')
DEBUG SignIn: Password length: 12
DEBUG SignIn: Validation passed, setting Loading state
DEBUG SignIn: Calling PocketBaseApi.signInWithEmail()
DEBUG SignIn: Request email: 'unknown@example.com', password length: 12
DEBUG SignIn: Got result from API
DEBUG API SignIn: signInWithEmail called with identity: unknown@example.com
DEBUG API SignIn: Request body: {identity=unknown@example.com, password=TestPass123}
DEBUG API SignIn: Response status: 400 Bad Request, body: {"code":400,"message":"Failed authentication attempt","data":{...}}
DEBUG API SignIn: Error - Sign in failed: 400 Bad Request - {...}
DEBUG SignIn: Sign in failed: Sign in failed: 400 Bad Request - {...}
```

## Next Steps

1. **Build and run the app** with the new improvements
2. **Test SignIn** with valid credentials
3. **Provide the full logcat output** showing the exact error
4. Based on the logs, we'll identify the exact issue and fix it

## Files Modified

- `AuthViewModel.kt`:
  - Added email format validation using regex pattern
  - Added password trimming in `signInWithEmail()`
  - Enhanced error messages for better debugging
  - Improved debug logging with better prefixes

- `PocketBaseApi.kt`:
  - Already has comprehensive debug logging for API calls

All changes are focused on catching issues early and providing better error messages/logs for debugging.
