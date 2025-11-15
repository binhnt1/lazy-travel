package com.lazytravel.data.repositories

import com.lazytravel.data.models.Feature
import com.lazytravel.data.models.PocketBaseListResponse
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.remote.PocketBaseConfig
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * Features Repository
 * Fetches features from PocketBase "features" collection
 * NO FALLBACK - Real data only from database
 */
class FeaturesRepository {

    /**
     * Get all active features from PocketBase
     * Sorted by order field
     *
     * @return Result with list of features or error
     * NO FALLBACK DATA - If DB fails, app should handle error properly
     */
    suspend fun getFeatures(): Result<List<Feature>> {
        return try {
            val client = PocketBaseClient.getClient()

            println("📡 Fetching features from PocketBase...")

            val response: PocketBaseListResponse<Feature> = client.get(
                "/api/collections/${PocketBaseConfig.Collections.FEATURES}/records"
            ) {
                parameter("perPage", 100)
                parameter("filter", "active=true")
                parameter("sort", "+order")
            }.body()

            println("✅ Fetched ${response.items.size} features from database")
            Result.success(response.items)
        } catch (e: Exception) {
            println("❌ Error fetching features from database: ${e.message}")
            e.printStackTrace()

            // NO FALLBACK - Return error to let app handle it properly
            Result.failure(e)
        }
    }
}
