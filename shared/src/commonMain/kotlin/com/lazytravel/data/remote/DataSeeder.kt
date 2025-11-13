package com.lazytravel.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Data Seeder - Easy-to-use utility for populating PocketBase
 *
 * Usage in your app:
 * ```kotlin
 * // In a Composable or ViewModel
 * LaunchedEffect(Unit) {
 *     DataSeeder.seedIfEmpty()
 * }
 *
 * // Or manually trigger
 * Button(onClick = {
 *     scope.launch {
 *         DataSeeder.seedAll()
 *     }
 * }) {
 *     Text("Seed Data")
 * }
 * ```
 */
object DataSeeder {

    /**
     * Check if database is empty and seed if needed
     * Safe to call on app startup
     */
    suspend fun seedIfEmpty(): SeedResult = withContext(Dispatchers.Default) {
        try {
            println("🔍 Checking if database needs seeding...")

            // Check if destinations exist
            val destinationsResult = PocketBaseApi.getRecords(
                collection = PocketBaseConfig.Collections.DESTINATIONS,
                perPage = 1
            )

            val isEmpty = destinationsResult.fold(
                onSuccess = { response -> response.totalItems == 0 },
                onFailure = { true }
            )

            if (isEmpty) {
                println("📦 Database is empty, starting seeding...")
                seedAll()
            } else {
                println("✅ Database already has data, skipping seed")
                SeedResult.AlreadySeeded
            }

        } catch (e: Exception) {
            println("❌ Seed check failed: ${e.message}")
            SeedResult.Failed(e.message ?: "Unknown error")
        }
    }

    /**
     * Seed all data regardless of current state
     * Use this to refresh/reset all data
     */
    suspend fun seedAll(): SeedResult = withContext(Dispatchers.Default) {
        try {
            println("\n" + "=".repeat(60))
            println("🌱 LAZY TRAVEL - DATA SEEDING")
            println("=".repeat(60))

            // Ensure PocketBase is connected
            val connected = testConnection()
            if (!connected) {
                return@withContext SeedResult.Failed("Cannot connect to PocketBase")
            }

            // Ensure collections exist
            println("\n📋 Ensuring collections exist...")
            PocketBaseSetup.ensureCollectionsExist()

            // Seed all data
            CompleteSeedData.seedAll()

            println("\n" + "=".repeat(60))
            println("✅ SEEDING COMPLETE!")
            println("💡 You can now use the app with sample data")
            println("=".repeat(60) + "\n")

            SeedResult.Success

        } catch (e: Exception) {
            println("❌ Seeding failed: ${e.message}")
            e.printStackTrace()
            SeedResult.Failed(e.message ?: "Unknown error")
        }
    }

    /**
     * Seed specific collection only
     */
    suspend fun seedDestinations() = CompleteSeedData.seedDestinations()
    suspend fun seedTours() = CompleteSeedData.seedTours()
    suspend fun seedBlogCategories() = CompleteSeedData.seedBlogCategories()
    suspend fun seedBuddyRequests() = CompleteSeedData.seedBuddyRequests()
    suspend fun seedAchievements() = CompleteSeedData.seedAchievements()

    /**
     * Clear all data (DANGEROUS - use only in development!)
     */
    suspend fun clearAll(): SeedResult = withContext(Dispatchers.Default) {
        try {
            println("\n⚠️ WARNING: This will DELETE ALL DATA!")
            CompleteSeedData.clearAll()
            SeedResult.Success
        } catch (e: Exception) {
            println("❌ Clear failed: ${e.message}")
            SeedResult.Failed(e.message ?: "Unknown error")
        }
    }

    /**
     * Test PocketBase connection
     */
    suspend fun testConnection(): Boolean = withContext(Dispatchers.Default) {
        try {
            println("🔍 Testing PocketBase connection...")
            println("📍 URL: ${PocketBaseConfig.BASE_URL}")

            val exists = PocketBaseApi.collectionExists(PocketBaseConfig.Collections.DESTINATIONS)

            if (exists) {
                println("✅ Connected to PocketBase successfully!")
                true
            } else {
                println("⚠️ Connected but collections don't exist")
                println("💡 Run ensureCollectionsExist() to create them")
                true
            }
        } catch (e: Exception) {
            println("❌ Cannot connect to PocketBase: ${e.message}")
            println("💡 Make sure PocketBase is running at: ${PocketBaseConfig.BASE_URL}")
            false
        }
    }

    /**
     * Get database statistics
     */
    suspend fun getStats(): DatabaseStats = withContext(Dispatchers.Default) {
        val stats = DatabaseStats()

        try {
            // Count destinations
            PocketBaseApi.getRecords(
                collection = PocketBaseConfig.Collections.DESTINATIONS,
                perPage = 1
            ).fold(
                onSuccess = { stats.destinations = it.totalItems },
                onFailure = { }
            )

            // Count tours
            PocketBaseApi.getRecords(
                collection = PocketBaseConfig.Collections.TOURS,
                perPage = 1
            ).fold(
                onSuccess = { stats.tours = it.totalItems },
                onFailure = { }
            )

            // Count blog posts
            PocketBaseApi.getRecords(
                collection = PocketBaseConfig.Collections.BLOG_POSTS,
                perPage = 1
            ).fold(
                onSuccess = { stats.blogPosts = it.totalItems },
                onFailure = { }
            )

            // Count buddy requests
            PocketBaseApi.getRecords(
                collection = PocketBaseConfig.Collections.BUDDY_REQUESTS,
                perPage = 1
            ).fold(
                onSuccess = { stats.buddyRequests = it.totalItems },
                onFailure = { }
            )

            // Count achievements
            PocketBaseApi.getRecords(
                collection = PocketBaseConfig.Collections.ACHIEVEMENTS,
                perPage = 1
            ).fold(
                onSuccess = { stats.achievements = it.totalItems },
                onFailure = { }
            )

            println("\n📊 Database Statistics:")
            println("   Destinations: ${stats.destinations}")
            println("   Tours: ${stats.tours}")
            println("   Blog Posts: ${stats.blogPosts}")
            println("   Buddy Requests: ${stats.buddyRequests}")
            println("   Achievements: ${stats.achievements}")

        } catch (e: Exception) {
            println("❌ Failed to get stats: ${e.message}")
        }

        stats
    }
}

/**
 * Seed operation result
 */
sealed class SeedResult {
    object Success : SeedResult()
    object AlreadySeeded : SeedResult()
    data class Failed(val error: String) : SeedResult()
}

/**
 * Database statistics
 */
data class DatabaseStats(
    var destinations: Int = 0,
    var tours: Int = 0,
    var blogPosts: Int = 0,
    var buddyRequests: Int = 0,
    var achievements: Int = 0
) {
    val total: Int
        get() = destinations + tours + blogPosts + buddyRequests + achievements

    val isEmpty: Boolean
        get() = total == 0
}
