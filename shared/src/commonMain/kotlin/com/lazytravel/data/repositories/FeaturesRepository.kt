package com.lazytravel.data.repositories

import com.lazytravel.data.models.Feature
import com.lazytravel.data.models.PocketBaseListResponse
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.remote.PocketBaseConfig
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

/**
 * Features Repository
 * Fetches features from PocketBase "features" collection
 * NO FALLBACK - Real data only from database
 */
class FeaturesRepository {

    private val json = Json { ignoreUnknownKeys = true }

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

            val httpResponse: HttpResponse = client.get(
                "/api/collections/${PocketBaseConfig.Collections.FEATURES}/records"
            ) {
                parameter("perPage", 100)
                parameter("filter", "active=true")
                parameter("sort", "+order")
            }

            // Log raw response for debugging
            val rawBody = httpResponse.bodyAsText()
            println("📡 Response status: ${httpResponse.status}")
            println("📡 Raw response body: $rawBody")

            // Parse response manually after logging
            val response: PocketBaseListResponse<Feature> = json.decodeFromString(rawBody)

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
