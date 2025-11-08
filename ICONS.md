# Hướng dẫn tạo App Icons

## 🎨 Cấu trúc Icon cho Android

Hiện tại project đã có **Adaptive Icons** (Android 8.0+) sử dụng vector drawables. Để hỗ trợ đầy đủ tất cả thiết bị, bạn cần thêm bitmap icons.

## 📁 Cấu trúc thư mục

```
androidApp/src/main/res/
├── drawable/
│   ├── ic_launcher_background.xml   ✅ Đã có (vector)
│   ├── ic_launcher_foreground.xml   ✅ Đã có (vector)
│   └── ic_launcher_legacy.xml       ✅ Đã có (fallback)
├── mipmap-anydpi-v26/
│   ├── ic_launcher.xml              ✅ Đã có (adaptive icon)
│   └── ic_launcher_round.xml        ✅ Đã có (adaptive icon)
├── mipmap-mdpi/                     ⚠️ Cần thêm PNG
│   ├── ic_launcher.png              (48x48 px)
│   └── ic_launcher_round.png        (48x48 px)
├── mipmap-hdpi/                     ⚠️ Cần thêm PNG
│   ├── ic_launcher.png              (72x72 px)
│   └── ic_launcher_round.png        (72x72 px)
├── mipmap-xhdpi/                    ⚠️ Cần thêm PNG
│   ├── ic_launcher.png              (96x96 px)
│   └── ic_launcher_round.png        (96x96 px)
├── mipmap-xxhdpi/                   ⚠️ Cần thêm PNG
│   ├── ic_launcher.png              (144x144 px)
│   └── ic_launcher_round.png        (144x144 px)
└── mipmap-xxxhdpi/                  ⚠️ Cần thêm PNG
    ├── ic_launcher.png              (192x192 px)
    └── ic_launcher_round.png        (192x192 px)
```

## 🚀 Cách 1: Sử dụng Android Studio Image Asset (Khuyến nghị)

### Bước 1: Mở Image Asset Studio

1. Mở project trong Android Studio
2. Click chuột phải vào `androidApp/src/main/res`
3. Chọn **New > Image Asset**

### Bước 2: Cấu hình Icon

1. **Icon Type**: Launcher Icons (Adaptive and Legacy)
2. **Foreground Layer**:
   - **Source Asset Type**: Image, Clip Art, hoặc Text
   - Nếu chọn Image: Browse và chọn file icon của bạn (PNG, JPG, SVG)
   - Nếu chọn Clip Art: Chọn từ thư viện có sẵn
3. **Background Layer**:
   - **Source Asset Type**: Color
   - Chọn màu background (ví dụ: #2196F3 - màu xanh)
4. **Options**:
   - ✅ Trim (cắt khoảng trống)
   - Resize: 100%
   - Shape: None, Circle, Square, Squircle (tùy ý)

### Bước 3: Generate

1. Click **Next**
2. Xem preview các icon sẽ được tạo
3. Click **Finish**

Android Studio sẽ tự động tạo tất cả các size icons cần thiết!

## 🎯 Cách 2: Tạo Icon thủ công

### A. Chuẩn bị Icon gốc

Tạo 1 file icon vuông có kích thước **1024x1024 px** (hoặc ít nhất 512x512 px).

### B. Resize cho từng density

Sử dụng các tool online hoặc photoshop để resize:

| Density | Size | Đặt vào thư mục |
|---------|------|-----------------|
| mdpi    | 48x48 px | `mipmap-mdpi/` |
| hdpi    | 72x72 px | `mipmap-hdpi/` |
| xhdpi   | 96x96 px | `mipmap-xhdpi/` |
| xxhdpi  | 144x144 px | `mipmap-xxhdpi/` |
| xxxhdpi | 192x192 px | `mipmap-xxxhdpi/` |

### C. Đặt vào đúng thư mục

Copy các file PNG vào từng thư mục tương ứng với tên:
- `ic_launcher.png` (icon vuông)
- `ic_launcher_round.png` (icon tròn)

## 🌐 Cách 3: Dùng Online Tools

### AppIcon.co (Miễn phí)
1. Truy cập: https://www.appicon.co/
2. Upload icon gốc (1024x1024 px)
3. Chọn **Android**
4. Download và giải nén
5. Copy các thư mục mipmap vào `androidApp/src/main/res/`

### Android Asset Studio (Google)
1. Truy cập: https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html
2. Upload icon hoặc dùng Clipart
3. Customize màu sắc, padding, shape
4. Download ZIP
5. Extract và copy vào project

### Icon Kitchen
1. Truy cập: https://icon.kitchen/
2. Upload hình hoặc chọn từ library
3. Customize background, foreground
4. Download cho Android
5. Copy vào project

## 📝 Quy tắc Thiết kế Icon

### 1. Size và Padding
- Icon gốc: 1024x1024 px
- Safe zone: Giữ nội dung quan trọng trong vùng tròn có đường kính 66% kích thước canvas
- Adaptive icon: Foreground layer có thể bị crop ở các góc

### 2. Màu sắc
- Sử dụng màu nổi bật, dễ nhận diện
- Tránh quá nhiều chi tiết nhỏ
- Đảm bảo contrast tốt giữa foreground và background

### 3. Format
- **PNG** với transparent background (khuyến nghị)
- Hoặc **WebP** (tiết kiệm dung lượng)
- Tránh dùng JPG (không hỗ trợ transparency)

## 🔧 Troubleshooting

### Lỗi: "resource mipmap/ic_launcher not found"

**Nguyên nhân:** Thiếu icon files

**Giải pháp:**

#### Option 1: Tạm thời dùng drawable (quick fix)
Sửa `AndroidManifest.xml`:
```xml
<application
    android:icon="@drawable/ic_launcher_legacy"
    android:roundIcon="@drawable/ic_launcher_legacy"
    ...>
```

#### Option 2: Tạo icons đầy đủ
Làm theo **Cách 1** ở trên (khuyến nghị).

### Build thành công nhưng icon xấu

- Kiểm tra lại size của từng file PNG
- Đảm bảo không bị stretch hay compress
- Sử dụng Image Asset Studio để tự động generate

## 🎨 Thiết kế Icon cho Lazy Travel

### Gợi ý concept:

1. **Máy bay** ✈️
   - Vector máy bay đơn giản
   - Background: gradient xanh dương (sky theme)

2. **Bản đồ + Pin** 📍
   - Icon bản đồ với location marker
   - Background: màu xanh lá (travel theme)

3. **Vali du lịch** 🧳
   - Icon vali hoặc ba lô
   - Background: màu cam/vàng (adventure theme)

4. **Compass** 🧭
   - La bàn định hướng
   - Background: gradient biển

### Icon hiện tại

Project đã có một icon máy bay đơn giản (vector) với:
- **Foreground:** Máy bay trắng
- **Background:** Màu xanh dương #2196F3

Bạn có thể:
- Giữ nguyên và generate PNG từ đây
- Hoặc thay bằng design riêng của bạn

## 💡 Khuyến nghị

Để development nhanh:
1. Sử dụng **Android Studio Image Asset** (nhanh nhất)
2. Hoặc download icon miễn phí từ:
   - [Flaticon](https://www.flaticon.com/)
   - [Icons8](https://icons8.com/)
   - [Material Icons](https://fonts.google.com/icons)
3. Sau đó dùng Image Asset để generate tất cả sizes

Để production:
1. Thiết kế icon chuyên nghiệp với Figma/Adobe Illustrator
2. Export ở size 1024x1024 px
3. Dùng Image Asset để generate tất cả sizes
4. Test trên nhiều devices và launchers khác nhau

## ✅ Checklist

- [ ] Tạo icon gốc 1024x1024 px
- [ ] Generate icons cho tất cả densities
- [ ] Copy vào các thư mục mipmap tương ứng
- [ ] Kiểm tra AndroidManifest.xml có đúng reference
- [ ] Build và test trên emulator/device
- [ ] Kiểm tra icon hiển thị đúng trên launcher
- [ ] Test adaptive icon trên Android 8.0+
- [ ] Test round icon trên các launcher hỗ trợ

---

**Happy icon creating! 🎨**
