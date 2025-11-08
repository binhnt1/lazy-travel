# PocketBase Integration Guide

## 🔗 Cấu hình hiện tại

**PocketBase Server:** http://103.159.51.215:8090

## 📋 Cấu trúc dự án

### Files quan trọng

```
shared/src/commonMain/kotlin/com/lazytravel/
├── data/remote/
│   ├── PocketBaseConfig.kt      # ⚙️ Cấu hình (URL, collections)
│   ├── PocketBaseClient.kt      # 🔌 Client singleton
│   └── PocketBaseSetup.kt       # 🛠️ Auto collection setup
├── data/repository/
│   └── DestinationRepositoryImpl.kt  # 💾 CRUD operations
└── domain/model/
    └── Destination.kt           # 📦 Model với @Serializable
```

## 🚀 Cách sử dụng

### 1️⃣ Thay đổi URL khi có domain

Mở file `shared/src/commonMain/kotlin/com/lazytravel/data/remote/PocketBaseConfig.kt`:

```kotlin
object PocketBaseConfig {
    // Thay đổi URL ở đây
    const val BASE_URL = "https://your-domain.com"  // ← Sửa đây

    // Hoặc giữ IP nếu chưa có domain
    // const val BASE_URL = "http://103.159.51.215:8090"
}
```

### 2️⃣ Thêm model mới

**Bước 1:** Tạo Kotlin model với `@Serializable`

```kotlin
// shared/src/commonMain/kotlin/com/lazytravel/domain/model/Hotel.kt
import kotlinx.serialization.Serializable

@Serializable
data class Hotel(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val stars: Int = 0,
    val price: Double = 0.0,
    val created: String = "",
    val updated: String = ""
)
```

**Bước 2:** Thêm collection name vào config

```kotlin
// PocketBaseConfig.kt
object Collections {
    const val DESTINATIONS = "destinations"
    const val HOTELS = "hotels"  // ← Thêm đây
    const val REVIEWS = "reviews"
}
```

**Bước 3:** Thêm vào danh sách auto-setup

```kotlin
// PocketBaseSetup.kt
private val requiredCollections = listOf(
    PocketBaseConfig.Collections.DESTINATIONS,
    PocketBaseConfig.Collections.HOTELS,  // ← Thêm đây
    PocketBaseConfig.Collections.REVIEWS
)
```

**Bước 4:** Restart app - Collection tự động tạo! ✅

### 3️⃣ Thêm field vào model có sẵn

**Chỉ cần sửa Kotlin model:**

```kotlin
@Serializable
data class Destination(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    // ... existing fields

    // ✨ Thêm fields mới
    val facilities: List<String> = emptyList(),  // ← NEW
    val isPopular: Boolean = false,              // ← NEW
    val tags: List<String> = emptyList()         // ← NEW
)
```

**Save data với fields mới:**

```kotlin
val destination = Destination(
    name = "Hạ Long",
    description = "Beautiful bay",
    facilities = listOf("WiFi", "Pool"),
    isPopular = true
)

repository.createDestination(destination)
// ✅ PocketBase tự động chấp nhận fields mới!
```

### 4️⃣ CRUD Operations

**Tạo Repository cho model mới:**

```kotlin
class HotelRepositoryImpl {
    private val client = PocketBaseClient.getInstance()
    private val collectionName = PocketBaseConfig.Collections.HOTELS

    suspend fun getHotels(): List<Hotel> {
        val records = client.records.getList(
            collection = collectionName,
            options = RecordListOptions(page = 1, perPage = 50)
        )
        return records.items.map { /* parse to Hotel */ }
    }

    suspend fun createHotel(hotel: Hotel): Hotel? {
        val record = client.records.create(
            collection = collectionName,
            body = hotel
        )
        return /* parse to Hotel */
    }
}
```

## 🔐 Admin Credentials

Cấu hình trong `PocketBaseConfig.kt`:

```kotlin
object Admin {
    const val EMAIL = "admin@lazytravel.com"
    const val PASSWORD = "your-admin-password"
}
```

**⚠️ Trong production:**
- Đừng hardcode password
- Sử dụng environment variables hoặc secure storage

## 🎯 Workflow

### Khi phát triển:

```
1. Sửa Kotlin models ✏️
2. Thêm collection name vào config (nếu model mới) ✏️
3. Run app ▶️
4. Collection/fields tự động được xử lý ✅
```

### Khi thêm field:

```
1. Sửa Kotlin model thôi! ✏️
2. Run app ▶️
3. PocketBase auto accept fields mới ✅
```

### Khi thêm model:

```
1. Tạo @Serializable data class ✏️
2. Thêm collection name vào PocketBaseConfig ✏️
3. Thêm vào requiredCollections trong PocketBaseSetup ✏️
4. Run app → Auto create collection ✅
```

## 📍 PocketBase Admin UI

**URL:** http://103.159.51.215:8090/_/

Bạn có thể:
- ✅ Xem collections và data
- ✅ Manually tạo collections
- ✅ Edit data trực tiếp
- ✅ Xem API logs
- ✅ Configure rules & permissions

## 🔄 Update URL sang Domain/SSL

**Khi có domain với SSL:**

```kotlin
// 1. Update PocketBaseConfig.kt
object PocketBaseConfig {
    const val BASE_URL = "https://api.lazytravel.com"  // ← New URL
}

// 2. Rebuild app
// 3. Done! ✅
```

**Không cần thay đổi gì khác!**

## 🐛 Troubleshooting

### Connection failed

```kotlin
// Check logs
println("PocketBase URL: ${PocketBaseConfig.BASE_URL}")

// Test connection manually
val client = PocketBaseClient.getInstance()
client.health.check()  // Should return OK
```

### Collections không tự động tạo

**Option 1:** Tạo manual trong Admin UI
1. Vào http://103.159.51.215:8090/_/
2. Click "New Collection"
3. Nhập tên (ví dụ: "hotels")
4. Click "Create"

**Option 2:** Check admin credentials
```kotlin
// Đảm bảo email/password đúng trong PocketBaseConfig
```

### Data không parse được

```kotlin
// Check model có @Serializable không
@Serializable  // ← Bắt buộc!
data class MyModel(...)

// Check field names match với PocketBase
// PocketBase field: "image_url"
// Kotlin field: val imageUrl → Cần mapping!
```

## 💡 Tips

### 1. Field naming conventions

PocketBase sử dụng snake_case, Kotlin dùng camelCase:

```kotlin
@Serializable
data class Destination(
    @SerialName("image_url")  // ← PocketBase field name
    val imageUrl: String = "" // ← Kotlin property name
)
```

### 2. Required vs Optional fields

```kotlin
@Serializable
data class Destination(
    val name: String = "",      // Optional (có default)
    val price: Double = 0.0,    // Optional
    @Required
    val id: String              // Required (không default) - chỉ khi cần
)
```

### 3. Nested objects

```kotlin
@Serializable
data class Location(
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

@Serializable
data class Destination(
    val name: String = "",
    val location: Location = Location()  // ✅ PocketBase hỗ trợ!
)
```

### 4. Realtime subscriptions

```kotlin
// Subscribe to collection changes
client.realtime.subscribe("destinations") { event ->
    when (event.action) {
        "create" -> println("New destination added!")
        "update" -> println("Destination updated!")
        "delete" -> println("Destination deleted!")
    }
}
```

## 📚 Tài liệu thêm

- [PocketBase Docs](https://pocketbase.io/docs/)
- [PocketBase Kotlin Client](https://github.com/agrevster/pocketbase-kotlin)
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)

## ✅ Checklist Setup

- [x] PocketBase server running
- [x] Admin account created
- [x] PocketBase client initialized
- [x] Auto collection setup configured
- [x] Repository using PocketBase
- [x] Models có @Serializable
- [ ] Update URL khi có domain
- [ ] Change admin credentials (production)
- [ ] Setup SSL certificate (production)

---

**Happy coding! 🚀**

Nếu cần thay đổi từ IP sang domain, chỉ cần sửa 1 dòng trong `PocketBaseConfig.kt`!
