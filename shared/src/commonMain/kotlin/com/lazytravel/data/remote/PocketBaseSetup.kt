package com.lazytravel.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Auto Collection Setup Helper
 * Automatically creates collections if they don't exist
 */
object PocketBaseSetup {

    /**
     * List of all collections needed by the app
     * Add new collection names here when creating new models
     */
    private val requiredCollections = listOf(
        PocketBaseConfig.Collections.DESTINATIONS,
        PocketBaseConfig.Collections.HOTELS,
        PocketBaseConfig.Collections.REVIEWS
    )

    /**
     * Ensure all required collections exist
     * Creates them if they don't exist
     */
    suspend fun ensureCollectionsExist() = withContext(Dispatchers.Default) {
        try {
            println("🔍 Checking PocketBase collections...")

            // Login as admin
            val authResult = PocketBaseApi.adminAuth(
                PocketBaseConfig.Admin.EMAIL,
                PocketBaseConfig.Admin.PASSWORD
            )

            if (authResult.isFailure) {
                println("⚠️ Admin auth failed: ${authResult.exceptionOrNull()?.message}")
                println("⚠️ Skipping auto-collection setup. Please create collections manually.")
                return@withContext
            }

            println("✅ Admin authenticated")

            // Check and create each collection
            requiredCollections.forEach { collectionName ->
                try {
                    val exists = PocketBaseApi.collectionExists(collectionName)

                    if (exists) {
                        println("✅ Collection '$collectionName' already exists")
                    } else {
                        // Create collection
                        val createResult = PocketBaseApi.createCollection(collectionName)
                        if (createResult.isSuccess) {
                            println("✅ Created collection '$collectionName'")
                        } else {
                            println("❌ Failed to create collection '$collectionName': ${createResult.exceptionOrNull()?.message}")
                        }
                    }
                } catch (e: Exception) {
                    println("❌ Error checking collection '$collectionName': ${e.message}")
                }
            }

            println("✅ Collection setup complete!")

        } catch (e: Exception) {
            println("❌ Collection setup failed: ${e.message}")
            println("💡 You may need to create collections manually in PocketBase Admin UI")
        }
    }

    /**
     * Create a single collection programmatically
     */
    suspend fun createCollection(name: String): Boolean {
        return try {
            val exists = PocketBaseApi.collectionExists(name)
            if (exists) {
                println("ℹ️ Collection '$name' already exists")
                return true
            }

            val result = PocketBaseApi.createCollection(name)
            if (result.isSuccess) {
                println("✅ Created collection '$name'")
                true
            } else {
                println("❌ Failed to create collection '$name'")
                false
            }
        } catch (e: Exception) {
            println("❌ Error creating collection '$name': ${e.message}")
            false
        }
    }
}
