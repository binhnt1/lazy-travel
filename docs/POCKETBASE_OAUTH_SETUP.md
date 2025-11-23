# PocketBase OAuth Configuration Guide

## Error: "400: invalid_request"

This error occurs when:
- PocketBase OAuth provider is not configured
- The redirect URI doesn't match what's registered in Google/Facebook/Apple
- Client ID or Client Secret is incorrect

## Step-by-Step Setup

### 1. Get Google OAuth Credentials

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable "Google+ API"
4. Go to "Credentials" → Create OAuth 2.0 Client ID
5. Choose "Android" as Application type
6. Add your package name: `com.lazytravel.android`
7. Get your **Client ID** and **Client Secret**

### 2. Configure Redirect URI in Google OAuth

In Google Cloud Console → OAuth consent screen:
1. Add "Authorized redirect URIs":
   ```
   lazytravel://oauth
   ```

**IMPORTANT**: The redirect URI MUST match exactly what's in your AndroidManifest.xml:
```xml
<data android:scheme="lazytravel" android:host="oauth" />
```

### 3. Configure PocketBase

#### Access PocketBase Admin Panel

1. Open browser: `http://localhost:8090/_/` (or your PocketBase URL)
2. Login with admin credentials
3. Go to **Settings → Auth providers**

#### Add Google OAuth Provider

1. Click "Google"
2. Fill in:
   - **Client ID**: (from Google Cloud Console)
   - **Client Secret**: (from Google Cloud Console)
   - **Redirect URL**: `lazytravel://oauth`

3. Click "Save"

#### Verify Configuration

1. Go to Collections → accounts → API Preview
2. Try this request:
   ```bash
   curl http://localhost:8090/api/collections/accounts/auth-methods
   ```

3. You should see:
   ```json
   {
     "emailPassword": true,
     "authProviders": [
       {
         "name": "google",
         "authUrl": "https://accounts.google.com/o/oauth2/auth?...",
         "state": "...",
         "codeVerifier": "...",
         ...
       }
     ]
   }
   ```

### 4. Test the Flow

1. Run the app
2. Go to Sign In → Click "Tiếp tục với Google"
3. You should be redirected to Google login
4. After login, should redirect back to app with code
5. App exchanges code for token and logs in

## Common Issues & Solutions

### Issue: "invalid_request" Error (400)

**Check:**
- ✅ Client ID is correct in PocketBase
- ✅ Client Secret is correct in PocketBase
- ✅ Redirect URI is exactly `lazytravel://oauth` in both:
  - Google OAuth Console
  - PocketBase settings
  - AndroidManifest.xml

### Issue: "redirect_uri_mismatch" Error

**Solution:**
- The redirect URI must match EXACTLY in all three places
- No trailing slashes or extra parameters
- Case-sensitive

### Issue: App doesn't receive the redirect

**Check:**
- ✅ AndroidManifest.xml has the deep link intent filter:
  ```xml
  <intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="lazytravel" android:host="oauth" />
  </intent-filter>
  ```
- ✅ MainActivity.kt has `handleDeepLink()` method
- ✅ OAuthCallbackRegistry is called in handleDeepLink

### Issue: "provider not configured" Error

**Solution:**
- Make sure you clicked "Save" after entering credentials in PocketBase
- Wait a few seconds for changes to take effect
- Restart the PocketBase server if needed
- Check the browser console for error messages

## Debugging

### View Auth URL

To see what auth URL PocketBase is generating:

```kotlin
// In SignInScreen or SignUpScreen
LaunchedEffect(oauthAuthUrl) {
    oauthAuthUrl?.let { url ->
        Log.d("OAuth", "Auth URL: $url")
        // Open in browser...
    }
}
```

### View Server Logs

Check PocketBase logs:
```bash
# If running locally
tail -f ~/.pocketbase/logs/*.log
```

Look for messages like:
```
[auth] Google provider configured successfully
[error] Invalid redirect_uri: ...
```

## Architecture Overview

```
┌─────────────────────────────────────┐
│         Mobile App (Android)        │
│   - SignInScreen/SignUpScreen       │
│   - AuthViewModel                   │
│   - OAuthBrowserLauncher           │
└────────────┬────────────────────────┘
             │
    1. initiateOAuth("google")
             │
             ▼
┌─────────────────────────────────────┐
│      PocketBase Server              │
│   /api/collections/accounts/       │
│     auth-methods                   │
└────────────┬────────────────────────┘
             │
    Returns: authUrl, codeVerifier
             │
             ▼
┌─────────────────────────────────────┐
│      Google OAuth Server            │
│   https://accounts.google.com/...  │
└────────────┬────────────────────────┘
             │
    User logs in
             │
             ▼
┌─────────────────────────────────────┐
│    Android Deep Link Handler        │
│   lazytravel://oauth?code=...      │
│   (Captured by MainActivity)        │
└────────────┬────────────────────────┘
             │
    Extract code from URL
             │
             ▼
┌─────────────────────────────────────┐
│      Mobile App (Android)           │
│   - completeOAuth(code)             │
└────────────┬────────────────────────┘
             │
    3. authWithOAuth2Code(code)
             │
             ▼
┌─────────────────────────────────────┐
│      PocketBase Server              │
│   /api/collections/accounts/       │
│     auth-with-oauth2               │
└────────────┬────────────────────────┘
             │
    Exchange code for token
             │
             ▼
┌─────────────────────────────────────┐
│         Mobile App                  │
│   - Logged in! ✅                   │
│   - Navigate to Home                │
└─────────────────────────────────────┘
```

## Security Notes

1. **PKCE (Proof Key for Code Exchange)**:
   - PocketBase automatically generates and validates PKCE
   - Protects against authorization code interception

2. **State Parameter**:
   - PocketBase automatically includes state parameter
   - Prevents CSRF attacks

3. **Deep Link Validation**:
   - Verify `state` parameter matches if needed
   - Check redirect URL origin in production

## References

- [PocketBase OAuth Documentation](https://pocketbase.io/docs/authentication/oauth2/)
- [Google OAuth 2.0 Setup](https://developers.google.com/identity/protocols/oauth2/native-app)
- [Android Deep Linking](https://developer.android.com/training/app-links/deep-linking)
