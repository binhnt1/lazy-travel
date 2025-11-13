# 🌱 Data Seeding Guide

Complete guide for seeding sample data into PocketBase for development and testing.

## 📦 What's Included

The seed data includes realistic Vietnamese travel data:

### Destinations (8)
- Ha Long Bay (Nature, UNESCO World Heritage)
- Hoi An Ancient Town (Cultural)
- Sapa Terraced Fields (Adventure)
- Phu Quoc Island (Beach)
- Da Lat City (Mountain)
- Ninh Binh (Nature)
- Nha Trang Beach (Beach)
- Phong Nha Cave (Adventure, UNESCO)

### Tours (5)
- Ha Long Bay Luxury Cruise - 2D1N ⭐ HOT
- Hoi An Walking & Cooking Tour 🆕 NEW
- Sapa Trekking Adventure - 3D2N 💰 DISCOUNT
- Phu Quoc Island Hopping
- Phong Nha Cave Exploration ⭐ HOT

### Blog Content
- **Categories (5)**: Travel Tips, Destination Guides, Food & Culture, Adventure Stories, Budget Travel
- **Posts (5)**: Comprehensive articles with realistic content, view counts, and tags

### Buddy Requests (4)
- Weekend getaways, trekking expeditions, beach holidays
- Various statuses (open, full) and slot availability

### Achievements (8)
- Gamification elements: First Trip, Explorer, Social Butterfly, Travel Planner, etc.
- Different categories and point rewards

## 🚀 Quick Start

### Option 1: Auto-Seed on App Start (Recommended)

Add this to your `MainActivity.kt` or app initialization:

```kotlin
import com.lazytravel.data.remote.DataSeeder
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Koin
        initKoin()

        // Auto-seed if database is empty
        lifecycleScope.launch {
            DataSeeder.seedIfEmpty()
        }

        setContent {
            LazyTravelTheme {
                MainScreen()
            }
        }
    }
}
```

### Option 2: Manual Seed Button (For Testing)

Create a debug screen with manual controls:

```kotlin
@Composable
fun DebugSeedScreen() {
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("Ready") }
    var stats by remember { mutableStateOf<DatabaseStats?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Database Seeding", style = MaterialTheme.typography.headlineMedium)

        Text("Status: $status")

        // Stats
        stats?.let {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📊 Database Statistics")
                    Text("Destinations: ${it.destinations}")
                    Text("Tours: ${it.tours}")
                    Text("Blog Posts: ${it.blogPosts}")
                    Text("Buddy Requests: ${it.buddyRequests}")
                    Text("Achievements: ${it.achievements}")
                    Text("Total: ${it.total}")
                }
            }
        }

        // Buttons
        Button(
            onClick = {
                scope.launch {
                    status = "Testing connection..."
                    val connected = DataSeeder.testConnection()
                    status = if (connected) "✅ Connected" else "❌ Failed"
                }
            }
        ) {
            Text("Test Connection")
        }

        Button(
            onClick = {
                scope.launch {
                    status = "Getting stats..."
                    stats = DataSeeder.getStats()
                    status = "✅ Stats loaded"
                }
            }
        ) {
            Text("Get Stats")
        }

        Button(
            onClick = {
                scope.launch {
                    status = "Seeding data..."
                    when (DataSeeder.seedAll()) {
                        is SeedResult.Success -> status = "✅ Seeding complete!"
                        is SeedResult.AlreadySeeded -> status = "ℹ️ Already seeded"
                        is SeedResult.Failed -> status = "❌ Seeding failed"
                    }
                    stats = DataSeeder.getStats()
                }
            }
        ) {
            Text("Seed All Data")
        }

        Button(
            onClick = {
                scope.launch {
                    status = "Clearing data..."
                    DataSeeder.clearAll()
                    status = "🗑️ Data cleared"
                    stats = DataSeeder.getStats()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Clear All Data (Dangerous!)")
        }
    }
}
```

### Option 3: iOS SwiftUI

```swift
import shared

struct ContentView: View {
    @State private var seedStatus = "Ready"

    var body: some View {
        VStack(spacing: 20) {
            Text("Data Seeding")
                .font(.title)

            Text(seedStatus)

            Button("Seed If Empty") {
                seedStatus = "Seeding..."
                Task {
                    let result = await DataSeeder.shared.seedIfEmpty()
                    seedStatus = "Complete"
                }
            }

            Button("Seed All") {
                seedStatus = "Seeding..."
                Task {
                    let result = await DataSeeder.shared.seedAll()
                    seedStatus = "Complete"
                }
            }

            Button("Get Stats") {
                Task {
                    let stats = await DataSeeder.shared.getStats()
                    seedStatus = "Total: \(stats.total) records"
                }
            }
        }
        .padding()
    }
}
```

## 📋 Available Methods

### `DataSeeder.seedIfEmpty()`
- **Safe**: Only seeds if database is empty
- **Use case**: App startup, automatic initialization
- **Returns**: `SeedResult`

### `DataSeeder.seedAll()`
- **Forceful**: Seeds regardless of current state
- **Use case**: Refresh data, reset to defaults
- **Returns**: `SeedResult`

### `DataSeeder.seedDestinations()`
- Seed only destinations (8 items)

### `DataSeeder.seedTours()`
- Seed only tours (5 items)
- Requires destinations to exist first

### `DataSeeder.seedBlogCategories()`
- Seed blog categories and posts
- Creates 5 categories + 5 posts

### `DataSeeder.seedBuddyRequests()`
- Seed buddy requests (4 items)
- Requires destinations to exist first

### `DataSeeder.seedAchievements()`
- Seed achievements (8 items)

### `DataSeeder.clearAll()` ⚠️
- **DANGEROUS**: Deletes all data
- Use only in development
- Cannot be undone

### `DataSeeder.testConnection()`
- Test if PocketBase is accessible
- Returns `Boolean`

### `DataSeeder.getStats()`
- Get count of records in each collection
- Returns `DatabaseStats`

## 🔄 Seeding Order

The seeder automatically handles dependencies:

```
1. Destinations (no dependencies)
2. Blog Categories (no dependencies)
3. Tours (depends on destinations)
4. Blog Posts (depends on categories)
5. Buddy Requests (depends on destinations)
6. Achievements (no dependencies)
```

## 🎯 Sample Data Details

### Destinations
Each destination includes:
- Name, description, location
- Category (Nature, Cultural, Adventure, Beach, Mountain)
- Rating (4.3 - 4.9)
- Price range (700K - 2M VND)
- High-quality Unsplash images

### Tours
Each tour includes:
- Destination link
- Duration (1-3 days)
- Price per person
- Detailed highlights, included/excluded items
- Badges (HOT, NEW, DISCOUNT)
- Rating and review counts
- Available slots

### Blog Posts
Each post includes:
- Realistic Vietnamese travel content
- Categories, tags, view counts
- Read time estimation
- Rich HTML content
- Published timestamps

### Buddy Requests
Each request includes:
- Trip details (dates, slots, price)
- Description and requirements
- Tags for filtering
- Status (open/full)

### Achievements
Gamification elements:
- Different categories (Trips, Explorer, Social, Community, Special)
- Point rewards (100-500 points)
- Icons and descriptions

## 📊 Expected Output

When seeding, you'll see:

```
==========================================================
🌱 LAZY TRAVEL - DATA SEEDING
==========================================================

🔍 Testing PocketBase connection...
📍 URL: http://103.159.51.215:8090
✅ Connected to PocketBase successfully!

📋 Ensuring collections exist...
✅ All collections exist

🌱 Starting complete data seeding...
==================================================

📍 Seeding Destinations...
  ✅ Ha Long Bay
  ✅ Hoi An Ancient Town
  ✅ Sapa Terraced Fields
  ✅ Phu Quoc Island
  ✅ Da Lat City
  ✅ Ninh Binh
  ✅ Nha Trang Beach
  ✅ Phong Nha Cave
📍 Destinations: 8/8 created

📚 Seeding Blog Categories...
  ✅ Travel Tips
  ✅ Destination Guides
  ✅ Food & Culture
  ✅ Adventure Stories
  ✅ Budget Travel
📚 Blog Categories: 5/5 created

📝 Seeding Blog Posts...
  ✅ 10 Essential Tips for First-Time Travelers to Vietnam
  ✅ The Ultimate Guide to Ha Long Bay: Everything You Need to Know
  ✅ Street Food Heaven: Must-Try Dishes in Hanoi
  ✅ Trekking Sapa: A Journey Through Vietnam's Rice Terraces
  ✅ How I Traveled Vietnam for Under $30/Day
📝 Blog Posts: 5/5 created

🎫 Seeding Tours...
  ✅ Ha Long Bay Luxury Cruise - 2D1N
  ✅ Hoi An Walking & Cooking Tour
  ✅ Sapa Trekking Adventure - 3D2N
  ✅ Phu Quoc Island Hopping
  ✅ Phong Nha Cave Exploration
🎫 Tours: 5/5 created

🏆 Seeding Achievements...
  ✅ First Trip
  ✅ Explorer
  ✅ Social Butterfly
  ✅ Travel Planner
  ✅ Helpful Reviewer
  ✅ Budget Master
  ✅ Photographer
  ✅ Adventure Seeker
🏆 Achievements: 8/8 created

🤝 Seeding Buddy Requests...
  ✅ Ha Long Bay Weekend Getaway
  ✅ Sapa Trekking Expedition
  ✅ Phu Quoc Beach Holiday
  ✅ Hoi An Cultural Tour
🤝 Buddy Requests: 4/4 created

==================================================
✅ Complete seeding finished!
💡 You can now use the app with sample data
==================================================

📊 Database Statistics:
   Destinations: 8
   Tours: 5
   Blog Posts: 5
   Buddy Requests: 4
   Achievements: 8
```

## ⚠️ Troubleshooting

### "Cannot connect to PocketBase"
- Check if PocketBase is running: `http://103.159.51.215:8090`
- Verify network connection
- Check firewall settings

### "Collection not found"
- Run `PocketBaseSetup.ensureCollectionsExist()` first
- The seeder calls this automatically, but you can run it manually

### "Failed to create record"
- Check PocketBase logs for details
- Verify schema matches domain models
- Ensure no duplicate data

### Duplicate Data
- Use `DataSeeder.clearAll()` to remove all data first
- Or manually delete collections in PocketBase admin

## 🔧 Customization

To add your own seed data, edit `/shared/src/commonMain/kotlin/com/lazytravel/data/remote/CompleteSeedData.kt`:

```kotlin
val myCustomDestinations = listOf(
    Destination(
        name = "My Custom Place",
        description = "...",
        // ...
    )
)
```

## 🚀 Production Notes

**DO NOT** use this seeder in production:
- ❌ Don't ship seeding code to production
- ❌ Don't auto-seed on production startup
- ✅ Use seeding only for development/testing
- ✅ Create proper data migration scripts for production
- ✅ Use real user-generated data in production

## 📚 Related Files

- `CompleteSeedData.kt` - All seed data definitions
- `DataSeeder.kt` - Seeding utility and helpers
- `PocketBaseSeedData.kt` - Original destinations seeder (legacy)
- `PocketBaseSetup.kt` - Schema and collection setup

## 💡 Tips

1. **First time setup**: Run `seedAll()` to populate everything
2. **Testing**: Use `clearAll()` then `seedAll()` to reset
3. **Stats**: Check `getStats()` to see what's in the database
4. **Selective**: Use individual seed methods for specific collections
5. **Safe mode**: Always use `seedIfEmpty()` on app startup
