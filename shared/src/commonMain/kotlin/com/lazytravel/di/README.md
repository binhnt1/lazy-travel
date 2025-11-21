# Koin Dependency Injection Setup

## 📦 Architecture Overview

```
┌─────────────────────────────────────────┐
│  UI Layer (Compose/SwiftUI)             │
│  - Inject ViewModels from Koin          │
└─────────────────┬───────────────────────┘
                  │ inject
┌─────────────────▼───────────────────────┐
│  ViewModels (PresentationModule)        │
│  - Factory scoped                        │
└─────────────────┬───────────────────────┘
                  │ inject
┌─────────────────▼───────────────────────┐
│  Use Cases (DomainModule)               │
│  - Factory scoped                        │
└─────────────────┬───────────────────────┘
                  │ inject
┌─────────────────▼───────────────────────┐
│  Repositories (DataModule)              │
│  - Singleton scoped                      │
└─────────────────────────────────────────┘
```

## 🚀 Setup

### 1. Initialize Koin

#### Android (MainActivity.kt or Application.onCreate)

```kotlin
import com.lazytravel.di.initKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Koin
        initKoin()

        setContent {
            LazyTravelTheme {
                MainScreen()
            }
        }
    }
}
```

#### iOS (AppDelegate.swift)

```swift
import shared

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        // Initialize Koin
        KoinSetupKt.doInitKoin()

        return true
    }
}
```

### 2. Use ViewModels in UI

#### Android Compose

```kotlin
import com.lazytravel.di.KoinHelper
import androidx.compose.runtime.*

@Composable
fun DestinationsScreen() {
    // Get ViewModel from Koin
    val viewModel = KoinHelper.getDestinationViewModel()

    // Observe state
    val uiState by viewModel.uiState.collectAsState()

    // Load data
    LaunchedEffect(Unit) {
        viewModel.loadDestinations()
    }

    // Render UI based on state
    when (val state = uiState) {
        is DestinationViewModel.UiState.Loading -> LoadingIndicator()
        is DestinationViewModel.UiState.Success -> DestinationList(state.destinations)
        is DestinationViewModel.UiState.Error -> ErrorMessage(state.message)
    }
}
```

#### iOS SwiftUI

```swift
import shared

struct DestinationsView: View {
    // Get ViewModel from Koin
    let viewModel = KoinHelper.shared.getDestinationViewModel()

    @State private var destinations: [Destination] = []
    @State private var isLoading = true

    var body: some View {
        VStack {
            if isLoading {
                ProgressView()
            } else {
                List(destinations, id: \.id) { destination in
                    DestinationRow(destination: destination)
                }
            }
        }
        .onAppear {
            viewModel.loadDestinations()
            observeViewModel()
        }
    }

    private func observeViewModel() {
        // Observe StateFlow from Kotlin
        viewModel.uiState.collect { state in
            switch state {
            case is DestinationViewModel.UiStateLoading:
                isLoading = true
            case let success as DestinationViewModel.UiStateSuccess:
                isLoading = false
                destinations = success.destinations
            case is DestinationViewModel.UiStateError:
                isLoading = false
            default:
                break
            }
        }
    }
}
```

## 📋 Available ViewModels

All ViewModels can be accessed through `KoinHelper`:

```kotlin
// Destinations
val destinationViewModel = KoinHelper.getDestinationViewModel()

// Trips
val tripsViewModel = KoinHelper.getTripsViewModel()

// Tours
val toursViewModel = KoinHelper.getToursViewModel()

// Posts (Social Feed)
val postsViewModel = KoinHelper.getPostsViewModel()

// Blog
val blogViewModel = KoinHelper.getBlogViewModel()

// Buddy Requests
val buddyRequestViewModel = KoinHelper.getBuddyRequestViewModel()

// Notifications
val notificationViewModel = KoinHelper.getNotificationViewModel()

// User Profile
val userProfileViewModel = KoinHelper.getUserProfileViewModel()
```

## 🔧 Modules

### DataModule
- **Scope**: Singleton
- **Contains**: Repository implementations (TripRepositoryImpl, TourRepositoryImpl, etc.)
- **Why Singleton**: Repositories should be shared across the app to maintain cache and avoid duplicate instances

### DomainModule
- **Scope**: Factory
- **Contains**: Use cases (GetTripsUseCase, CreateTripUseCase, etc.)
- **Why Factory**: Use cases are stateless and can be created on demand

### PresentationModule
- **Scope**: Factory
- **Contains**: ViewModels (TripsViewModel, ToursViewModel, etc.)
- **Why Factory**: ViewModels should be created fresh for each screen instance

## 🧪 Testing

### Unit Testing with Koin

```kotlin
class TripsViewModelTest : KoinTest {

    @Before
    fun setup() {
        startKoin {
            modules(testModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `when loadTrips succeeds, state should be Success`() = runTest {
        val viewModel: TripsViewModel by inject()

        viewModel.loadTrips("user123")

        val state = viewModel.tripsState.value
        assertTrue(state is TripsViewModel.TripsUiState.Success)
    }
}

val testModule = module {
    single<TripRepository> { FakeTripRepository() }
    factory { GetTripsUseCase(get()) }
    factory { TripsViewModel(get(), get(), get()) }
}
```

## ⚙️ Advanced Usage

### Manual Injection (if needed)

```kotlin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyCustomClass : KoinComponent {
    private val tripRepository: TripRepository by inject()

    suspend fun doSomething() {
        val trips = tripRepository.getTrips("user123")
        // ...
    }
}
```

### Get instance directly

```kotlin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AnotherClass : KoinComponent {
    fun performAction() {
        val viewModel = get<TripsViewModel>()
        // ...
    }
}
```

## 📚 Best Practices

1. **Always use interfaces**: Inject interfaces (e.g., `TripRepository`), not implementations
2. **Proper scoping**:
   - Use `single` for stateful objects that should be shared (Repositories)
   - Use `factory` for stateless objects that can be recreated (Use Cases, ViewModels)
3. **Lazy injection**: Use `by inject()` for lazy initialization
4. **Testing**: Create test modules with fake implementations for unit testing
5. **Initialize early**: Call `initKoin()` in MainActivity/AppDelegate before any DI usage

## 🐛 Troubleshooting

### "No definition found for..."
- Make sure `initKoin()` is called before accessing any dependencies
- Check if the module containing the dependency is included in `initKoin()`

### "Already registered"
- You may have called `initKoin()` multiple times
- Use `stopKoin()` before re-initializing in tests

### iOS: Cannot find ViewModels
- Make sure the Kotlin framework exports all ViewModels
- Check that `KoinSetupKt.doInitKoin()` is called in AppDelegate
