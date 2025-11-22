package com.lazytravel.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Auto Collection Setup Helper
 * Automatically creates collections if they don't exist
 */
object PocketBaseSetup {
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
