# Chi tiết Kiến trúc - Lazy Travel

## 🎨 Tổng quan Kiến trúc

Dự án sử dụng **Kotlin Multiplatform Mobile (KMM)** kết hợp với **Clean Architecture** để tối đa hóa việc chia sẻ code giữa Android và iOS.

## 📊 Sơ đồ Kiến trúc

```
┌─────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                      │
│  ┌──────────────────┐              ┌──────────────────┐    │
│  │   Android App    │              │    iOS App       │    │
│  │  (Jetpack        │              │   (SwiftUI)      │    │
│  │   Compose)       │              │                  │    │
│  └────────┬─────────┘              └────────┬─────────┘    │
│           │                                 │               │
│           └─────────────┬───────────────────┘               │
│                         │                                   │
│                         ▼                                   │
│           ┌─────────────────────────┐                      │
│           │  DestinationViewModel   │  ◄── Shared Code    │
│           │    (Kotlin/Common)      │                      │
│           └──────────┬──────────────┘                      │
└──────────────────────┼───────────────────────────────────────┘
                       │
┌──────────────────────┼───────────────────────────────────────┐
│              DOMAIN LAYER (Business Logic)                  │
│                      │                                       │
│           ┌──────────▼──────────────┐                       │
│           │  GetDestinationsUseCase │  ◄── Shared Code     │
│           └──────────┬──────────────┘                       │
│                      │                                       │
│           ┌──────────▼──────────────┐                       │
│           │ DestinationRepository   │  (Interface)          │
│           │      (Contract)         │  ◄── Shared Code     │
│           └──────────┬──────────────┘                       │
└──────────────────────┼───────────────────────────────────────┘
                       │
┌──────────────────────┼───────────────────────────────────────┐
│                 DATA LAYER                                   │
│                      │                                       │
│           ┌──────────▼──────────────┐                       │
│           │ DestinationRepositoryImpl│ ◄── Shared Code     │
│           └──────────┬──────────────┘                       │
│                      │                                       │
│           ┌──────────▼──────────────┐                       │
│           │    Data Sources         │                       │
│           │  - API (Future)         │  ◄── Shared Code     │
│           │  - Database (Future)    │                       │
│           │  - Mock Data (Current)  │                       │
│           └─────────────────────────┘                       │
└─────────────────────────────────────────────────────────────┘
```

## 🔄 Luồng dữ liệu chi tiết

### 1. User Interaction Flow

```
User tương tác với UI
    ↓
UI gọi ViewModel method (loadDestinations())
    ↓
ViewModel gọi Use Case
    ↓
Use Case gọi Repository Interface
    ↓
Repository Implementation xử lý logic data
    ↓
Dữ liệu được trả về qua Result<T>
    ↓
ViewModel update UI State (StateFlow)
    ↓
UI observe state change và update
    ↓
User thấy kết quả
```

### 2. State Management Flow

```kotlin
// ViewModel quản lý state
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Destination>) : UiState()
    data class Error(val message: String) : UiState()
}

// StateFlow emit state changes
private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
val uiState: StateFlow<UiState> = _uiState.asStateFlow()
```

## 📦 Module Structure

### Shared Module (100% Shared Code)

```
shared/
├── commonMain/          ← Code chung cho tất cả platforms
│   ├── domain/
│   │   ├── model/      ← Domain entities
│   │   ├── repository/ ← Repository interfaces
│   │   └── usecase/    ← Business logic
│   ├── data/
│   │   └── repository/ ← Data implementations
│   ├── presentation/   ← ViewModels
│   └── di/             ← Dependency injection
├── androidMain/         ← Android-specific code
│   └── Platform.android.kt
└── iosMain/             ← iOS-specific code
    └── Platform.ios.kt
```

## 🎯 Dependency Rules (Clean Architecture)

```
┌─────────────────────────────────────┐
│     Presentation Layer              │  ← Không phụ thuộc vào UI framework
│  - ViewModels (Shared)              │     cụ thể của platform
│  - UI State                         │
└──────────┬──────────────────────────┘
           │ Depends on ↓
┌──────────▼──────────────────────────┐
│     Domain Layer                    │  ← Pure Kotlin, không dependencies
│  - Models                           │     ngoài
│  - Use Cases                        │
│  - Repository Interfaces            │
└──────────┬──────────────────────────┘
           │ Depends on ↓
┌──────────▼──────────────────────────┐
│     Data Layer                      │  ← Implement repositories,
│  - Repository Implementations       │     có thể depend vào network,
│  - Data Sources                     │     database libraries
│  - DTOs / Mappers                   │
└─────────────────────────────────────┘
```

**Nguyên tắc:**
- Domain layer không phụ thuộc vào bất kỳ layer nào
- Data layer phụ thuộc vào Domain (thông qua interfaces)
- Presentation layer phụ thuộc vào Domain
- Dependencies luôn chỉ từ ngoài vào trong

## 🔌 Dependency Injection

Sử dụng **Object Singleton Pattern** đơn giản:

```kotlin
object AppModule {
    // Lazy initialization
    private val repository: DestinationRepository by lazy {
        DestinationRepositoryImpl()
    }

    private val useCase: GetDestinationsUseCase by lazy {
        GetDestinationsUseCase(repository)
    }

    // Public factory method
    fun provideDestinationViewModel(): DestinationViewModel {
        return DestinationViewModel(useCase)
    }
}
```

**Ưu điểm:**
- Đơn giản, dễ hiểu
- Không cần thêm thư viện
- Phù hợp cho app nhỏ/medium

**Nếu cần scale lớn hơn, có thể dùng:**
- Koin (multiplatform DI)
- Kodein

## 🧪 Testing Strategy

### 1. Unit Tests (Domain Layer)

```kotlin
class GetDestinationsUseCaseTest {
    @Test
    fun `should return destinations when repository succeeds`() {
        // Given
        val mockRepository = MockDestinationRepository()
        val useCase = GetDestinationsUseCase(mockRepository)

        // When
        val result = runBlocking { useCase() }

        // Then
        assertTrue(result.isSuccess)
    }
}
```

### 2. Integration Tests (Data Layer)

```kotlin
class DestinationRepositoryTest {
    @Test
    fun `should fetch destinations from API`() {
        // Test repository implementation
    }
}
```

### 3. UI Tests
- Android: Compose UI Tests
- iOS: XCTest / SwiftUI Previews

## 🚀 Mở rộng trong tương lai

### 1. Thêm Network Layer

```kotlin
// data/network/
interface DestinationApi {
    suspend fun getDestinations(): List<DestinationDto>
}

class DestinationApiImpl(
    private val httpClient: HttpClient
) : DestinationApi {
    // Ktor implementation
}
```

### 2. Thêm Database Layer

```kotlin
// data/local/
class DestinationDatabase {
    // SQLDelight implementation
}
```

### 3. Thêm Caching Strategy

```kotlin
class DestinationRepositoryImpl(
    private val api: DestinationApi,
    private val database: DestinationDatabase
) {
    suspend fun getDestinations(): List<Destination> {
        // Try cache first
        val cached = database.getDestinations()
        if (cached.isNotEmpty()) return cached

        // Fetch from network
        val remote = api.getDestinations()
        database.saveDestinations(remote)
        return remote
    }
}
```

## 📱 Platform-Specific Features

### Android

```kotlin
// androidMain/
actual class PlatformSpecificFeature {
    actual fun doSomething() {
        // Android implementation using Android SDK
    }
}
```

### iOS

```kotlin
// iosMain/
actual class PlatformSpecificFeature {
    actual fun doSomething() {
        // iOS implementation using iOS frameworks
    }
}
```

## 🎨 UI Layer Architecture

### Android (Jetpack Compose)

```kotlin
@Composable
fun DestinationScreen(viewModel: DestinationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is Loading -> LoadingView()
        is Success -> DestinationList(data)
        is Error -> ErrorView(message)
    }
}
```

### iOS (SwiftUI)

```swift
struct ContentView: View {
    @StateObject private var viewModel = DestinationViewModelWrapper()

    var body: some View {
        switch viewModel.uiState {
        case .loading:
            ProgressView()
        case .success(let destinations):
            List(destinations) { destination in
                DestinationRow(destination: destination)
            }
        case .error(let message):
            ErrorView(message: message)
        }
    }
}
```

## 💡 Best Practices

1. **Single Responsibility**: Mỗi class chỉ làm một việc
2. **Dependency Inversion**: Depend on abstractions, not implementations
3. **Immutability**: Sử dụng `val` và `data class`
4. **Error Handling**: Sử dụng `Result<T>` để handle errors
5. **Coroutines**: Sử dụng structured concurrency
6. **State Management**: Sử dụng StateFlow cho reactive updates

## 📚 Tài liệu tham khảo

- [Clean Architecture by Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [KMM Samples](https://github.com/JetBrains/compose-multiplatform)
