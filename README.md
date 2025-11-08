# Lazy Travel - Kotlin Multiplatform Mobile App

Ứng dụng du lịch đa nền tảng sử dụng Kotlin Multiplatform để chia sẻ code giữa iOS và Android.

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
- Android Studio Arctic Fox hoặc mới hơn
- JDK 11 hoặc mới hơn
- Android SDK 24+

### iOS
- Xcode 14.0 hoặc mới hơn
- macOS Monterey hoặc mới hơn
- CocoaPods (tùy chọn)

## 📦 Cài đặt và Chạy

> ⚠️ **Lưu ý:** Dự án này cần kết nối internet lần đầu để download dependencies.
> Xem chi tiết trong [SETUP.md](SETUP.md) để biết hướng dẫn đầy đủ và troubleshooting.

### Yêu cầu
- JDK 17 hoặc mới hơn
- Android Studio Hedgehog (2023.1.1) hoặc mới hơn (cho Android)
- Xcode 14+ (cho iOS, chỉ trên macOS)

### Quick Start

**1. Clone và Setup:**
```bash
git clone <repository-url>
cd lazy-travel

# Nếu chưa có gradlew, tạo wrapper:
gradle wrapper --gradle-version 8.2
```

**2. Android:**
```bash
# Build và install trên emulator/device
./gradlew :androidApp:installDebug

# Hoặc mở trong Android Studio và click Run
```

**3. iOS (macOS only):**
```bash
# Build shared framework
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64

# Mở Xcode project
open iosApp/iosApp.xcodeproj

# Trong Xcode: Chọn scheme và nhấn Run (⌘R)
```

Gặp vấn đề? Xem [SETUP.md](SETUP.md) để biết troubleshooting chi tiết.

## 🔧 Công nghệ sử dụng

### Shared Module
- **Kotlin Multiplatform**: 1.9.20
- **Coroutines**: 1.7.3 (async/await)
- **Kotlin Flow**: State management

### Android
- **Jetpack Compose**: UI framework hiện đại
- **Material Design 3**: Design system
- **Lifecycle**: State management với Compose

### iOS
- **SwiftUI**: Declarative UI framework
- **Combine**: Reactive programming (nếu cần)

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
