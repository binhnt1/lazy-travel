# iOS Setup Guide - LazyTravel

Hướng dẫn chi tiết để chạy iOS app trên Xcode (Mac).

## Yêu cầu

- ✅ **macOS** với Xcode 15+ installed
- ✅ **Kotlin Multiplatform Mobile plugin** (KMM plugin) for Xcode
- ✅ **CocoaPods** installed (nếu dùng)

## Bước 1: Build Shared Framework

Trước khi mở Xcode, bạn **phải build shared framework** cho iOS:

### Option A: Build từ Terminal (Recommended)

```bash
# Từ project root directory
./gradlew :shared:assembleSharedDebugXCFramework
```

Hoặc build cho Release:

```bash
./gradlew :shared:assembleSharedReleaseXCFramework
```

### Option B: Build từ Android Studio

1. Mở project trong Android Studio
2. Chọn Gradle tab bên phải
3. Tìm: `lazy-travel > shared > Tasks > kotlin multiplatform`
4. Run: `assembleSharedDebugXCFramework`

## Bước 2: Mở Project trong Xcode

```bash
cd iosApp
open iosApp.xcodeproj
```

Hoặc double-click file `iosApp.xcodeproj` trong Finder.

## Bước 3: Add Framework to Xcode Project

**QUAN TRỌNG:** Bạn cần add shared framework vào Xcode project lần đầu tiên.

1. **Project Navigator** (⌘ + 1) → Click **iosApp** (root project)
2. Select target **iosApp**
3. Tab **General**
4. Scroll xuống **"Frameworks, Libraries, and Embedded Content"**
5. Click nút **+**
6. Click **"Add Other..."** → **"Add Files..."**
7. Navigate đến: `shared/build/XCFrameworks/debug/shared.xcframework`
8. Click **Open**
9. Đảm bảo **"Embed & Sign"** được chọn trong dropdown

### Add Framework Search Path

1. Tab **Build Settings**
2. Search: `Framework Search Paths`
3. Double-click **Framework Search Paths** row
4. Click **+**
5. Add: `$(PROJECT_DIR)/../shared/build/XCFrameworks/debug`
6. Select **recursive** nếu có option

## Bước 4: Configure Xcode Settings

### 4.1. Chọn Team/Signing

1. Click vào **iosApp** project trong Project Navigator
2. Chọn tab **Signing & Capabilities**
3. Chọn **Team** của bạn (Apple Developer account)
4. Nếu chưa có Team:
   - Click **Add Account...**
   - Đăng nhập bằng Apple ID
   - Hoặc chọn **Personal Team** (free)

### 4.2. Chọn Device/Simulator

- Ở thanh toolbar phía trên, chọn device hoặc simulator
- Recommended: Chọn **iPhone 15 Pro** simulator để test

### 4.3. Build Settings (Important!)

Verify các settings sau:

1. **Build Settings** tab:
   - **Deployment Target**: iOS 15.0 trở lên
   - **Swift Language Version**: Swift 5.x

2. **Build Phases**:
   - Phải có phase: "Compile Kotlin Framework"
   - Nếu không có → see troubleshooting below

## Bước 5: Build và Run

1. Press **⌘ + B** để build
2. Press **⌘ + R** để run

Hoặc click nút ▶️ (Play) ở toolbar.

## Bước 6: Verify App Works

Khi app chạy thành công, bạn sẽ thấy:

- ✅ Header: "Xin chào, Minh! 👋"
- ✅ Subtitle: "Sẵn sàng cho chuyến phiêu lưu tiếp theo?"
- ✅ Message: "🎉 Compose is working on iOS!"
- ✅ Build info: Kotlin 2.2.21, Compose 1.9.3

## Troubleshooting

### Lỗi: "Module 'shared' not found"

**Nguyên nhân:** Shared framework chưa được build.

**Giải pháp:**
```bash
# Build framework
./gradlew :shared:assembleSharedDebugXCFramework

# Sau đó clean build trong Xcode
# Product > Clean Build Folder (⌘ + Shift + K)
# Product > Build (⌘ + B)
```

### Lỗi: "No such module 'shared'"

**Nguyên nhân:** Framework path không đúng.

**Giải pháp:**
1. Xcode → Project Settings → Build Settings
2. Search: "Framework Search Paths"
3. Verify có path: `$(SRCROOT)/../shared/build/XCFrameworks/debug`

### Lỗi: Build Phase "Compile Kotlin Framework" not found

**Giải pháp:**
1. Project Settings → Build Phases
2. Click **+** → New Run Script Phase
3. Add script:
```bash
cd "$SRCROOT/.."
./gradlew :shared:embedAndSignAppleFrameworkForXcode
```
4. Name it: "Compile Kotlin Framework"
5. Move it to **before** "Compile Sources"

### Lỗi: Code signing failed

**Giải pháp:**
1. Project Settings → Signing & Capabilities
2. Change Bundle Identifier thành unique (vd: `com.yourname.lazytravel`)
3. Chọn Team của bạn

### Build thành công nhưng app crash ngay khi start

**Nguyên nhân:** Có thể dependencies missing hoặc init code fail.

**Giải pháp:**
1. Check Console log trong Xcode (⌘ + Shift + C)
2. Tìm crash message/stack trace
3. Verify rằng ContentView đang dùng simple test UI (không call PocketBase)

### Simulator không boot

**Giải pháp:**
```bash
# Reset simulator
xcrun simctl shutdown all
xcrun simctl erase all

# Restart Xcode
```

## Workflow Thông Thường

Khi develop iOS app:

### 1. Sau mỗi lần thay đổi Shared code (Kotlin):

```bash
# Build framework mới
./gradlew :shared:assembleSharedDebugXCFramework

# Sau đó trong Xcode:
# Product > Clean Build Folder (⌘ + Shift + K)
# Product > Build (⌘ + B)
```

### 2. Chỉ thay đổi iOS code (Swift):

- Chỉ cần Build/Run trong Xcode bình thường
- Không cần rebuild framework

## Build Script Automation (Advanced)

Để tự động build framework mỗi khi build iOS:

1. Project Settings → Build Phases
2. "Compile Kotlin Framework" script → Edit
3. Update script:

```bash
cd "$SRCROOT/.."
./gradlew :shared:embedAndSignAppleFrameworkForXcode \
    -Pkotlin.native.cocoapods.generate.wrapper=true
```

4. Tick: "For install builds only" (để không build khi archive)

## Xcode Shortcuts Hữu Ích

- **⌘ + B**: Build
- **⌘ + R**: Run
- **⌘ + .**: Stop running
- **⌘ + Shift + K**: Clean Build Folder
- **⌘ + Shift + O**: Quick Open (find files)
- **⌘ + Shift + C**: Open Console (view logs)
- **⌘ + K**: Clear Console

## Next Steps

Sau khi test screen chạy thành công:

1. ✅ Verify shared module accessible
2. ✅ Add navigation (SwiftUI NavigationView)
3. ✅ Integrate HeaderBar component từ shared
4. ✅ Add more screens
5. ✅ Connect PocketBase API

## Resources

- [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- [KMM for iOS](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html)
- [Xcode Help](https://developer.apple.com/documentation/xcode)

## Support

Nếu gặp lỗi không có trong troubleshooting, check:

1. **Console logs** trong Xcode
2. **Gradle build logs** trong terminal
3. Verify Kotlin/Gradle versions match

Happy coding! 🚀
