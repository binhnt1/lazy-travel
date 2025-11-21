# Lazy Travel - Kotlin Multiplatform Mobile App

Ứng dụng du lịch đa nền tảng sử dụng Kotlin Multiplatform để chia sẻ code giữa iOS và Android.

## 🚀 Quick Start (Cực Đơn Giản!)

### iOS Setup (1 Command):

```bash
./setup-ios.sh
open iosApp/iosApp.xcworkspace
```

**Chi tiết:** Xem [QUICK_START.md](QUICK_START.md)

### Android:

```bash
# Mở Android Studio → Open Project → Chọn folder này
# Click Run ▶️
```

---

## 📚 Documentation

- **[QUICK_START.md](QUICK_START.md)** - Setup siêu đơn giản (RECOMMENDED) ⭐
- **[DEV_WORKFLOW.md](DEV_WORKFLOW.md)** - Development workflow chi tiết
- **[MAC_BUILD_GUIDE.md](MAC_BUILD_GUIDE.md)** - Build guide và troubleshooting
- **[IOS_SETUP.md](IOS_SETUP.md)** - iOS setup thủ công (nếu cần)

---

## 🏗️ Kiến trúc

Dự án sử dụng **Clean Architecture** với 3 layers chính:

```
LazyTravel/
├── shared/                          # Module Kotlin Multiplatform
│   └── src/
│       ├── commonMain/              # Code dùng chung cho cả iOS và Android
│       │   └── kotlin/com/lazytravel/
│       │       ├── domain/          # Business Logic Layer
│       │       │   ├── model/       # Domain Models (Destination)
│       │       │   ├── repository/  # Repository Interfaces
│       │       │   └── usecase/     # Use Cases (GetDestinationsUseCase)
│       │       ├── data/            # Data Layer
│       │       │   └── repository/  # Repository Implementations
│       │       ├── presentation/    # Presentation Layer
│       │       │   └── DestinationViewModel.kt
│       │       └── di/              # Dependency Injection
│       │           └── AppModule.kt
│       ├── androidMain/             # Code riêng cho Android
│       │   └── kotlin/com/lazytravel/
│       │       └── Platform.android.kt
│       └── iosMain/                 # Code riêng cho iOS
│           └── kotlin/com/lazytravel/
│               └── Platform.ios.kt
├── androidApp/                      # Android Application
│   └── src/main/
│       ├── kotlin/com/lazytravel/android/
│       │   └── MainActivity.kt      # Jetpack Compose UI
│       └── AndroidManifest.xml
└── iosApp/                          # iOS Application
    └── iosApp/
        ├── iOSApp.swift
        └── ContentView.swift        # SwiftUI
```

## 📱 Các Layer trong Kiến trúc

### 1. Domain Layer (Business Logic)
- **Models**: Định nghĩa các entity nghiệp vụ (`Destination`)
- **Repository Interfaces**: Contract cho việc truy xuất dữ liệu
- **Use Cases**: Chứa logic nghiệp vụ cụ thể (`GetDestinationsUseCase`)

### 2. Data Layer
- **Repository Implementations**: Triển khai cụ thể việc lấy dữ liệu
- Hiện tại sử dụng mock data, có thể mở rộng thành API calls

### 3. Presentation Layer
- **ViewModel**: Quản lý UI state và business logic cho màn hình
- Sử dụng Kotlin Flow để quản lý state
- Shared giữa Android và iOS

## 🚀 Yêu cầu

### Android
- **Android Studio**: Koala+ (2024.1.1)
- **JDK**: 21 (LTS)
- **Android SDK**: 24 - 36

### iOS (macOS only)
- **macOS**: Monterey+ (M1/M2 hoặc Intel)
- **Xcode**: 15.0+
- **Java**: 21 (cho Gradle)
- **CocoaPods**: Tự động install qua script

---

## 📦 Cài đặt (Siêu Đơn Giản!)

### iOS (1 command):

```bash
./setup-ios.sh
```

Script tự động:
- ✅ Check & install CocoaPods
- ✅ Build shared framework
- ✅ Configure Xcode project
- ✅ Ready to run!

### Android:

```bash
# Open Android Studio
# File → Open → Chọn folder lazy-travel
# Click Run ▶️
```

**Chi tiết:** Xem [QUICK_START.md](QUICK_START.md) cho hướng dẫn đầy đủ.

## 🔧 Công nghệ sử dụng

### Shared Module
- **Kotlin Multiplatform**: 2.2.21
- **Compose Multiplatform**: 1.9.3
- **Coroutines**: 1.10.2 (async/await)
- **Kotlin Flow**: State management
- **Ktor**: 3.3.2 (HTTP client)
- **Kotlinx Serialization**: 1.9.0 (JSON)
- **Koin**: 4.1.1 (Dependency Injection)

### Android
- **Jetpack Compose**: UI framework hiện đại
- **Material Design 3**: Design system
- **Lifecycle**: State management với Compose
- **Android SDK**: 36 (min 24)

### iOS
- **SwiftUI**: Declarative UI framework
- **iOS Deployment Target**: 15.0+
- **CocoaPods**: Dependency management

### Backend
- **PocketBase**: Backend-as-a-Service
- API endpoint: `http://103.159.51.215:8090`

## 📝 Cách thêm tính năng mới

### 1. Thêm Domain Model
Tạo file trong `shared/src/commonMain/kotlin/com/lazytravel/domain/model/`

```kotlin
data class Hotel(
    val id: String,
    val name: String,
    val price: Double
)
```

### 2. Thêm Repository Interface
Tạo interface trong `shared/src/commonMain/kotlin/com/lazytravel/domain/repository/`

```kotlin
interface HotelRepository {
    suspend fun getHotels(): List<Hotel>
}
```

### 3. Implement Repository
Tạo implementation trong `shared/src/commonMain/kotlin/com/lazytravel/data/repository/`

```kotlin
class HotelRepositoryImpl : HotelRepository {
    override suspend fun getHotels(): List<Hotel> {
        // Implementation
    }
}
```

### 4. Tạo Use Case
Tạo use case trong `shared/src/commonMain/kotlin/com/lazytravel/domain/usecase/`

```kotlin
class GetHotelsUseCase(
    private val repository: HotelRepository
) {
    suspend operator fun invoke(): Result<List<Hotel>> {
        // Business logic
    }
}
```

### 5. Thêm vào DI Module
Update `AppModule.kt` để provide dependencies

### 6. Tạo ViewModel
Tạo ViewModel trong `shared/src/commonMain/kotlin/com/lazytravel/presentation/`

### 7. Tạo UI
- **Android**: Tạo Composable function trong `androidApp`
- **iOS**: Tạo SwiftUI View trong `iosApp`

## 🎯 Điểm mạnh của kiến trúc này

### ✅ Đơn giản
- Không sử dụng DI framework phức tạp (Koin, Dagger)
- Module DI đơn giản với object singleton
- Dễ hiểu cho người mới

### ✅ Scalable
- Dễ dàng mở rộng thêm features
- Tách biệt rõ ràng giữa các layers
- Testable

### ✅ Code Reuse
- Business logic được share 100% giữa iOS và Android
- ViewModel được share, giảm duplicate code
- Chỉ cần viết UI riêng cho mỗi platform

### ✅ Platform Specific
- Android: Jetpack Compose (native Android UI)
- iOS: SwiftUI (native iOS UI)
- Trải nghiệm người dùng tối ưu cho từng platform

## 🔄 Luồng dữ liệu

```
UI (Android/iOS)
    ↓
ViewModel (Shared)
    ↓
Use Case (Shared)
    ↓
Repository Interface (Shared)
    ↓
Repository Implementation (Shared)
    ↓
Data Source (API/Database)
```

## 📚 Tài liệu tham khảo

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [SwiftUI](https://developer.apple.com/xcode/swiftui/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## 🤝 Đóng góp

Nếu bạn muốn đóng góp, vui lòng:
1. Fork repository
2. Tạo feature branch
3. Commit changes
4. Push và tạo Pull Request

## 📄 License

MIT License
