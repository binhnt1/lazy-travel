package com.lazytravel.data.setup

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.remote.PocketBaseConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay

/**
 * Setup Features Collection on PocketBase Server
 * Creates collection and seeds production data
 */
object SetupFeaturesCollection {

    /**
     * Full setup: Admin auth + Create collection + Seed data
     */
    suspend fun setup(): Result<String> {
        return try {
            println("🚀 Starting Features Collection setup...")

            // 1. Initialize client
            PocketBaseClient.initialize()
            println("✅ PocketBase client initialized")

            // 2. Admin authentication
            println("🔐 Authenticating admin...")
            val authResult = PocketBaseApi.adminAuth(
                PocketBaseConfig.Admin.EMAIL,
                PocketBaseConfig.Admin.PASSWORD
            )

            if (authResult.isFailure) {
                return Result.failure(Exception("Admin auth failed: ${authResult.exceptionOrNull()?.message}"))
            }
            println("✅ Admin authenticated")

            // 3. Check if collection exists
            val collectionExists = PocketBaseApi.collectionExists(PocketBaseConfig.Collections.FEATURES)
            println("📋 Collection 'features' exists: $collectionExists")

            // 4. Create collection if not exists
            if (!collectionExists) {
                println("📦 Creating 'features' collection...")
                createFeaturesCollection()
                delay(1000) // Wait for collection to be created
            }

            // 5. Seed production data
            println("🌱 Seeding production features data...")
            seedFeaturesData()

            Result.success("✅ Features collection setup complete!")
        } catch (e: Exception) {
            println("❌ Setup failed: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Create features collection with schema
     */
    private suspend fun createFeaturesCollection() {
        val client = PocketBaseClient.getClient()

        val schema = listOf(
            mapOf(
                "name" to "icon",
                "type" to "text",
                "required" to true
            ),
            mapOf(
                "name" to "title_en",
                "type" to "text",
                "required" to true
            ),
            mapOf(
                "name" to "title_vi",
                "type" to "text",
                "required" to true
            ),
            mapOf(
                "name" to "description_en",
                "type" to "text",
                "required" to true
            ),
            mapOf(
                "name" to "description_vi",
                "type" to "text",
                "required" to true
            ),
            mapOf(
                "name" to "order",
                "type" to "number",
                "required" to true
            ),
            mapOf(
                "name" to "active",
                "type" to "bool",
                "required" to false
            )
        )

        val response: HttpResponse = client.post("/api/collections") {
            contentType(ContentType.Application.Json)
            PocketBaseClient.authToken?.let {
                header("Authorization", it)
            }
            setBody(mapOf(
                "name" to "features",
                "type" to "base",
                "schema" to schema
            ))
        }

        if (response.status.isSuccess()) {
            println("✅ Collection 'features' created successfully")
        } else {
            println("❌ Failed to create collection: ${response.status}")
        }
    }

    /**
     * Seed production features data
     */
    private suspend fun seedFeaturesData() {
        val client = PocketBaseClient.getClient()

        val features = listOf(
            mapOf(
                "icon" to "🗳️",
                "title_en" to "Democratic Voting",
                "title_vi" to "Vote dân chủ",
                "description_en" to "Everyone votes on destinations, hotels & activities",
                "description_vi" to "Mọi người vote điểm đến, khách sạn & hoạt động",
                "order" to 1,
                "active" to true
            ),
            mapOf(
                "icon" to "💰",
                "title_en" to "Smart Cost Splitting",
                "title_vi" to "Chia chi phí thông minh",
                "description_en" to "Auto-calculate and split expenses fairly",
                "description_vi" to "Tự động tính toán và chia chi phí công bằng",
                "order" to 2,
                "active" to true
            ),
            mapOf(
                "icon" to "📅",
                "title_en" to "AI Itinerary",
                "title_vi" to "Lịch trình AI",
                "description_en" to "Generate optimized day-by-day plans",
                "description_vi" to "Tạo kế hoạch tối ưu theo từng ngày",
                "order" to 3,
                "active" to true
            ),
            mapOf(
                "icon" to "📸",
                "title_en" to "Shared Album",
                "title_vi" to "Album chung",
                "description_en" to "Save and share photos with group",
                "description_vi" to "Lưu và chia sẻ ảnh cùng nhóm bạn",
                "order" to 4,
                "active" to true
            )
        )

        features.forEach { feature ->
            try {
                val response: HttpResponse = client.post("/api/collections/features/records") {
                    contentType(ContentType.Application.Json)
                    PocketBaseClient.authToken?.let {
                        header("Authorization", it)
                    }
                    setBody(feature)
                }

                if (response.status.isSuccess()) {
                    println("  ✅ Created: ${feature["title_en"]}")
                } else {
                    println("  ⚠️ Failed to create: ${feature["title_en"]} - ${response.status}")
                }

                delay(200) // Avoid rate limiting
            } catch (e: Exception) {
                println("  ❌ Error creating feature: ${e.message}")
            }
        }

        println("✅ Seeded ${features.size} features")
    }
}
