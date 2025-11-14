# Mac Build Guide - Complete Project Integration

Hướng dẫn build dự án hoàn chỉnh trên Mac, bao gồm shared module và PocketBase API.

## ⚠️ QUAN TRỌNG: Fix Java Version TRƯỚC

Gradle 8.13 **KHÔNG hỗ trợ Java 25**. Bạn cần Java 17 hoặc 21.

### Kiểm tra Java hiện tại:
```bash
java -version
```

Nếu thấy `25.0.1` → **PHẢI downgrade!**

### Cách 1: Dùng SDKMAN (RECOMMENDED)

```bash
# Kiểm tra SDKMAN đã cài chưa
sdk version

# Install Java 17 LTS (Temurin distribution)
sdk install java 17.0.13-tem

# Đặt làm mặc định
sdk default java 17.0.13-tem

# Kiểm tra lại
java -version
# Phải thấy: openjdk version "17.0.13"
```

### Cách 2: Dùng Homebrew

```bash
# Cài Java 17
brew install openjdk@17

# Tạo symlink
sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk \
  /Library/Java/JavaVirtualMachines/openjdk-17.jdk

# Thêm vào shell profile (~/.zshrc hoặc ~/.bash_profile)
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"

# Reload shell
source ~/.zshrc

# Kiểm tra
java -version
```

---

## 🔧 Step 1: Build Shared Framework

Sau khi fix Java version:

```bash
# Từ root project directory
cd /path/to/lazy-travel

# Build framework cho iOS
./gradlew :shared:assembleSharedDebugXCFramework

# Nếu lỗi "Gradle wrapper not found", chạy:
chmod +x fix-gradle-wrapper.sh
./fix-gradle-wrapper.sh

# Rồi build lại:
./gradlew :shared:assembleSharedDebugXCFramework
```

### Kết quả khi thành công:

```
BUILD SUCCESSFUL in 45s
```

Framework sẽ được tạo tại:
```
shared/build/XCFrameworks/debug/shared.xcframework/
```

---

## 📱 Step 2: Mở iOS Project trong Xcode

```bash
cd iosApp
open iosApp.xcodeproj
```

**QUAN TRỌNG:** Đừng dùng `open .` vì có thể mở nhầm workspace.

---

## 🔗 Step 3: Configure Xcode Project

### 3.1. Enable Shared Module Import

File đã được comment để tránh lỗi. Giờ uncomment:

**iosApp/iosApp/iOSApp.swift:**
```swift
import SwiftUI
import shared  // ✅ Uncomment dòng này

@main
struct iOSApp: App {
    // ...
}
```

**iosApp/iosApp/ContentView.swift:**
```swift
import SwiftUI
import shared  // ✅ Uncomment dòng này

struct ContentView: View {
    // ...
}
```

### 3.2. Add Framework to Xcode

1. **Project Navigator** (⌘+1)
2. Click project **iosApp** (root)
3. Select target **iosApp**
4. Tab **General**
5. Scroll xuống **Frameworks, Libraries, and Embedded Content**
6. Click **+** button
7. Click **Add Other** → **Add Files...**
8. Navigate đến: `shared/build/XCFrameworks/debug/shared.xcframework`
9. Click **Open**
10. Đảm bảo **Embed & Sign** được chọn

### 3.3. Add Framework Search Path

1. Tab **Build Settings**
2. Search: "Framework Search Paths"
3. Double-click **Framework Search Paths**
4. Click **+**
5. Add: `$(PROJECT_DIR)/../shared/build/XCFrameworks/debug`

---

## 🚀 Step 4: Run iOS App

1. **Select simulator:** iPhone 15 Pro
2. **Configure signing:**
   - Tab **Signing & Capabilities**
   - Select your **Team**
3. **Build and Run:** Press **⌘ + R**

### Nếu build thành công:

App sẽ hiển thị:
- ✅ Greeting header: "Xin chào, Minh!"
- ✅ PassportCard với level, XP, badges
- ✅ Trip cards (Nha Trang, Đà Lạt)
- ✅ Destination carousel
- ✅ Bottom navigation

---

## 🔌 Step 5: Integrate PocketBase API

### 5.1. Kiểm tra PocketBase Connection

PocketBase backend: `http://103.159.51.215:8090`

Test trong browser:
```
http://103.159.51.215:8090/_/
```

Phải thấy PocketBase admin UI.

### 5.2. Sử dụng Shared Module API

Shared module đã có các API clients. Ví dụ sử dụng:

**Example: Login User**

```swift
import shared
import SwiftUI

struct LoginView: View {
    @State private var email = ""
    @State private var password = ""

    func login() {
        // TODO: Use PocketBase API from shared module
        // Example: AuthRepository.login(email: email, password: password)
    }

    var body: some View {
        VStack {
            TextField("Email", text: $email)
            SecureField("Password", text: $password)
            Button("Login", action: login)
        }
    }
}
```

### 5.3. Check Available APIs

Sau khi import shared, bạn có thể access:
- `shared.data.repository.*` - Repositories
- `shared.domain.model.*` - Data models
- `shared.data.remote.*` - API clients

Xem code trong `shared/src/commonMain/kotlin/` để biết APIs available.

---

## 🐛 Troubleshooting

### Error: "No such module 'shared'"

**Nguyên nhân:** Framework chưa được build hoặc chưa được add vào Xcode.

**Fix:**
1. Build framework: `./gradlew :shared:assembleSharedDebugXCFramework`
2. Add framework vào Xcode (xem Step 3.2)
3. Clean build folder: **Product → Clean Build Folder** (⇧⌘K)
4. Rebuild: **⌘ + B**

### Error: "BUILD FAILED" khi chạy Gradle

**Nguyên nhân:** Java version không tương thích.

**Fix:**
1. Check Java: `java -version`
2. Nếu không phải 17 hoặc 21, downgrade (xem phần đầu guide)
3. Restart terminal
4. Build lại

### Error: "Could not find or load main class GradleWrapperMain"

**Nguyên nhân:** Gradle wrapper jar bị thiếu.

**Fix:**
```bash
chmod +x fix-gradle-wrapper.sh
./fix-gradle-wrapper.sh
```

### Error: Xcode build fails với lỗi "Framework not found"

**Nguyên nhân:** Framework search path chưa đúng.

**Fix:**
1. Xcode → Build Settings
2. Framework Search Paths
3. Add: `$(PROJECT_DIR)/../shared/build/XCFrameworks/debug`
4. Clean & rebuild

### App crashes khi gọi API

**Nguyên nhân:** PocketBase URL chưa được config đúng hoặc network không connect được.

**Fix:**
1. Check PocketBase có chạy không: `http://103.159.51.215:8090`
2. Check `Info.plist` có allow HTTP không:
   ```xml
   <key>NSAppTransportSecurity</key>
   <dict>
       <key>NSAllowsArbitraryLoads</key>
       <true/>
   </dict>
   ```
3. Check console logs trong Xcode

---

## 📋 Build Checklist

Checklist để verify mọi thứ hoạt động:

- [ ] Java 17 hoặc 21 installed (`java -version`)
- [ ] Gradle wrapper fixed (`./gradlew --version`)
- [ ] Shared framework built successfully
- [ ] Framework xuất hiện tại `shared/build/XCFrameworks/debug/shared.xcframework`
- [ ] Xcode project opened: `iosApp.xcodeproj`
- [ ] Framework added vào Xcode project
- [ ] Framework search path configured
- [ ] `import shared` uncommented trong iOSApp.swift và ContentView.swift
- [ ] Signing configured (Team selected)
- [ ] Build successful (⌘ + B)
- [ ] App runs trong simulator (⌘ + R)
- [ ] UI displays correctly
- [ ] PocketBase accessible (`http://103.159.51.215:8090`)
- [ ] API calls working (test login/logout)

---

## 🎯 Current Project Structure

```
lazy-travel/
├── androidApp/          # Android app (đã có test UI)
├── iosApp/
│   └── iosApp/
│       ├── Components/  # SwiftUI UI components
│       │   ├── Organisms/
│       │   │   ├── HeaderBar.swift       ✅ Complete
│       │   │   ├── PassportCard.swift    ✅ Complete
│       │   │   ├── TripCard.swift        ✅ Complete
│       │   │   └── DestinationCard.swift ✅ Complete
│       │   └── Screens/
│       │       └── HomeView.swift        ✅ Complete
│       ├── iOSApp.swift                  ✅ Updated
│       └── ContentView.swift             ✅ Updated
├── shared/              # Kotlin shared module
│   ├── src/commonMain/  # Shared business logic
│   └── build/
│       └── XCFrameworks/
│           └── debug/
│               └── shared.xcframework  ⏳ Needs building
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.properties   ✅ Configured
│       └── gradle-wrapper.jar          ✅ Fixed
├── fix-gradle-wrapper.sh               ✅ Script ready
├── IOS_DEV_GUIDE.md                    ✅ Quick UI guide
└── MAC_BUILD_GUIDE.md                  ✅ This file
```

---

## 🔄 Rebuild Workflow

Khi có thay đổi trong shared module:

```bash
# 1. Clean old build
./gradlew clean

# 2. Build new framework
./gradlew :shared:assembleSharedDebugXCFramework

# 3. Trong Xcode:
# Product → Clean Build Folder (⇧⌘K)
# Product → Build (⌘ + B)
```

---

## 📚 Next Steps

Sau khi setup xong:

1. **Implement API Integration**
   - Tạo ViewModel layer trong shared
   - Connect PocketBase APIs
   - Test authentication flow

2. **Add More UI Screens**
   - Login/Register screen
   - Trip detail screen
   - Profile screen
   - Explore screen

3. **Test End-to-End**
   - Create trip → API call → Update UI
   - Login → Store token → Fetch user data
   - Vote destination → Real-time updates

4. **Production Build**
   - Build release framework: `./gradlew :shared:assembleSharedReleaseXCFramework`
   - Archive iOS app: Product → Archive
   - Submit to App Store

---

## ✅ Success Criteria

Project hoàn chỉnh khi:

- ✅ iOS app build thành công
- ✅ Shared module integrated
- ✅ PocketBase API calls working
- ✅ UI hiển thị data từ backend
- ✅ Authentication flow hoạt động
- ✅ CRUD operations cho trips/destinations working

---

Happy Building! 🎉

Nếu gặp vấn đề, check lại từng step trong guide này.
