package com.lazytravel.data.repositories

import com.lazytravel.data.base.PocketBaseListResponse
import com.lazytravel.data.models.Feature
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.remote.PocketBaseConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

class FeaturesRepository {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getFeatures(): Result<List<Feature>> {
        return try {
            val client = PocketBaseClient.getClient()
            val httpResponse: HttpResponse = client.get(
                "/api/collections/${PocketBaseConfig.Collections.FEATURES}/records"
            ) {
                parameter("perPage", 100)
                parameter("filter", "active=true")
                parameter("sort", "+order")
            }

            // Log raw response for debugging
            val rawBody = httpResponse.bodyAsText()
            val response: PocketBaseListResponse<Feature> = json.decodeFromString(rawBody)
            Result.success(response.items)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
