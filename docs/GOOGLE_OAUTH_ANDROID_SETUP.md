# Google OAuth Setup for Android - Proper Domain-Based Redirect

## Problem
Google OAuth requires redirect URIs to be proper domains (like `example.com/path`), not custom schemes (like `lazytravel://oauth`).

## Solutions (Choose One)

### Solution 1: Using Android App Links (Recommended) ✅

This is the official Android way to handle OAuth redirects.

#### Step 1: Create a Domain

You need a domain you control. For testing, you can use:
- A real domain you own (e.g., `lazytravel.com`)
- Or use `lazytravel.example.com` (this won't work for live, only local testing)

#### Step 2: Configure Google OAuth

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create Android OAuth 2.0 Client ID
3. For "Package name": `com.lazytravel.android`
4. Get your app's SHA-256 fingerprint:
   ```bash
   # With your keystore
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

   # Or if using release keystore
   keytool -list -v -keystore /path/to/release.keystore
   ```

5. Add to Google Console:
   - Package name: `com.lazytravel.android`
   - SHA-256: (your fingerprint)
   - Application ID: (optional)

6. In the OAuth consent screen, add redirect URI:
   ```
   https://yourdomain.com/oauth-callback
   ```

#### Step 3: Update AndroidManifest.xml

Replace the current deep link intent filter with an App Link:

```xml
<!-- Remove the old custom scheme intent filter -->
<!-- DELETE THIS:
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="lazytravel" android:host="oauth" />
</intent-filter>
-->

<!-- Add this instead for App Links -->
<intent-filter android:autoVerify="true">
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />

    <!-- Use your actual domain -->
    <data
        android:scheme="https"
        android:host="yourdomain.com"
        android:path="/oauth-callback" />
</intent-filter>
```

#### Step 4: Create assetlinks.json

Create a file at `https://yourdomain.com/.well-known/assetlinks.json`:

```json
[
  {
    "relation": ["delegate_permission/common.handle_all_urls"],
    "target": {
      "namespace": "android_app",
      "package_name": "com.lazytravel.android",
      "sha256_cert_fingerprints": ["YOUR:SHA256:FINGERPRINT:HERE"]
    }
  }
]
```

This file proves you control the domain AND the app.

#### Step 5: Configure PocketBase

1. Open PocketBase admin: `http://localhost:8090/_/`
2. Go to **Settings → Auth providers → Google**
3. Enter:
   - Client ID: (from Google Cloud Console)
   - Client Secret: (from Google Cloud Console)
   - Redirect URL: `https://yourdomain.com/oauth-callback`
4. Save

#### Step 6: Update Code

In `AuthViewModel.kt`:

```kotlin
val redirectUrl = "https://yourdomain.com/oauth-callback"

val result = PocketBaseApi.authWithOAuth2Code(
    provider = provider.name,
    code = code,
    codeVerifier = provider.codeVerifier,
    redirectUrl = redirectUrl
)
```

---

### Solution 2: Using Localhost for Development 🔧

For local development/testing without a domain:

#### Step 1: Update PocketBase Config

Edit your PocketBase config or set environment:
```bash
pb_auth_clientid=YOUR_GOOGLE_CLIENT_ID
pb_auth_clientsecret=YOUR_GOOGLE_SECRET
```

#### Step 2: Configure Google OAuth

In Google Cloud Console:
1. Add redirect URI: `http://localhost:8090/oauth-redirect`
2. Allow HTTP (only for development!)

#### Step 3: Update AndroidManifest.xml

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />

    <!-- For localhost development -->
    <data android:scheme="http" android:host="localhost" android:port="8090" />
</intent-filter>
```

#### Step 4: Update Code

```kotlin
// For development
val redirectUrl = "http://localhost:8090/oauth-redirect"

// For production
val redirectUrl = "https://yourdomain.com/oauth-callback"
```

---

### Solution 3: Using Google Sign-In SDK (Official Way) 🏆

This is Google's recommended approach for native apps.

#### Step 1: Add Dependency

In `shared/build.gradle.kts`:
```kotlin
commonMain.dependencies {
    // Google Sign-In (if available for KMM)
    // This might require Android-specific implementation
}
```

#### Step 2: Implement Sign-In Handler

Instead of the current OAuth flow, use:
```kotlin
// Simplified version
val googleClient = GoogleSignInClient(...)
val result = googleClient.signIn()
val idToken = result.idToken

// Then send to PocketBase
val pbResult = PocketBaseApi.authWithGoogle(idToken)
```

---

## Quick Testing Checklist

- [ ] Domain configured in Google Cloud Console
- [ ] `assetlinks.json` is publicly accessible at `https://yourdomain.com/.well-known/assetlinks.json`
- [ ] SHA-256 fingerprint matches in `assetlinks.json`
- [ ] Redirect URI matches in Google, PocketBase, and code
- [ ] AndroidManifest.xml has correct intent filter
- [ ] For App Links: `android:autoVerify="true"` is present
- [ ] Run: `adb shell pm get-app-links com.lazytravel.android` to verify App Link

## Debugging App Links

```bash
# Verify App Link state
adb shell pm get-app-links com.lazytravel.android

# Check verification status
adb shell pm verify-app-links --re-verify com.lazytravel.android

# View logs
adb logcat | grep -i "applinks"
```

## For Development Only

If you want to test WITHOUT a proper domain or assetlinks.json:

```kotlin
// Use custom scheme only for development
val redirectUrl = if (BuildConfig.DEBUG) {
    "lazytravel://oauth"  // Development
} else {
    "https://yourdomain.com/oauth-callback"  // Production
}
```

But note: This won't work with Google OAuth in production. Google requires verified domains.

## Important Notes

⚠️ **Custom schemes (lazytravel://) cannot be used directly with Google OAuth**
- Google requires domain-based redirect URIs
- Use App Links (official Android way) or a real domain
- Custom schemes work with PocketBase's OAuth implementation, but Google's OAuth provider doesn't accept them directly

✅ **App Links vs Deep Links**
- Deep Links: Can be intercepted by any app (less secure)
- App Links: Android verifies domain ownership (secure, requires assetlinks.json)
- For OAuth: Always use App Links

## References

- [Android App Links](https://developer.android.com/training/app-links)
- [Google OAuth 2.0 for Android](https://developers.google.com/identity/protocols/oauth2/native-app)
- [PocketBase OAuth Documentation](https://pocketbase.io/docs/authentication/oauth2/)
