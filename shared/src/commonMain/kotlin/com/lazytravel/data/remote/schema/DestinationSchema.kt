package com.lazytravel.data.remote.schema

/**
 * Schema Definition for Destinations Collection
 *
 * This is an example schema that matches the Destination model.
 * You can create similar schema files for other collections.
 *
 * Usage:
 * ```
 * lifecycleScope.launch {
 *     SchemaMigration.migrate(destinationsSchema)
 * }
 * ```
 */

/**
 * Destinations Collection Schema
 * Matches: com.lazytravel.domain.model.Destination
 */
val destinationsSchema = collectionSchema {
    name = "destinations"
    type = CollectionType.BASE

    // Define all fields
    fields {
        // Name - Required text field
        text("name") {
            required = true
            min = 2
            max = 200
        }

        // Description - Rich text editor
        editor("description") {
            required = true
        }

        // Image URL
        url("imageUrl") {
            required = false
        }

        // Rating - Number from 0 to 5
        number("rating") {
            required = false
            min = 0.0
            max = 5.0
        }

        // Price - Number (VND)
        number("price") {
            required = true
            min = 0.0
        }

        // Location - Text field
        text("location") {
            required = true
            max = 200
        }

        // Category - Dropdown select
        select("category") {
            required = true
            values = listOf(
                "Beach",
                "Mountain",
                "Cultural",
                "Nature",
                "Adventure",
                "Urban",
                "Historical"
            )
            maxSelect = 1  // Single select
        }
    }

    // Create indexes for better query performance
    indexes {
        index("name")           // Index on name for search
        index("category")       // Index on category for filtering
        index("rating")         // Index on rating for sorting
    }

    // API Rules (optional)
    // null = public access, "" = auth required, specific rule = custom condition

    listRule = null      // Anyone can list destinations
    viewRule = null      // Anyone can view destination details
    createRule = ""      // Only authenticated users can create
    updateRule = ""      // Only authenticated users can update
    deleteRule = ""      // Only authenticated users can delete
}

/**
 * Example: Hotels Schema (placeholder for future implementation)
 *
 * Uncomment and customize when ready to implement hotels
 */
/*
val hotelsSchema = collectionSchema {
    name = "hotels"
    type = CollectionType.BASE

    fields {
        text("name") {
            required = true
            min = 2
            max = 200
        }

        text("address") {
            required = true
            max = 500
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

        // Relation to destinations
        relation("destination") {
            collectionId = "destinations"
            maxSelect = 1
            required = true
        }

        // Multiple images
        file("images") {
            maxSelect = 5
            maxSize = 5242880  // 5MB per file
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
                "Airport Shuttle"
            )
            maxSelect = 10  // Multiple select
        }

        bool("isAvailable") {
            required = true
        }
    }

    indexes {
        index("name")
        index("stars")
        compositeIndex("destination", "stars")  // Composite index
    }

    listRule = null
    viewRule = null
    createRule = ""
    updateRule = ""
    deleteRule = ""
}
*/

/**
 * Example: Reviews Schema (placeholder for future implementation)
 *
 * Uncomment and customize when ready to implement reviews
 */
/*
val reviewsSchema = collectionSchema {
    name = "reviews"
    type = CollectionType.BASE

    fields {
        // Relation to destinations or hotels
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

        // Reviewer name
        text("reviewerName") {
            required = true
            max = 100
        }

        // Reviewer email
        email("reviewerEmail") {
            required = false
        }

        // Photos
        file("photos") {
            maxSelect = 3
            maxSize = 5242880
            mimeTypes = listOf("image/jpeg", "image/png")
        }

        // Is verified
        bool("isVerified") {
            required = false
        }
    }

    indexes {
        index("rating")
        compositeIndex("destination", "rating")
    }

    listRule = null
    viewRule = null
    createRule = ""
    updateRule = ""
    deleteRule = ""
}
*/
