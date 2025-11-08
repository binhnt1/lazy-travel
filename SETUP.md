# Hướng dẫn Setup và Build Project

## ⚠️ Lưu ý quan trọng

Dự án này được tạo trong môi trường sandbox không có internet. Khi bạn clone về máy local, bạn cần làm theo các bước sau:

## 📋 Yêu cầu hệ thống

### Cho Android Development
- JDK 17 hoặc mới hơn (khuyến nghị JDK 17)
- Android Studio Hedgehog (2023.1.1) hoặc mới hơn
- Android SDK với:
  - Android SDK Platform 34
  - Android SDK Build-Tools 34.0.0
  - Android SDK Platform-Tools

### Cho iOS Development (chỉ trên macOS)
- macOS Monterey (12.0) hoặc mới hơn
- Xcode 14.0 hoặc mới hơn
- CocoaPods (cài đặt: `sudo gem install cocoapods`)

## 🚀 Setup Project lần đầu

### Bước 1: Clone repository

```bash
git clone <repository-url>
cd lazy-travel
```

### Bước 2: Kiểm tra JDK

```bash
java -version
# Nên hiển thị JDK 17 hoặc cao hơn
```

Nếu chưa có JDK 17:
- **macOS**: `brew install openjdk@17`
- **Windows**: Download từ [Adoptium](https://adoptium.net/)
- **Linux**: `sudo apt install openjdk-17-jdk`

### Bước 3: Set JAVA_HOME (nếu cần)

**macOS/Linux:**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
# Hoặc thêm vào ~/.zshrc hoặc ~/.bashrc
```

**Windows:**
```
setx JAVA_HOME "C:\Program Files\Java\jdk-17"
```

### Bước 4: Tạo Gradle Wrapper (nếu chưa có)

```bash
gradle wrapper --gradle-version 8.2
```

Nếu lỗi "gradle command not found", cài đặt Gradle:
- **macOS**: `brew install gradle`
- **Windows**: Download từ [gradle.org](https://gradle.org/releases/)
- **Linux**: `sudo apt install gradle`

## 📱 Build Android App

### Option 1: Sử dụng Android Studio (Khuyến nghị)

1. Mở Android Studio
2. Click "Open" và chọn thư mục dự án
3. Chờ Gradle sync hoàn tất
4. Chọn module `androidApp` trong dropdown
5. Click Run button (▶️) hoặc Shift + F10

### Option 2: Build từ Command Line

```bash
# Sync dependencies
./gradlew build

# Cài đặt debug APK lên thiết bị/emulator
./gradlew :androidApp:installDebug

# Build release APK
./gradlew :androidApp:assembleRelease
# APK sẽ ở: androidApp/build/outputs/apk/release/
```

## 🍎 Build iOS App

### Bước 1: Build Shared Framework

```bash
./gradlew :shared:linkDebugFrameworkIosArm64
# Hoặc cho simulator:
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

### Bước 2: Mở Xcode Project

```bash
open iosApp/iosApp.xcodeproj
```

### Bước 3: Configure Signing

1. Trong Xcode, chọn project "iosApp"
2. Chọn tab "Signing & Capabilities"
3. Chọn Team của bạn
4. Xcode sẽ tự động tạo provisioning profile

### Bước 4: Run

1. Chọn scheme "iosApp"
2. Chọn simulator hoặc thiết bị
3. Click Run (⌘R)

## 🔧 Troubleshooting

### Lỗi 1: "Plugin was not found"

**Giải pháp:**
```bash
# Xóa cache Gradle
rm -rf ~/.gradle/caches/

# Sync lại
./gradlew clean build --refresh-dependencies
```

### Lỗi 2: "Could not resolve dependencies"

**Giải pháp:**
1. Kiểm tra kết nối internet
2. Nếu đằng sau proxy, cấu hình trong `gradle.properties`:
```properties
systemProp.http.proxyHost=proxy.company.com
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=proxy.company.com
systemProp.https.proxyPort=8080
```

### Lỗi 3: "Unsupported class file major version"

**Nguyên nhân:** JDK version không tương thích

**Giải pháp:**
```bash
# Kiểm tra Java version
java -version

# Nên dùng JDK 17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

### Lỗi 4: Android SDK not found

**Giải pháp:**
Tạo file `local.properties` ở thư mục root:
```properties
sdk.dir=/Users/your-username/Library/Android/sdk
# Windows: sdk.dir=C\:\\Users\\your-username\\AppData\\Local\\Android\\Sdk
```

### Lỗi 5: iOS build fails - Framework not found

**Giải pháp:**
```bash
# Clean và rebuild shared framework
./gradlew :shared:clean
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64

# Trong Xcode, clean build folder
# Product > Clean Build Folder (Shift + Cmd + K)
```

## 🎯 Quick Start Commands

### Android
```bash
# Build và install
./gradlew :androidApp:installDebug

# Run tests
./gradlew :shared:test
./gradlew :androidApp:testDebugUnitTest

# Check code
./gradlew detekt  # Nếu có setup detekt
```

### iOS
```bash
# Build framework cho simulator
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64

# Build framework cho device
./gradlew :shared:linkDebugFrameworkIosArm64

# Run unit tests (trong shared module)
./gradlew :shared:iosSimulatorArm64Test
```

## 📦 Dependencies Cache

Gradle sẽ download dependencies lần đầu tiên. Chúng sẽ được cache tại:
- **macOS/Linux:** `~/.gradle/caches/`
- **Windows:** `C:\Users\<username>\.gradle\caches\`

## 🔄 Update Dependencies

Để update tất cả dependencies lên version mới nhất:

```bash
./gradlew dependencyUpdates  # Nếu có plugin
# Hoặc manually update version trong build.gradle.kts
```

## 📚 Useful Commands

```bash
# Xem tất cả tasks available
./gradlew tasks

# Xem dependency tree
./gradlew :shared:dependencies
./gradlew :androidApp:dependencies

# Clean toàn bộ project
./gradlew clean

# Build toàn bộ project
./gradlew build

# Run với offline mode (dùng cache)
./gradlew build --offline
```

## 🌐 Network Issues

Nếu bạn ở môi trường không có internet hoặc internet chậm:

1. **Dùng Maven Local:**
Thêm vào `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        mavenLocal()  // Tìm trong cache local trước
        google()
        mavenCentral()
    }
}
```

2. **Offline Mode:**
```bash
./gradlew build --offline
```

## ✅ Xác nhận Setup thành công

Chạy lệnh sau để kiểm tra:

```bash
# Kiểm tra Gradle
./gradlew --version

# Build shared module
./gradlew :shared:build

# Build Android
./gradlew :androidApp:assembleDebug

# iOS (trên macOS)
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

Nếu tất cả đều pass, bạn đã setup thành công! 🎉

## 📞 Hỗ trợ

Nếu gặp vấn đề:
1. Kiểm tra phần Troubleshooting ở trên
2. Xem logs chi tiết với: `./gradlew build --stacktrace --info`
3. Tìm kiếm lỗi trên [Stack Overflow](https://stackoverflow.com)
4. Xem tài liệu chính thức:
   - [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
   - [Gradle](https://docs.gradle.org)
   - [Android Studio](https://developer.android.com/studio)
