# Development Workflow - LazyTravel KMM

Hướng dẫn workflow để code và xem kết quả ngay lập tức.

---

## 🎯 Quick Start

### Chạy iOS App

```bash
cd iosApp
open iosApp.xcodeproj
```

**Trong Xcode:**
1. Chọn simulator: **iPhone 15 Pro**
2. Nhấn **⌘ + R** để run
3. App sẽ mở với UI đầy đủ

### Chạy Android App

```bash
# Option 1: Android Studio
# - Open Android Studio
# - Open folder: androidApp
# - Click Run (▶️)

# Option 2: Command line
./gradlew :androidApp:installDebug
```

---

## 🔄 Development Workflows

### Workflow 1: Thay Đổi iOS UI (Native SwiftUI)

**Khi:** Bạn chỉnh sửa file `.swift` (UI components, screens)

**Bước:**
1. Edit file trong `iosApp/iosApp/Components/` hoặc `iosApp/iosApp/Screens/`
2. Trong Xcode, nhấn **⌘ + R** (Run)
3. Xem kết quả ngay lập tức

**Thời gian:** ~5-10 giây

**VD:** Sửa `HeaderBar.swift`, `PassportCard.swift`, `HomeView.swift`

---

### Workflow 2: Thay Đổi Shared Module (Business Logic)

**Khi:** Bạn chỉnh sửa code trong `shared/src/commonMain/kotlin/`

**Bước:**

```bash
# 1. Edit code trong shared/
# Example: shared/src/commonMain/kotlin/com/lazytravel/domain/model/User.kt

# 2. Rebuild framework
./rebuild-ios.sh

# 3. Trong Xcode:
# ⌘ + Shift + K (Clean Build Folder)
# ⌘ + R (Run)
```

**Thời gian:** ~30-60 giây (rebuild framework)

**VD:** Sửa models, repositories, use cases trong shared module

---

### Workflow 3: Thay Đổi Android UI (Compose)

**Khi:** Bạn chỉnh sửa file `.kt` trong `androidApp/`

**Bước:**
1. Edit file trong `androidApp/src/main/kotlin/`
2. Trong Android Studio, nhấn **▶️ Run** hoặc **⌘ + R**
3. Xem kết quả ngay

**Thời gian:** ~10-20 giây

**Lưu ý:** Nếu dùng Compose, có thể bật **Live Edit** để xem thay đổi real-time!

---

### Workflow 4: Thay Đổi Shared UI Components (Compose Multiplatform)

**Khi:** Bạn chỉnh sửa Compose components trong `shared/src/commonMain/kotlin/`

**For iOS:**
```bash
./rebuild-ios.sh
# Rồi run lại trong Xcode
```

**For Android:**
```bash
# Android Studio tự rebuild shared module
# Chỉ cần Run lại
```

**Thời gian:**
- iOS: ~30-60 giây (rebuild framework)
- Android: ~10-20 giây (auto rebuild)

---

## 🛠️ Development Scripts

### `rebuild-ios.sh` - Rebuild iOS Framework Nhanh

```bash
chmod +x rebuild-ios.sh
./rebuild-ios.sh
```

Dùng khi:
- Thay đổi code trong `shared/`
- Thêm/sửa models, repositories, APIs
- Update business logic

### `fix-gradle-wrapper.sh` - Fix Gradle Wrapper

```bash
chmod +x fix-gradle-wrapper.sh
./fix-gradle-wrapper.sh
```

Dùng khi:
- Lỗi "Could not find GradleWrapperMain"
- Clone project mới

---

## 📱 Platform-Specific Setup

### iOS Development

**IDE:** Xcode 15+

**Requirements:**
- ✅ Java 21 installed
- ✅ CocoaPods installed
- ✅ Xcode Command Line Tools
- ✅ Framework built: `./gradlew :shared:assembleSharedDebugXCFramework`

**Project Structure:**
```
iosApp/
├── iosApp.xcodeproj     # ← Mở file này
└── iosApp/
    ├── Components/      # UI components
    ├── Screens/         # Full screens
    ├── iOSApp.swift     # App entry
    └── ContentView.swift
```

**Tips:**
- Enable **Live Preview** trong Xcode để xem UI changes real-time
- Dùng **⌘ + Shift + K** để clean build folder khi framework thay đổi
- Check console log: **⌘ + Shift + Y**

---

### Android Development

**IDE:** Android Studio Koala+ (2024.1.1)

**Requirements:**
- ✅ JDK 21
- ✅ Android SDK 36
- ✅ Kotlin 2.2.21

**Project Structure:**
```
androidApp/
├── build.gradle.kts
└── src/main/kotlin/
    └── com/lazytravel/android/
        └── MainActivity.kt
```

**Tips:**
- Enable **Compose Live Edit**: Settings → Build, Execution, Deployment → Compose → Enable Live Edit
- Dùng **Logcat** để debug
- Hot reload: **⌘ + R** (Run) hoặc **Ctrl + Shift + F9** (Apply Changes)

---

## 🔌 Backend API Development

**PocketBase:** `http://103.159.51.215:8090`

### Test API Connection

```bash
curl http://103.159.51.215:8090/_/
```

Phải thấy PocketBase admin UI.

### Workflow Khi Thêm API Mới

**Bước 1:** Thêm API client trong `shared/`

```kotlin
// shared/src/commonMain/kotlin/com/lazytravel/data/remote/TripApi.kt
interface TripApi {
    suspend fun getTrips(): List<Trip>
}
```

**Bước 2:** Implement trong repository

```kotlin
// shared/src/commonMain/kotlin/com/lazytravel/data/repository/TripRepository.kt
class TripRepository(private val api: TripApi) {
    suspend fun fetchTrips(): Result<List<Trip>> {
        // Implementation
    }
}
```

**Bước 3:** Rebuild framework

```bash
./rebuild-ios.sh  # For iOS
# Android tự rebuild
```

**Bước 4:** Sử dụng trong UI

**iOS (SwiftUI):**
```swift
import shared

struct TripsView: View {
    // Use TripRepository from shared module
}
```

**Android (Compose):**
```kotlin
import com.lazytravel.data.repository.TripRepository

@Composable
fun TripsScreen() {
    // Use TripRepository
}
```

---

## 🐛 Common Issues & Solutions

### Issue 1: "No such module 'shared'" (iOS)

**Nguyên nhân:** Framework chưa build hoặc chưa được add vào Xcode.

**Fix:**
```bash
./gradlew :shared:assembleSharedDebugXCFramework
```

Rồi trong Xcode:
1. Project Navigator → iosApp
2. Target iosApp → General
3. Frameworks, Libraries, and Embedded Content
4. Add `shared.xcframework` nếu chưa có

---

### Issue 2: Code Thay Đổi Nhưng Không Thấy Kết Quả (iOS)

**Nguyên nhân:** Xcode cache hoặc framework chưa rebuild.

**Fix:**
```bash
# 1. Rebuild framework
./rebuild-ios.sh

# 2. Trong Xcode
⌘ + Shift + K  # Clean Build Folder
⌘ + R          # Run
```

---

### Issue 3: Gradle Build Chậm

**Nguyên nhân:** JVM memory thấp.

**Fix:** Đã config trong `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m
```

Nếu vẫn chậm, tăng thêm:
```properties
org.gradle.jvmargs=-Xmx6144m -XX:MaxMetaspaceSize=1536m
```

---

### Issue 4: Android Build Fails

**Nguyên nhân:** Shared module có lỗi.

**Fix:**
```bash
# Check shared module compile
./gradlew :shared:build

# Nếu có lỗi, fix rồi build lại Android
```

---

## 🚀 Performance Tips

### Faster iOS Builds

1. **Incremental builds:** Đừng dùng `./gradlew clean` trừ khi cần thiết
2. **Parallel builds:** Đã config trong `gradle.properties`
3. **Build cache:** Gradle daemon tự enable

### Faster Android Builds

1. **Live Edit:** Enable Compose Live Edit để thấy thay đổi ngay
2. **Apply Changes:** Dùng **Ctrl + Shift + F9** thay vì rebuild toàn bộ
3. **Gradle cache:** Sử dụng `--build-cache`

### Faster Framework Rebuilds

```bash
# Chỉ build iOS frameworks cần thiết
./gradlew :shared:linkDebugFrameworkIosArm64  # Real device
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64  # M1/M2 simulator
./gradlew :shared:linkDebugFrameworkIosX64  # Intel simulator
```

---

## 📋 Development Checklist

### Before Starting Development

- [ ] Java 21 installed (`java -version`)
- [ ] Xcode 15+ installed
- [ ] Android Studio installed
- [ ] CocoaPods installed (`pod --version`)
- [ ] Framework built (`./gradlew :shared:assembleSharedDebugXCFramework`)
- [ ] iOS project opens in Xcode
- [ ] Android project opens in Android Studio

### Daily Development

- [ ] Pull latest code: `git pull`
- [ ] Rebuild framework if shared code changed: `./rebuild-ios.sh`
- [ ] Run tests: `./gradlew test`
- [ ] Check PocketBase running: `curl http://103.159.51.215:8090/_/`

---

## 🎓 Learning Resources

### Kotlin Multiplatform
- [Official KMM Docs](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)

### SwiftUI
- [Apple SwiftUI Tutorials](https://developer.apple.com/tutorials/swiftui)
- [SwiftUI by Example](https://www.hackingwithswift.com/quick-start/swiftui)

### Jetpack Compose
- [Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Compose Pathway](https://developer.android.com/courses/pathways/compose)

---

## 💡 Best Practices

### Code Organization

**Shared Module:**
```
shared/src/commonMain/kotlin/
├── data/           # Data layer (repositories, APIs)
├── domain/         # Business logic (use cases, models)
└── ui/             # Shared UI components (Compose)
```

**iOS App:**
```
iosApp/iosApp/
├── Components/     # Reusable UI (Atoms, Molecules, Organisms)
├── Screens/        # Full screens
└── ViewModels/     # SwiftUI ViewModels
```

**Android App:**
```
androidApp/src/main/kotlin/
├── ui/             # Compose UI
├── viewmodels/     # Android ViewModels
└── di/             # Dependency Injection
```

### Git Workflow

```bash
# 1. Create feature branch
git checkout -b feature/my-feature

# 2. Make changes and test
./rebuild-ios.sh  # If changing shared

# 3. Commit
git add .
git commit -m "feat: Add new feature"

# 4. Push
git push origin feature/my-feature
```

---

## ✅ Summary

| Task | iOS | Android |
|------|-----|---------|
| **Edit UI** | Edit `.swift` → Run (⌘R) | Edit `.kt` → Run |
| **Edit Shared** | Edit `.kt` → `./rebuild-ios.sh` → Clean+Run | Edit `.kt` → Run |
| **Hot Reload** | Live Preview | Compose Live Edit |
| **Clean Build** | ⌘ + Shift + K | Build → Clean Project |
| **View Logs** | ⌘ + Shift + Y | Logcat |

---

Happy Coding! 🎉

Nếu gặp issue không có trong guide này, hãy tạo GitHub issue hoặc liên hệ team.
