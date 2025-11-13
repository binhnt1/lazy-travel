package com.lazytravel.data.remote.schema

/**
 * User & Profile Related Schemas
 *
 * Note: The "users" collection is a PocketBase AUTH collection and must be created
 * separately through the PocketBase admin dashboard. It handles authentication automatically.
 *
 * These schemas extend user functionality with profiles, stats, and gamification.
 */

/**
 * User Profiles - Extended user information
 * Relation: 1-to-1 with users (auth collection)
 */
val userProfilesSchema = collectionSchema {
    name = "user_profiles"
    type = CollectionType.BASE

    fields {
        // Link to auth user
        relation("user") {
            collectionId = "users"  // PocketBase auth collection
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        // Profile info
        editor("bio") {
            required = false
        }

        text("location") {
            required = false
            max = 200
        }

        number("age") {
            required = false
            min = 13.0
            max = 120.0
            onlyInt = true
        }

        select("gender") {
            values = listOf("male", "female", "other", "prefer_not_to_say")
            maxSelect = 1
            required = false
        }

        // Travel preferences (JSON array: ["Beach", "Mountain", "Cultural"])
        json("travel_preferences") {
            required = false
        }

        // Languages (JSON array: ["vi", "en", "ja"])
        json("languages") {
            required = false
        }

        text("phone_number") {
            required = false
            max = 20
        }

        // Gamification fields
        select("level") {
            values = listOf("Bronze", "Silver", "Gold", "Platinum", "Diamond")
            maxSelect = 1
            required = true
        }

        number("points") {
            required = true
            min = 0.0
            onlyInt = true
        }

        bool("verified") {
            required = true
        }
    }

    indexes {
        uniqueIndex("user")  // One profile per user
        index("level")
        index("points")
    }

    // API Rules
    listRule = null  // Public - anyone can see profiles
    viewRule = null  // Public
    createRule = "@request.auth.id != ''"  // Auth required
    updateRule = "@request.auth.id = user.id"  // Only owner
    deleteRule = "@request.auth.id = user.id"  // Only owner
}

/**
 * User Stats - Travel passport statistics
 * Relation: 1-to-1 with users
 */
val userStatsSchema = collectionSchema {
    name = "user_stats"
    type = CollectionType.BASE

    fields {
        relation("user") {
            collectionId = "users"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        number("countries_visited") {
            required = true
            min = 0.0
            onlyInt = true
        }

        number("cities_visited") {
            required = true
            min = 0.0
            onlyInt = true
        }

        number("trips_completed") {
            required = true
            min = 0.0
            onlyInt = true
        }

        number("photos_uploaded") {
            required = true
            min = 0.0
            onlyInt = true
        }

        number("total_distance_km") {
            required = true
            min = 0.0
        }

        date("last_trip_date") {
            required = false
        }
    }

    indexes {
        uniqueIndex("user")
        index("trips_completed")
        index("countries_visited")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id = user.id"
    deleteRule = "@request.auth.id = user.id"
}

/**
 * Achievements - Definition of badges/achievements
 * Master data table
 */
val achievementsSchema = collectionSchema {
    name = "achievements"
    type = CollectionType.BASE

    fields {
        text("name") {
            required = true
            min = 2
            max = 100
        }

        text("icon") {
            required = true
            max = 10  // Emoji
        }

        text("description") {
            required = true
            max = 500
        }

        select("requirement_type") {
            values = listOf(
                "trips_count",
                "destinations_visited",
                "photos_uploaded",
                "countries_visited",
                "distance_traveled",
                "years_active",
                "reviews_written",
                "friends_count"
            )
            maxSelect = 1
            required = true
        }

        number("requirement_value") {
            required = true
            min = 1.0
        }

        number("points") {
            required = true
            min = 0.0
            onlyInt = true
        }

        bool("is_active") {
            required = true
        }
    }

    indexes {
        index("requirement_type")
        index("is_active")
    }

    listRule = null  // Public
    viewRule = null
    createRule = ""  // Admin only (must be authenticated)
    updateRule = ""  // Admin only
    deleteRule = ""  // Admin only
}

/**
 * User Achievements - Which achievements users have unlocked
 * Relation: Many-to-many between users and achievements
 */
val userAchievementsSchema = collectionSchema {
    name = "user_achievements"
    type = CollectionType.BASE

    fields {
        relation("user") {
            collectionId = "users"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        relation("achievement") {
            collectionId = "achievements"
            maxSelect = 1
            required = true
        }

        date("unlocked_at") {
            required = true
        }
    }

    indexes {
        compositeIndex("user", "achievement")  // Unique combination
        index("unlocked_at")
    }

    listRule = null  // Public
    viewRule = null
    createRule = ""  // System only
    updateRule = ""  // No updates allowed
    deleteRule = "@request.auth.id = user.id"  // Owner can remove
}
