# Quick Start - LazyTravel

Setup dự án **CỰC KỲ ĐƠN GIẢN** với 1 command duy nhất!

---

## 🚀 Setup Lần Đầu (1 Command)

```bash
# Pull code
git pull origin claude/integrate-pocketbase-api-011CV5KfSXVm7ctgVvuDyxH2

# Setup tất cả (build framework + install pods + configure)
chmod +x setup-ios.sh
./setup-ios.sh
```

**Thời gian:** ~2-3 phút (lần đầu tiên)

**Script sẽ tự động:**
- ✅ Check CocoaPods installed
- ✅ Build shared framework
- ✅ Install pods
- ✅ Configure Xcode project

---

## 📱 Chạy iOS App

```bash
# Mở Xcode (QUAN TRỌNG: Mở .xcworkspace, KHÔNG phải .xcodeproj)
cd iosApp
open iosApp.xcworkspace
```

**Trong Xcode:**
1. Chọn **Team** (tab Signing & Capabilities)
2. Chọn **iPhone 15 Pro** simulator
3. Press **⌘ + R**

Done! App sẽ hiển thị HomeView với đầy đủ UI.

---

## 🔄 Development Workflow (Cực Đơn Giản)

### Thay Đổi iOS UI (Swift files)

```
Edit .swift file → Xcode: ⌘ + R
```

**Thời gian:** ~5 giây ⚡

---

### Thay Đổi Shared Module (Kotlin files)

**Option 1: Tự động (Recommended)**
```
Edit .kt file → Xcode: ⌘ + B → ⌘ + R
```
Framework tự động rebuild khi bạn build trong Xcode!

**Option 2: Manual rebuild**
```bash
./rebuild-ios.sh
```

**Thời gian:** ~30-60 giây

---

## 🎯 Tóm Tắt So Sánh

### Setup Cũ (Phức Tạp) ❌

```
1. Build framework
2. Mở Xcode
3. Add framework manually vào project
4. Configure Framework Search Paths
5. Configure Build Settings
6. Configure Build Phases
```

### Setup Mới (Đơn Giản) ✅

```bash
./setup-ios.sh  # 1 command duy nhất
open iosApp.xcworkspace
```

---

## 📚 Chi Tiết Hơn?

- **DEV_WORKFLOW.md** - Workflow development chi tiết
- **MAC_BUILD_GUIDE.md** - Build và integration guide
- **IOS_SETUP.md** - Setup thủ công (nếu không dùng script)

---

## 🐛 Troubleshooting

### Lỗi: "pod: command not found"

**Fix:**
```bash
sudo gem install cocoapods
```

### Lỗi: Build fails trong Xcode

**Fix:**
```bash
# Clean và rebuild
cd iosApp
pod install
cd ..
./gradlew :shared:assembleSharedDebugXCFramework
```

Rồi trong Xcode: **⌘ + Shift + K** → **⌘ + R**

### Lỗi: "Module 'shared' not found"

**Nguyên nhân:** Bạn mở nhầm `.xcodeproj` thay vì `.xcworkspace`

**Fix:**
```bash
cd iosApp
open iosApp.xcworkspace  # Đúng
# NOT: open iosApp.xcodeproj  # Sai
```

---

## ✅ Checklist

Setup thành công khi:

- [ ] Script `./setup-ios.sh` chạy không lỗi
- [ ] File `iosApp.xcworkspace` được tạo
- [ ] Mở được `iosApp.xcworkspace` trong Xcode
- [ ] Build thành công (⌘ + B)
- [ ] App run được và hiển thị HomeView

---

## 🎉 Xong!

Giờ bạn có thể code và xem thay đổi ngay lập tức!

**iOS UI:** Edit Swift → ⌘ + R
**Shared logic:** Edit Kotlin → ⌘ + B → ⌘ + R

Simple as that! 🚀
