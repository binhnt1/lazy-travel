# 📋 Schema Definition System Guide

## 🎯 Tổng quan

Schema Definition System cho phép bạn định nghĩa cấu trúc database bằng **Kotlin DSL** thay vì phải vào PocketBase dashboard.

### ✅ Ưu điểm:
- **Type-safe**: Compiler check lỗi syntax
- **Version control**: Schema được commit vào git
- **Auto-migration**: Tự động sync lên PocketBase khi app start
- **Readable**: Dễ đọc, dễ hiểu hơn JSON
- **Validation**: Có min/max, required, pattern, etc.
- **Relations**: Dễ dàng tạo foreign keys
- **Indexes**: Tối ưu query performance

---

## 📁 Cấu trúc files

```
shared/src/commonMain/kotlin/com/lazytravel/data/remote/schema/
├── SchemaDefinition.kt       # DSL core (đã có sẵn)
├── SchemaMigration.kt        # Migration engine (đã có sẵn)
├── DestinationSchema.kt      # Example schema (đã có sẵn)
├── HotelSchema.kt            # ← BẠN SẼ TẠO
├── ReviewSchema.kt           # ← BẠN SẼ TẠO
└── YourNewSchema.kt          # ← BẠN SẼ TẠO
```

---

## 🚀 Cách sử dụng

### Bước 1: Tạo Schema Definition

Tạo file mới trong `data/remote/schema/`, ví dụ `HotelSchema.kt`:

```kotlin
package com.lazytravel.data.remote.schema

val hotelsSchema = collectionSchema {
    name = "hotels"
    type = CollectionType.BASE

    fields {
        text("name") {
            required = true
            min = 2
            max = 200
        }

        number("stars") {
            required = true
            min = 1.0
            max = 5.0
            onlyInt = true
        }

        number("pricePerNight") {
            required = true
            min = 0.0
        }

        bool("isAvailable") {
            required = true
        }
    }

    indexes {
        index("name")
        index("stars")
    }

    // API Rules
    listRule = null      // Public: Anyone can list
    viewRule = null      // Public: Anyone can view
    createRule = ""      // Auth required: Only logged in users
    updateRule = ""      // Auth required
    deleteRule = ""      // Auth required
}
```

### Bước 2: Thêm vào Migration

Mở `PocketBaseSetup.kt` và thêm schema vào:

```kotlin
// Line 40-45
val migrationResult = SchemaMigration.migrate(
    destinationsSchema,
    hotelsSchema,        // ← THÊM ĐÂY
    // reviewsSchema,    // ← Uncomment khi có
)
```

### Bước 3: Import schema (nếu cần)

Thêm import ở đầu file `PocketBaseSetup.kt`:

```kotlin
import com.lazytravel.data.remote.schema.hotelsSchema
```

### Bước 4: Remove khỏi Legacy Collections

Trong `PocketBaseSetup.kt`, xóa collection khỏi `legacyCollections`:

```kotlin
// Line 23-26
private val legacyCollections = listOf(
    // PocketBaseConfig.Collections.HOTELS,  // ← Comment hoặc xóa dòng này
    PocketBaseConfig.Collections.REVIEWS
)
```

### Bước 5: Run app!

Khi app start, schema sẽ tự động được sync lên PocketBase:
```
🔍 Checking PocketBase collections...
📋 Using Schema Migration System...
✅ Admin authenticated
➕ Creating collection 'hotels'...
✅ Created collection 'hotels'
```

---

## 📖 DSL Reference

### Field Types

#### 1. Text Field
```kotlin
text("fieldName") {
    required = true
    min = 2           // Minimum length
    max = 200         // Maximum length
    pattern = "^[a-z]+$"  // Regex pattern
}
```

#### 2. Number Field
```kotlin
number("price") {
    required = true
    min = 0.0
    max = 1000000.0
    onlyInt = false    // true = chỉ integer
}
```

#### 3. Boolean Field
```kotlin
bool("isActive") {
    required = true
}
```

#### 4. Email Field
```kotlin
email("email") {
    required = true
    onlyDomains = listOf("gmail.com", "example.com")
    exceptDomains = listOf("tempmail.com")
}
```

#### 5. URL Field
```kotlin
url("website") {
    required = false
    onlyDomains = listOf("example.com")
    exceptDomains = listOf("malicious.com")
}
```

#### 6. Date Field
```kotlin
date("startDate") {
    required = true
    min = "2024-01-01"
    max = "2025-12-31"
}
```

#### 7. Select Field (Dropdown)
```kotlin
select("category") {
    required = true
    values = listOf("Option1", "Option2", "Option3")
    maxSelect = 1     // 1 = single select, >1 = multiple select
}
```

**Multiple select example:**
```kotlin
select("tags") {
    values = listOf("Tag1", "Tag2", "Tag3", "Tag4")
    maxSelect = 5     // Allow selecting up to 5 tags
}
```

#### 8. JSON Field
```kotlin
json("metadata") {
    required = false
    maxSize = 2000000  // Max size in bytes
}
```

#### 9. File Field (Images, PDFs, etc.)
```kotlin
file("images") {
    required = false
    maxSelect = 5      // Number of files
    maxSize = 5242880  // 5MB per file
    mimeTypes = listOf(
        "image/jpeg",
        "image/png",
        "image/webp"
    )
    thumbs = listOf(   // Thumbnail sizes
        "100x100",
        "300x300",
        "600x600"
    )
}
```

#### 10. Relation Field (Foreign Key)
```kotlin
relation("destination") {
    collectionId = "destinations"  // Target collection
    maxSelect = 1                  // 1 = one-to-one, >1 = one-to-many
    minSelect = 0
    cascadeDelete = false          // Delete related records?
    required = true
}
```

**Multiple relations example:**
```kotlin
relation("amenities") {
    collectionId = "amenities"
    maxSelect = 10               // Can select multiple amenities
    cascadeDelete = false
}
```

#### 11. Editor Field (Rich Text)
```kotlin
editor("description") {
    required = true
    convertUrls = true    // Auto-convert URLs to links
}
```

### Indexes

Indexes tăng tốc độ query:

```kotlin
indexes {
    // Single field index
    index("name")
    index("category")

    // Composite index (multiple fields)
    compositeIndex("category", "rating")
    compositeIndex("destination", "stars")

    // Unique index (no duplicates)
    uniqueIndex("email")
    uniqueIndex("slug")
}
```

### API Rules

Control quyền truy cập:

```kotlin
// null = Public (anyone can access, no login required)
listRule = null
viewRule = null

// "" = Auth required (must be logged in)
createRule = ""
updateRule = ""
deleteRule = ""

// Custom rules (PocketBase filter syntax)
listRule = "@request.auth.id != ''"              // Must be logged in
viewRule = "@request.auth.id = ownerId"          // Can only view own records
updateRule = "@request.auth.role = 'admin'"      // Only admins
deleteRule = "@request.auth.id = ownerId || @request.auth.role = 'admin'"  // Owner or admin
```

---

## 💡 Examples

### Example 1: Hotels Collection

```kotlin
val hotelsSchema = collectionSchema {
    name = "hotels"
    type = CollectionType.BASE

    fields {
        // Basic info
        text("name") {
            required = true
            min = 2
            max = 200
        }

        text("address") {
            required = true
            max = 500
        }

        text("phone") {
            required = false
            pattern = "^\\+?[0-9]{10,15}$"  // Phone number format
        }

        email("email") {
            required = false
        }

        url("website") {
            required = false
        }

        // Rating & Price
        number("stars") {
            required = true
            min = 1.0
            max = 5.0
            onlyInt = true
        }

        number("pricePerNight") {
            required = true
            min = 0.0
        }

        // Location - Relation to destinations
        relation("destination") {
            collectionId = "destinations"
            maxSelect = 1
            required = true
        }

        // Images
        file("images") {
            maxSelect = 10
            maxSize = 5242880  // 5MB
            mimeTypes = listOf("image/jpeg", "image/png", "image/webp")
            thumbs = listOf("100x100", "300x300", "600x600")
        }

        // Amenities - Multiple select
        select("amenities") {
            values = listOf(
                "WiFi",
                "Pool",
                "Gym",
                "Restaurant",
                "Bar",
                "Parking",
                "Spa",
                "Airport Shuttle",
                "Pet Friendly",
                "Business Center"
            )
            maxSelect = 10
        }

        // Description
        editor("description") {
            required = true
            convertUrls = true
        }

        // Additional metadata
        json("facilities") {
            required = false
        }

        // Status
        bool("isAvailable") {
            required = true
        }

        bool("isFeatured") {
            required = false
        }
    }

    indexes {
        index("name")
        index("stars")
        index("pricePerNight")
        compositeIndex("destination", "stars")
        compositeIndex("destination", "pricePerNight")
    }

    listRule = null
    viewRule = null
    createRule = ""
    updateRule = ""
    deleteRule = ""
}
```

### Example 2: Reviews Collection

```kotlin
val reviewsSchema = collectionSchema {
    name = "reviews"
    type = CollectionType.BASE

    fields {
        // Relations - Can review either destination or hotel
        relation("destination") {
            collectionId = "destinations"
            maxSelect = 1
            required = false
        }

        relation("hotel") {
            collectionId = "hotels"
            maxSelect = 1
            required = false
        }

        // Review content
        editor("content") {
            required = true
        }

        // Rating
        number("rating") {
            required = true
            min = 1.0
            max = 5.0
        }

        // Reviewer info
        text("reviewerName") {
            required = true
            max = 100
        }

        email("reviewerEmail") {
            required = false
        }

        // Photos
        file("photos") {
            maxSelect = 5
            maxSize = 5242880
            mimeTypes = listOf("image/jpeg", "image/png")
            thumbs = listOf("200x200", "600x600")
        }

        // Status
        bool("isVerified") {
            required = false
        }

        bool("isVisible") {
            required = true
        }
    }

    indexes {
        index("rating")
        compositeIndex("destination", "rating")
        compositeIndex("hotel", "rating")
    }

    listRule = null
    viewRule = null
    createRule = ""
    updateRule = "@request.auth.id = ownerId"
    deleteRule = "@request.auth.id = ownerId"
}
```

### Example 3: User Profile Collection

```kotlin
val userProfilesSchema = collectionSchema {
    name = "user_profiles"
    type = CollectionType.BASE

    fields {
        // Link to auth user
        relation("user") {
            collectionId = "_pb_users_auth_"  // Special collection for auth users
            maxSelect = 1
            required = true
        }

        text("displayName") {
            required = true
            min = 2
            max = 50
        }

        text("bio") {
            required = false
            max = 500
        }

        file("avatar") {
            maxSelect = 1
            maxSize = 2097152  // 2MB
            mimeTypes = listOf("image/jpeg", "image/png")
            thumbs = listOf("100x100", "300x300")
        }

        url("website") {
            required = false
        }

        select("preferredLanguage") {
            values = listOf("en", "vi", "ja", "ko")
            maxSelect = 1
        }

        select("interests") {
            values = listOf(
                "Beach",
                "Mountain",
                "Cultural",
                "Nature",
                "Adventure",
                "Food",
                "Photography",
                "History"
            )
            maxSelect = 5
        }

        json("preferences") {
            required = false
        }
    }

    indexes {
        uniqueIndex("user")  // One profile per user
        index("displayName")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id = user.id"
    deleteRule = "@request.auth.id = user.id"
}
```

---

## 🔄 Workflow

### Development Flow:

```
1. Tạo file schema mới (e.g., HotelSchema.kt)
   ↓
2. Define schema với DSL
   ↓
3. Thêm vào PocketBaseSetup.kt
   ↓
4. Run app
   ↓
5. Schema tự động sync lên PocketBase ✅
```

### Updating Schema:

```
1. Sửa schema definition file
   ↓
2. Run app
   ↓
3. Schema tự động update ✅
```

**Note:** PocketBase sẽ KHÔNG xóa fields cũ khi bạn remove khỏi schema. Nếu muốn xóa field, phải vào dashboard.

### Adding New Field:

```kotlin
// Chỉ cần thêm vào schema:
fields {
    // ... existing fields

    text("newField") {  // ← Thêm field mới
        required = false
    }
}
```

Run app → Field được tạo tự động!

---

## ⚡ Tips & Best Practices

### 1. Required vs Optional
```kotlin
// Required - User MUST provide value
text("name") {
    required = true
}

// Optional - Can be empty
text("nickname") {
    required = false
}
```

### 2. Default Values
PocketBase sẽ dùng default values dựa trên type:
- Text: `""`
- Number: `0`
- Bool: `false`
- Array: `[]`

### 3. Relations Best Practices
```kotlin
// One-to-one
relation("owner") {
    collectionId = "users"
    maxSelect = 1
    required = true
}

// One-to-many
relation("tags") {
    collectionId = "tags"
    maxSelect = 10
    required = false
}

// Many-to-many
// Cần tạo junction table riêng
```

### 4. File Upload Best Practices
```kotlin
file("images") {
    maxSelect = 5
    maxSize = 5242880  // 5MB - điều chỉnh theo nhu cầu

    // Chỉ accept image types
    mimeTypes = listOf(
        "image/jpeg",
        "image/png",
        "image/webp"
    )

    // Generate thumbnails
    thumbs = listOf(
        "100x100",    // List view
        "300x300",    // Card view
        "600x600"     // Detail view
    )
}
```

### 5. Index Strategy
Index những fields hay query:
```kotlin
indexes {
    // Search by name
    index("name")

    // Filter by category
    index("category")

    // Sort by rating
    index("rating")

    // Combined filters
    compositeIndex("category", "rating")
}
```

⚠️ **Cảnh báo:** Quá nhiều indexes có thể làm chậm write operations.

### 6. API Rules Examples
```kotlin
// Public read, auth write
listRule = null
viewRule = null
createRule = ""
updateRule = ""
deleteRule = ""

// Only owner can modify
updateRule = "@request.auth.id = ownerId"
deleteRule = "@request.auth.id = ownerId"

// Admin only
updateRule = "@request.auth.role = 'admin'"
deleteRule = "@request.auth.role = 'admin'"

// Complex rule
deleteRule = "@request.auth.id = ownerId || @request.auth.role = 'admin'"
```

---

## 🐛 Troubleshooting

### Schema không sync được

**Kiểm tra:**
1. Admin credentials đúng không? (`PocketBaseConfig.Admin`)
2. PocketBase server có chạy không?
3. Check logs trong console

### Collection không tạo được

**Debug:**
```kotlin
// In SchemaMigration.kt, output đã có logs
🚀 Starting schema migration...
✅ Admin authenticated
➕ Creating collection 'hotels'...
❌ Failed to create 'hotels': [error message]
```

### Field type không đúng

Đảm bảo field type trong schema match với Kotlin model:
```kotlin
// Schema
number("rating") { ... }

// Kotlin model
@Serializable
data class Hotel(
    val rating: Double  // ← Phải là Double, không phải String!
)
```

---

## 📚 Tham khảo thêm

- [PocketBase Collections](https://pocketbase.io/docs/collections/)
- [PocketBase API Rules](https://pocketbase.io/docs/api-rules-and-filters/)
- [PocketBase Field Types](https://pocketbase.io/docs/collections/#fields)

---

## 🎉 Kết luận

Với Schema Definition System, bạn có thể:
- ✅ Tạo collections nhanh chóng với full validation
- ✅ Version control schema trong git
- ✅ Tránh phải vào dashboard nhiều lần
- ✅ Type-safe và dễ maintain
- ✅ Auto-migration khi app start

**Workflow đơn giản:**
1. Tạo schema file
2. Thêm vào PocketBaseSetup
3. Run app
4. Done! 🎉
