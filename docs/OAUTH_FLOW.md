# PocketBase OAuth2 Integration Guide

## ❌ Cách KHÔNG đúng (hiện tại)

```kotlin
// WRONG: Passing empty string or idToken
viewModel.signInWithGoogle("")  // ❌ Sai!
```

## ✅ Cách đúng: PocketBase OAuth2 Flow

### Flow hoàn chỉnh:

```
User click button
    ↓
1. GET /api/collections/users/auth-methods
    → Nhận authUrl cho provider
    ↓
2. Mở authUrl trong browser/WebView
    → User đăng nhập tại Google/Facebook/Apple
    ↓
3. Provider redirect về app với 'code'
    → URL: myapp://oauth?code=ABC123&state=XYZ
    ↓
4. Extract code từ redirect URL
    ↓
5. POST /api/collections/users/auth-with-oauth2
    → Gửi code + provider name
    ↓
6. Nhận token + user data
    → Login thành công!
```

## Implementation Steps

### Step 1: Get OAuth Providers & AuthURL

```kotlin
suspend fun startGoogleLogin() {
    // Gọi API để lấy danh sách providers
    val result = PocketBaseApi.getOAuthProviders()

    result.fold(
        onSuccess = { response ->
            // Tìm Google provider
            val googleProvider = response.authProviders.find { it.name == "google" }

            if (googleProvider != null) {
                // Lưu lại codeVerifier (cần cho bước 4)
                savedCodeVerifier = googleProvider.codeVerifier

                // Mở browser với authUrl
                openBrowser(googleProvider.authUrl)
            }
        },
        onFailure = { error ->
            // Hiển thị lỗi
        }
    )
}
```

### Step 2: Open Browser (Platform-specific)

#### Android - CustomTabsIntent
```kotlin
// androidMain/kotlin/OAuth.android.kt
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun openBrowser(context: Context, url: String) {
    val intent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()

    intent.launchUrl(context, Uri.parse(url))
}
```

#### iOS - ASWebAuthenticationSession
```swift
// iosMain/swift/OAuth.swift
import AuthenticationServices

func openBrowser(url: String, completion: @escaping (String?) -> Void) {
    guard let authURL = URL(string: url) else { return }

    let session = ASWebAuthenticationSession(
        url: authURL,
        callbackURLScheme: "myapp",
        completionHandler: { callbackURL, error in
            if let url = callbackURL {
                // Extract code from URL
                let code = URLComponents(url: url, resolvingAgainstBaseURL: false)?
                    .queryItems?
                    .first(where: { $0.name == "code" })?
                    .value

                completion(code)
            }
        }
    )

    session.presentationContextProvider = // your view controller
    session.start()
}
```

### Step 3: Handle Redirect (Lấy code)

#### Android - Deep Link trong AndroidManifest.xml
```xml
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <!-- Redirect URL scheme -->
        <data android:scheme="myapp" android:host="oauth" />
    </intent-filter>
</activity>
```

#### Trong Activity:
```kotlin
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)

    intent?.data?.let { uri ->
        // Extract code từ redirect URL
        val code = uri.getQueryParameter("code")
        val state = uri.getQueryParameter("state")

        if (code != null) {
            // Gọi API để hoàn tất OAuth
            completeOAuth(code)
        }
    }
}
```

### Step 4: Complete OAuth với code

```kotlin
suspend fun completeOAuth(code: String) {
    val result = PocketBaseApi.authWithGoogle(
        code = code,
        codeVerifier = savedCodeVerifier,  // Từ step 1
        redirectUrl = "myapp://oauth"      // Phải match với manifest
    )

    result.fold(
        onSuccess = { authResponse ->
            // Login thành công!
            // authResponse.token đã được lưu tự động
            // authResponse.record chứa user data
            navigateToHome()
        },
        onFailure = { error ->
            showError(error.message)
        }
    )
}
```

## Các API Methods

### 1. Get OAuth Providers
```kotlin
suspend fun getOAuthProviders(): Result<OAuthProvidersResponse>
```

**Response:**
```json
{
  "emailPassword": true,
  "authProviders": [
    {
      "name": "google",
      "state": "random-state-string",
      "codeVerifier": "pkce-verifier",
      "codeChallenge": "pkce-challenge",
      "codeChallengeMethod": "S256",
      "authUrl": "https://accounts.google.com/o/oauth2/auth?client_id=..."
    },
    {
      "name": "facebook",
      "authUrl": "https://www.facebook.com/v12.0/dialog/oauth?client_id=..."
    },
    {
      "name": "apple",
      "authUrl": "https://appleid.apple.com/auth/authorize?client_id=..."
    }
  ]
}
```

### 2. Complete OAuth with Code
```kotlin
suspend fun authWithOAuth2Code(
    provider: String,        // "google", "facebook", "apple"
    code: String,            // Authorization code từ redirect
    codeVerifier: String = "",   // PKCE verifier (optional)
    redirectUrl: String = ""     // Redirect URL đã dùng
): Result<AuthResponse>
```

### 3. Helper Methods
```kotlin
// Tất cả đều gọi authWithOAuth2Code bên trong
suspend fun authWithGoogle(code: String, ...): Result<AuthResponse>
suspend fun authWithApple(code: String, ...): Result<AuthResponse>
suspend fun authWithFacebook(code: String, ...): Result<AuthResponse>
```

## Redirect URL Configuration

### Android
```xml
<!-- AndroidManifest.xml -->
<data android:scheme="myapp" android:host="oauth" />
```
→ Redirect URL: `myapp://oauth`

### iOS
```xml
<!-- Info.plist -->
<key>CFBundleURLTypes</key>
<array>
    <dict>
        <key>CFBundleURLSchemes</key>
        <array>
            <string>myapp</string>
        </array>
    </dict>
</array>
```
→ Redirect URL: `myapp://oauth`

### PocketBase Admin Panel
Trong Settings → Auth providers:
- Google: Thêm `myapp://oauth` vào Authorized redirect URIs
- Facebook: Thêm `myapp://oauth` vào Valid OAuth Redirect URIs
- Apple: Thêm `myapp://oauth` vào Return URLs

## Security Notes

1. **PKCE (Proof Key for Code Exchange)**:
   - PocketBase tự động tạo `codeVerifier` và `codeChallenge`
   - Bạn PHẢI lưu `codeVerifier` từ step 1 để dùng ở step 4
   - Điều này bảo vệ khỏi authorization code interception attacks

2. **State Parameter**:
   - Dùng để prevent CSRF attacks
   - PocketBase tự động handle

3. **Deep Link Security**:
   - Validate state parameter nếu cần
   - Check redirect URL origin

## Example: Complete Implementation

```kotlin
class AuthViewModel {
    private var oauthCodeVerifier: String = ""

    suspend fun initiateGoogleLogin(context: Context) {
        _authState.value = AuthState.Loading

        val result = PocketBaseApi.getOAuthProviders()
        result.fold(
            onSuccess = { response ->
                val googleProvider = response.authProviders
                    .find { it.name == "google" }

                if (googleProvider != null) {
                    // Lưu codeVerifier
                    oauthCodeVerifier = googleProvider.codeVerifier

                    // Mở browser
                    openOAuthBrowser(context, googleProvider.authUrl)
                } else {
                    _authState.value = AuthState.Error("Google OAuth not configured")
                }
            },
            onFailure = { error ->
                _authState.value = AuthState.Error(error.message ?: "Failed to get OAuth providers")
            }
        )
    }

    suspend fun handleOAuthCallback(code: String) {
        _authState.value = AuthState.Loading

        val result = PocketBaseApi.authWithGoogle(
            code = code,
            codeVerifier = oauthCodeVerifier,
            redirectUrl = "myapp://oauth"
        )

        result.fold(
            onSuccess = { authResponse ->
                _authState.value = AuthState.Success(authResponse.record)
                oauthCodeVerifier = "" // Clear
            },
            onFailure = { error ->
                _authState.value = AuthState.Error(error.message ?: "OAuth failed")
                oauthCodeVerifier = "" // Clear
            }
        )
    }
}
```

## Tóm tắt

**Không còn cần `idToken` nữa!**

PocketBase OAuth2 flow sử dụng:
1. ✅ `authUrl` - để mở browser
2. ✅ `code` - authorization code từ provider redirect
3. ✅ `codeVerifier` - PKCE security
4. ✅ `redirectUrl` - deep link callback

**Không dùng:**
- ❌ `idToken` (Firebase/Google Sign-In SDK style)
- ❌ `accessToken` (old OAuth 2.0 implicit flow)
