# Schema Definition System

## 📁 Files trong folder này:

- **SchemaDefinition.kt** - Kotlin DSL core để define schemas
- **SchemaMigration.kt** - Migration engine để sync schemas lên PocketBase
- **DestinationSchema.kt** - Example schema cho destinations collection

## 🚀 Quick Start

### Tạo schema mới:

```kotlin
// VD: HotelSchema.kt
package com.lazytravel.data.remote.schema

val hotelsSchema = collectionSchema {
    name = "hotels"
    type = CollectionType.BASE

    fields {
        text("name") {
            required = true
            max = 200
        }

        number("stars") {
            required = true
            min = 1.0
            max = 5.0
        }
    }

    indexes {
        index("name")
    }
}
```

### Thêm vào migration:

Mở `PocketBaseSetup.kt` và thêm:

```kotlin
SchemaMigration.migrate(
    destinationsSchema,
    hotelsSchema,  // ← Thêm đây
)
```

### Run app!

Schema sẽ tự động sync lên PocketBase khi app start.

---

## 📚 Documentation

Xem **SCHEMA_GUIDE.md** ở root folder để biết thêm chi tiết và examples.

---

## 💡 Tips

1. **Field Types:** text, number, bool, email, url, date, select, json, file, relation, editor
2. **Indexes:** Tăng performance cho search/filter
3. **API Rules:** Control quyền truy cập (null = public, "" = auth required)
4. **Relations:** Dễ dàng tạo foreign keys giữa collections

---

## ✅ Workflow

```
Tạo schema file → Thêm vào PocketBaseSetup → Run app → Done! ✨
```
