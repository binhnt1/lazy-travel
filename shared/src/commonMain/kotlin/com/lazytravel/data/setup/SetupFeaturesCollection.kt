package com.lazytravel.data.setup

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.remote.PocketBaseConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.*

/**
 * Setup Features Collection on PocketBase Server
 * Creates collection, updates schema, and seeds production data
 */
object SetupFeaturesCollection {

    /**
     * Full setup: Admin auth + Create collection + Update schema + Seed data
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
            val collectionId = if (!collectionExists) {
                println("📦 Creating 'features' collection...")
                createFeaturesCollection()
            } else {
                // Get collection id if exists
                PocketBaseApi.getCollectionId(PocketBaseConfig.Collections.FEATURES)
            }

            if (collectionId == null) {
                return Result.failure(Exception("Failed to get collection id"))
            }

            // 5. Update schema (if newly created)
            updateFeaturesSchema(collectionId)

            // 6. Seed production data
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
     * Create basic features collection, return generated collection id
     * Uses adminToken for authorization
     */
    private suspend fun createFeaturesCollection(): String? {
        val client = PocketBaseClient.getClient()

        val createBody = buildJsonObject {
            put("name", "features")
            put("type", "base")
        }

        val response: HttpResponse = client.post("/api/collections") {
            contentType(ContentType.Application.Json)
            // Use adminToken for creating collection
            PocketBaseClient.adminToken?.let { header("Authorization", it) }
            setBody(createBody)
        }

        if (!response.status.isSuccess()) {
            println("❌ Failed to create collection: ${response.status}")
            return null
        }

        // Parse JSON response to get collection id
        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val collectionId = json["id"]?.jsonPrimitive?.content

        println("✅ Collection 'features' created with id: $collectionId")
        return collectionId
    }

    /**
     * Update schema of features collection
     * Uses adminToken for authorization
     */
    private suspend fun updateFeaturesSchema(collectionId: String) {
        val schema = buildJsonArray {
            add(buildJsonObject { put("name", "icon"); put("type", "text"); put("required", true) })
            add(buildJsonObject { put("name", "title_en"); put("type", "text"); put("required", true) })
            add(buildJsonObject { put("name", "title_vi"); put("type", "text"); put("required", true) })
            add(buildJsonObject { put("name", "description_en"); put("type", "text"); put("required", true) })
            add(buildJsonObject { put("name", "description_vi"); put("type", "text"); put("required", true) })
            add(buildJsonObject { put("name", "order"); put("type", "number"); put("required", true) })
            add(buildJsonObject { put("name", "active"); put("type", "bool"); put("required", false) })
        }

        val updateBody = buildJsonObject { put("schema", schema) }

        val patchResponse: HttpResponse = PocketBaseClient.getClient().patch("/api/collections/$collectionId") {
            contentType(ContentType.Application.Json)
            // Use adminToken for updating schema
            PocketBaseClient.adminToken?.let { header("Authorization", it) }
            setBody(updateBody)
        }

        if (patchResponse.status.isSuccess()) {
            println("✅ Schema updated successfully for collection id: $collectionId")
        } else {
            println("❌ Failed to update schema: ${patchResponse.status}")
        }
    }

    /**
     * Seed production features data
     * Note: If collection has auth rules, you may need collectionToken
     * For now, using adminToken as it has full permissions
     */
    private suspend fun seedFeaturesData() {
        val client = PocketBaseClient.getClient()

        val features = listOf(
            buildJsonObject {
                put("icon", "🗳️")
                put("title_en", "Democratic Voting")
                put("title_vi", "Vote dân chủ")
                put("description_en", "Everyone votes on destinations, hotels & activities")
                put("description_vi", "Mọi người vote điểm đến, khách sạn & hoạt động")
                put("order", 1)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "💰")
                put("title_en", "Smart Cost Splitting")
                put("title_vi", "Chia chi phí thông minh")
                put("description_en", "Auto-calculate and split expenses fairly")
                put("description_vi", "Tự động tính toán và chia chi phí công bằng")
                put("order", 2)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "📅")
                put("title_en", "AI Itinerary")
                put("title_vi", "Lịch trình AI")
                put("description_en", "Generate optimized day-by-day plans")
                put("description_vi", "Tạo kế hoạch tối ưu theo từng ngày")
                put("order", 3)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "📸")
                put("title_en", "Shared Album")
                put("title_vi", "Album chung")
                put("description_en", "Save and share photos with group")
                put("description_vi", "Lưu và chia sẻ ảnh cùng nhóm bạn")
                put("order", 4)
                put("active", true)
            }
        )

        features.forEach { feature ->
            try {
                val response: HttpResponse = client.post("/api/collections/features/records") {
                    contentType(ContentType.Application.Json)
                    // Use adminToken for creating records (admin has full permissions)
                    // If collection had specific auth rules, we'd use collectionToken instead
                    PocketBaseClient.adminToken?.let { header("Authorization", it) }
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
