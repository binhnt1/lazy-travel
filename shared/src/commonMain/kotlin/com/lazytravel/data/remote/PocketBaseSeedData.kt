package com.lazytravel.data.remote

import com.lazytravel.domain.model.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Sample Data Seeder for PocketBase
 * Helps populate initial test data for development
 */
object PocketBaseSeedData {

    /**
     * Seed sample destinations into PocketBase
     */
    suspend fun seedDestinations() = withContext(Dispatchers.Default) {
        try {
            println("🌱 Seeding sample destinations...")

            val sampleDestinations = listOf(
                Destination(
                    name = "Ha Long Bay",
                    description = "UNESCO World Heritage Site featuring thousands of limestone islands and islets",
                    imageUrl = "https://images.unsplash.com/photo-1528127269322-539801943592",
                    rating = 4.8,
                    price = 1200000.0,
                    location = "Quang Ninh, Vietnam",
                    category = "Nature"
                ),
                Destination(
                    name = "Hoi An Ancient Town",
                    description = "Well-preserved ancient town with unique architecture and vibrant culture",
                    imageUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482",
                    rating = 4.7,
                    price = 800000.0,
                    location = "Quang Nam, Vietnam",
                    category = "Cultural"
                ),
                Destination(
                    name = "Sapa Terraced Fields",
                    description = "Stunning rice terraces and ethnic minority villages in the mountains",
                    imageUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b",
                    rating = 4.6,
                    price = 1500000.0,
                    location = "Lao Cai, Vietnam",
                    category = "Adventure"
                ),
                Destination(
                    name = "Phu Quoc Island",
                    description = "Tropical paradise with pristine beaches and crystal clear waters",
                    imageUrl = "https://images.unsplash.com/photo-1552465011-b4e21bf6e79a",
                    rating = 4.5,
                    price = 2000000.0,
                    location = "Kien Giang, Vietnam",
                    category = "Beach"
                ),
                Destination(
                    name = "Da Lat City",
                    description = "Cool mountain city known for flowers, coffee, and French architecture",
                    imageUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482",
                    rating = 4.4,
                    price = 900000.0,
                    location = "Lam Dong, Vietnam",
                    category = "Mountain"
                )
            )

            var successCount = 0
            var failCount = 0

            for (destination in sampleDestinations) {
                val result = PocketBaseApi.createRecord(
                    PocketBaseConfig.Collections.DESTINATIONS,
                    destination
                )

                if (result.isSuccess) {
                    println("✅ Created: ${destination.name}")
                    successCount++
                } else {
                    println("❌ Failed to create: ${destination.name} - ${result.exceptionOrNull()?.message}")
                    failCount++
                }
            }

            println("🌱 Seeding complete! Success: $successCount, Failed: $failCount")

        } catch (e: Exception) {
            println("❌ Seeding failed: ${e.message}")
        }
    }

    /**
     * Clear all destinations (for testing)
     * WARNING: This will delete all destination records!
     */
    suspend fun clearDestinations() = withContext(Dispatchers.Default) {
        try {
            println("🗑️ Clearing all destinations...")

            // Get all destinations
            val result = PocketBaseApi.getRecords(
                collection = PocketBaseConfig.Collections.DESTINATIONS,
                perPage = 500
            )

            result.fold(
                onSuccess = { response ->
                    var deletedCount = 0
                    response.items.forEach { item ->
                        // Extract ID from JSON element
                        val id = item.toString().substringAfter("\"id\":\"").substringBefore("\"")
                        val deleteResult = PocketBaseApi.deleteRecord(
                            PocketBaseConfig.Collections.DESTINATIONS,
                            id
                        )
                        if (deleteResult.isSuccess) {
                            deletedCount++
                        }
                    }
                    println("🗑️ Deleted $deletedCount destinations")
                },
                onFailure = { error ->
                    println("❌ Failed to clear destinations: ${error.message}")
                }
            )

        } catch (e: Exception) {
            println("❌ Clear operation failed: ${e.message}")
        }
    }

    /**
     * Check PocketBase connection
     */
    suspend fun testConnection(): Boolean {
        return try {
            println("🔍 Testing PocketBase connection...")
            val exists = PocketBaseApi.collectionExists(PocketBaseConfig.Collections.DESTINATIONS)
            if (exists) {
                println("✅ PocketBase connection successful!")
                println("✅ Collection '${PocketBaseConfig.Collections.DESTINATIONS}' exists")
            } else {
                println("⚠️ Collection '${PocketBaseConfig.Collections.DESTINATIONS}' not found")
                println("💡 Run PocketBaseSetup.ensureCollectionsExist() to create it")
            }
            true
        } catch (e: Exception) {
            println("❌ PocketBase connection failed: ${e.message}")
            println("💡 Check if PocketBase is running at: ${PocketBaseConfig.BASE_URL}")
            false
        }
    }
}
