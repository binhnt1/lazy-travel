# iOS Debug Steps - Xác định nguyên nhân crash

## Vấn đề hiện tại
App crash với error: `kfun:kotlin#error(kotlin.Any){}kotlin.Nothing`

## Các bước debug:

### Step 1: Test framework cơ bản (KHÔNG dùng Compose)

1. **Mở file `iosApp/iosApp/iOSApp.swift`**

2. **Tạm thời thay đổi ContentView**:
   ```swift
   // Thay dòng này:
   ContentView()

   // Bằng dòng này:
   ContentView_Test()
   ```

3. **Full code `iOSApp.swift` sau khi sửa**:
   ```swift
   import SwiftUI

   @main
   struct iOSApp: App {
       var body: some Scene {
           WindowGroup {
               ContentView_Test()  // Changed from ContentView()
           }
       }
   }
   ```

4. **Rebuild và chạy**:
   - Clean: `Product → Clean Build Folder` (⇧⌘K)
   - Build: `Product → Build` (⌘B)
   - Run: `Product → Run` (⌘R)

5. **Kết quả mong đợi**:
   - Nếu KHÔNG crash → Framework OK, vấn đề nằm ở Compose
   - Nếu VẪN crash → Framework có vấn đề, cần xem thêm

---

### Step 2: Nếu Step 1 OK, test Compose đơn giản

1. Nếu `ContentView_Test` chạy OK
2. Quay lại dùng `ContentView()` trong iOSApp.swift
3. Minimal Compose UI sẽ được test

---

### Step 3: Thu thập thông tin để debug

Nếu vẫn crash, cần thông tin sau:

#### A. Full error message
1. Trong Xcode, mở Console: `View → Debug Area → Activate Console` (⇧⌘C)
2. Copy TOÀN BỘ text từ console khi crash
3. Paste vào file hoặc gửi cho tôi

#### B. Build warnings
1. Mở Report Navigator: `View → Navigators → Show Report Navigator` (⌘9)
2. Click vào build log mới nhất
3. Tìm các warning về "shared", "framework", "Compose"
4. Copy và gửi cho tôi

#### C. Framework info
Chạy trong Terminal:
```bash
cd /path/to/lazy-travel
ls -la shared/build/XCFrameworks/debug/shared.xcframework/
file shared/build/XCFrameworks/debug/shared.xcframework/ios-arm64/shared.framework/shared
```

Gửi kết quả cho tôi.

---

## Các lỗi thường gặp và cách fix:

### Lỗi 1: Framework không được rebuild
```bash
# Clean và rebuild
./gradlew clean
./gradlew :shared:assembleSharedDebugXCFramework
cd iosApp && pod install && cd ..
```

### Lỗi 2: Xcode cache cũ
```bash
# Clean Xcode cache
rm -rf ~/Library/Developer/Xcode/DerivedData/*
```

Sau đó trong Xcode:
- `Product → Clean Build Folder` (⇧⌘K)
- Quit Xcode và mở lại

### Lỗi 3: CocoaPods cache cũ
```bash
cd iosApp
pod deintegrate
rm -rf Pods iosApp.xcworkspace Podfile.lock
pod install
cd ..
```

---

## Thông tin cần gửi cho debug:

1. ✅ Xcode version (Xcode → About Xcode)
2. ✅ macOS version
3. ✅ Kết quả Step 1 (ContentView_Test có chạy không?)
4. ✅ Full console error message nếu crash
5. ✅ Build warnings nếu có

Với thông tin này tôi sẽ tìm ra chính xác vấn đề!
