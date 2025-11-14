# iOS Development Guide - No Gradle Wrapper Needed

Hướng dẫn chạy iOS app **KHÔNG CẦN** build Gradle wrapper.

## ⚡ Option 1: Chạy Trực Tiếp (RECOMMENDED - Fastest!)

**Bạn KHÔNG CẦN build shared framework** để xem UI!

iOS UI components đã được tạo hoàn toàn bằng **SwiftUI native**, không phụ thuộc vào shared module.

### Cách làm:

1. **Mở Xcode:**
   ```bash
   cd iosApp
   open iosApp.xcodeproj
   ```

2. **Configure:**
   - Select **iPhone 15 Pro** simulator
   - Tab **Signing & Capabilities** → chọn Team

3. **Run:**
   - Press **⌘ + R**
   - Hoặc click nút ▶️

4. **Enjoy!** 🎉
   - App sẽ hiển thị full UI ngay lập tức
   - Không cần Gradle, không cần build framework

### Tại sao không cần build shared module?

UI components được tạo bằng **pure SwiftUI**:
- ✅ HeaderBar.swift
- ✅ PassportCard.swift
- ✅ TripCard.swift
- ✅ DestinationCard.swift
- ✅ HomeView.swift

Tất cả đều là **native iOS code**, không có dependency vào Kotlin shared module.

---

## 🔧 Option 2: Fix Gradle Wrapper (Nếu Muốn Build Shared Module)

Nếu sau này bạn muốn integrate với Kotlin shared module, fix Gradle wrapper:

### Method A: Auto Fix Script

```bash
# Run fix script
chmod +x fix-gradle-wrapper.sh
./fix-gradle-wrapper.sh

# Then build framework
./gradlew :shared:assembleSharedDebugXCFramework
```

### Method B: Manual Download

```bash
# Download wrapper jar
mkdir -p gradle/wrapper
curl -L -o gradle/wrapper/gradle-wrapper.jar \
  https://raw.githubusercontent.com/gradle/gradle/v8.13.0/gradle/wrapper/gradle-wrapper.jar

# Make executable
chmod +x gradlew

# Build framework
./gradlew :shared:assembleSharedDebugXCFramework
```

### Method C: Use Gradle Global (nếu đã install)

```bash
# Check if Gradle installed
gradle --version

# If yes, use it directly
gradle :shared:assembleSharedDebugXCFramework
```

---

## 🏗️ Option 3: Build từ Android Studio

Nếu bạn có Android Studio:

1. Open project trong Android Studio
2. Gradle tab (bên phải)
3. Navigate: `lazy-travel > shared > Tasks > kotlin multiplatform`
4. Double-click: `assembleSharedDebugXCFramework`

---

## 📱 Xem UI Ngay Bây Giờ

**Quick start guide:**

```bash
# 1. Navigate to iOS app
cd iosApp

# 2. Open in Xcode
open iosApp.xcodeproj

# 3. In Xcode:
#    - Select iPhone 15 Pro simulator
#    - Press ⌘ + R

# That's it! No Gradle needed! 🚀
```

---

## 🎨 Những Gì Bạn Sẽ Thấy

```
┌─────────────────────────────┐
│ Xin chào, Minh! 👋          │ ← Greeting Header
│ Sẵn sàng cho chuyến...      │
├─────────────────────────────┤
│ ╔═══════════════════════╗   │
│ ║ 🌟 Level 12           ║   │ ← PassportCard
│ ║ Minh Nguyen           ║   │
│ ║ XP: ▓▓▓▓▓░░░ 57%     ║   │
│ ║ 🗺️23 🌍8 📸342        ║   │
│ ║ Badges: 🏆🎒🗺️📸⛰️   ║   │
│ ╚═══════════════════════╝   │
├─────────────────────────────┤
│ Chuyến Đi Của Bạn          │
│ ┌────┐ ┌────┐              │ ← Trip Cards
│ │🔥  │ │⏰  │              │
│ │Nha │ │Đà  │              │
│ │Trang│ │Lạt │              │
│ └────┘ └────┘              │
├─────────────────────────────┤
│ Điểm Đến Phổ Biến          │
│ ┌────┐┌────┐┌────┐         │ ← Destinations
│ │Hạ  ││Hội ││Sapa│         │
│ │Long││An  ││    │         │
│ └────┘└────┘└────┘         │
├─────────────────────────────┤
│ Hành Động Nhanh            │
│ [➕] [🗳️] [👥] [📝]        │ ← Quick Actions
├─────────────────────────────┤
│ 🏠  🗺️  ➕  👥  👤        │ ← Bottom Nav
└─────────────────────────────┘
```

---

## ❓ FAQ

### Q: Tôi có cần build shared framework không?
**A:** Không! UI đã là SwiftUI native, chạy được ngay.

### Q: Khi nào cần build shared framework?
**A:** Chỉ khi muốn integrate:
- PocketBase API calls
- ViewModels từ Kotlin
- Business logic từ shared module

### Q: Làm sao biết app đã chạy thành công?
**A:** Bạn sẽ thấy:
- Greeting header với tên "Minh"
- PassportCard với level, XP, stats
- Trip cards (Nha Trang, Đà Lạt)
- Destination carousel
- Bottom navigation

### Q: Tôi gặp lỗi "No such module 'shared'"?
**A:** Ignore nó! Comment out dòng `import shared` trong các file:
- iOSApp.swift (dòng 2)
- ContentView.swift (dòng 2)

UI vẫn chạy bình thường vì không dùng shared module.

---

## 🚀 Recommended Path

**For viewing iOS UI now:**
1. Skip Gradle completely
2. Open Xcode
3. Run app
4. Enjoy beautiful UI!

**For full integration later:**
1. Fix Gradle wrapper
2. Build shared framework
3. Integrate Kotlin code
4. Connect to PocketBase API

---

Happy iOS Development! 🎉
