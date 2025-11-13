package com.lazytravel.data.remote.schema

/**
 * Tour Packages Schemas
 *
 * For pre-packaged tour offerings like "Phú Quốc 4N3Đ", "Sapa Trekking"
 */

/**
 * Tours - Pre-packaged tour offerings
 */
val toursSchema = collectionSchema {
    name = "tours"
    type = CollectionType.BASE

    fields {
        text("name") {
            required = true
            min = 2
            max = 200
        }

        relation("destination") {
            collectionId = "destinations"
            maxSelect = 1
            required = true
        }

        number("price_per_person") {
            required = true
            min = 0.0
        }

        number("duration_days") {
            required = true
            min = 1.0
            onlyInt = true
        }

        number("duration_nights") {
            required = true
            min = 0.0
            onlyInt = true
        }

        editor("description") {
            required = true
        }

        file("images") {
            maxSelect = 10
            maxSize = 5242880  // 5MB per image
            mimeTypes = listOf(
                "image/jpeg",
                "image/png",
                "image/webp"
            )
            thumbs = listOf(
                "100x100",
                "300x300",
                "600x600"
            )
        }

        select("badge") {
            values = listOf("hot", "new", "discount")
            maxSelect = 1
            required = false
        }

        number("discount_percent") {
            required = false
            min = 0.0
            max = 100.0
            onlyInt = true
        }

        number("rating") {
            required = true
            min = 0.0
            max = 5.0
        }

        number("total_reviews") {
            required = true
            min = 0.0
            onlyInt = true
        }

        // What's included (JSON array: ["Vé máy bay", "Khách sạn", "Ăn sáng"])
        json("includes") {
            required = false
        }

        // Day-by-day itinerary (JSON)
        json("itinerary") {
            required = false
        }

        number("available_slots") {
            required = true
            min = 0.0
            onlyInt = true
        }

        bool("is_active") {
            required = true
        }
    }

    indexes {
        index("destination")
        index("rating")
        index("price_per_person")
        index("is_active")
        compositeIndex("destination", "rating")
    }

    listRule = "is_active = true"
    viewRule = null
    createRule = ""  // Admin only
    updateRule = ""  // Admin only
    deleteRule = ""  // Admin only
}

/**
 * Tour Reviews - User reviews for tours
 */
val tourReviewsSchema = collectionSchema {
    name = "tour_reviews"
    type = CollectionType.BASE

    fields {
        relation("tour") {
            collectionId = "tours"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        relation("user") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }

        number("rating") {
            required = true
            min = 1.0
            max = 5.0
        }

        editor("review_text") {
            required = true
        }

        file("photos") {
            maxSelect = 5
            maxSize = 5242880
            mimeTypes = listOf(
                "image/jpeg",
                "image/png"
            )
        }

        number("helpful_count") {
            required = true
            min = 0.0
            onlyInt = true
        }

        bool("is_verified") {
            required = true
        }
    }

    indexes {
        index("tour")
        index("rating")
        compositeIndex("tour", "user")
        compositeIndex("tour", "rating")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id = user.id"
    deleteRule = "@request.auth.id = user.id"
}
