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
 */
class FeaturesRepository {

    /**
     * Get all active features from PocketBase
     * Sorted by order field
     */
    suspend fun getFeatures(): Result<List<Feature>> {
        return try {
            val client = PocketBaseClient.getClient()

            val response: PocketBaseListResponse<Feature> = client.get(
                "/api/collections/${PocketBaseConfig.Collections.FEATURES}/records"
            ) {
                parameter("perPage", 100)
                parameter("filter", "active=true")
                parameter("sort", "+order")
            }.body()

            Result.success(response.items)
        } catch (e: Exception) {
            println("L Error fetching features: ${e.message}")
            e.printStackTrace()

            // Return fallback mock data if API fails
            Result.success(getFallbackFeatures())
        }
    }

    /**
     * Fallback features (mock data) if API fails
     */
    private fun getFallbackFeatures(): List<Feature> {
        return listOf(
            Feature(
                id = "1",
                icon = "=ó",
                titleEn = "Democratic Voting",
                titleVi = "Vote dân chç",
                descriptionEn = "Everyone votes on destinations, hotels & activities",
                descriptionVi = "MÍi ng°Ýi vote iÃm ¿n, khách s¡n & ho¡t Ùng",
                order = 1,
                active = true
            ),
            Feature(
                id = "2",
                icon = "=°",
                titleEn = "Smart Cost Splitting",
                titleVi = "Chia chi phí thông minh",
                descriptionEn = "Auto-calculate and split expenses fairly",
                descriptionVi = "Tñ Ùng tính toán và chia chi phí công b±ng",
                order = 2,
                active = true
            ),
            Feature(
                id = "3",
                icon = "=Å",
                titleEn = "AI Itinerary",
                titleVi = "LËch trình AI",
                descriptionEn = "Generate optimized day-by-day plans",
                descriptionVi = "T¡o k¿ ho¡ch tÑi °u theo tëng ngày",
                order = 3,
                active = true
            ),
            Feature(
                id = "4",
                icon = "=ø",
                titleEn = "Shared Album",
                titleVi = "Album chung",
                descriptionEn = "Save and share photos with group",
                descriptionVi = "L°u và chia s» £nh cùng nhóm b¡n",
                order = 4,
                active = true
            )
        )
    }
}
