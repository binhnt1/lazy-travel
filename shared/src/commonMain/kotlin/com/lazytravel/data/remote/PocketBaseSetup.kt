package com.lazytravel.data.remote

import io.github.agrevster.pocketbaseKotlin.PocketbaseException
import io.github.agrevster.pocketbaseKotlin.dsl.collections.CollectionCreate
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
     *
     * Call this once during app startup (optional, but recommended for dev)
     */
    suspend fun ensureCollectionsExist() = withContext(Dispatchers.Default) {
        try {
            val client = PocketBaseClient.getInstance()

            println("🔍 Checking PocketBase collections...")

            // Login as admin (needed to create collections)
            try {
                client.admins.authWithPassword(
                    PocketBaseConfig.Admin.EMAIL,
                    PocketBaseConfig.Admin.PASSWORD
                )
                println("✅ Admin authenticated")
            } catch (e: Exception) {
                println("⚠️ Admin auth failed: ${e.message}")
                println("⚠️ Skipping auto-collection setup. Please create collections manually in Admin UI.")
                return@withContext
            }

            // Check and create each collection
            requiredCollections.forEach { collectionName ->
                try {
                    // Try to get collection (will throw if doesn't exist)
                    client.collections.getOne(collectionName)
                    println("✅ Collection '$collectionName' already exists")
                } catch (e: PocketbaseException) {
                    // Collection doesn't exist, create it
                    try {
                        client.collections.create(
                            CollectionCreate(
                                name = collectionName,
                                type = "base" // base collection type
                            )
                        )
                        println("✅ Created collection '$collectionName'")
                    } catch (createError: Exception) {
                        println("❌ Failed to create collection '$collectionName': ${createError.message}")
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
     * Useful when adding a new model at runtime
     */
    suspend fun createCollection(name: String): Boolean {
        return try {
            val client = PocketBaseClient.getInstance()

            // Try to get collection first
            try {
                client.collections.getOne(name)
                println("ℹ️ Collection '$name' already exists")
                return true
            } catch (e: PocketbaseException) {
                // Doesn't exist, create it
                client.collections.create(
                    CollectionCreate(
                        name = name,
                        type = "base"
                    )
                )
                println("✅ Created collection '$name'")
                true
            }
        } catch (e: Exception) {
            println("❌ Failed to create collection '$name': ${e.message}")
            false
        }
    }
}
