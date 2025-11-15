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

            // 4. Create collection if not exists and get collection ID
            val collectionId: String? = if (!collectionExists) {
                println("📦 Creating 'features' collection...")
                createFeaturesCollection()
            } else {
                println("📋 Collection 'features' already exists, getting id...")
                getCollectionIdByName(PocketBaseConfig.Collections.FEATURES)
            }

            if (collectionId == null) {
                println("❌ Failed to get collection id")
                return Result.failure(Exception("Failed to get collection id"))
            }

            println("✅ Working with collection id: $collectionId")

            // 5. Update schema and rules (always update to ensure correct structure)
            println("🔧 Updating schema and public access rules...")
            updateFeaturesSchema(collectionId)

            // 6. Seed production data (only if collection is empty)
            println("🌱 Checking if data seeding is needed...")
            val needsSeeding = checkIfSeedingNeeded()
            if (needsSeeding) {
                println("🌱 Seeding production features data...")
                seedFeaturesData()
            } else {
                println("✅ Collection already has data, skipping seeding")
            }

            Result.success("✅ Features collection setup complete!")
        } catch (e: Exception) {
            println("❌ Setup failed: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Get collection ID by name using GET /api/collections/{name}
     * Returns null if not found
     */
    private suspend fun getCollectionIdByName(name: String): String? {
        return try {
            val client = PocketBaseClient.getClient()
            println("🔍 Getting collection id for: $name")

            val response: HttpResponse = client.get("/api/collections/$name") {
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }

            if (response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                val json = Json.parseToJsonElement(responseBody).jsonObject
                val id = json["id"]?.jsonPrimitive?.content
                println("🔍 Collection '$name' id: $id")
                id
            } else {
                println("❌ Failed to get collection: ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("❌ Error getting collection id: ${e.message}")
            null
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

        println("📦 Creating collection with body: $createBody")
        println("📦 Admin token: ${PocketBaseClient.adminToken?.take(20)}...")

        val response: HttpResponse = client.post("/api/collections") {
            contentType(ContentType.Application.Json)
            // Use adminToken for creating collection
            PocketBaseClient.adminToken?.let {
                header("Authorization", it)
                println("📦 Authorization header added")
            } ?: println("⚠️ No admin token available!")
            setBody(createBody)
        }

        println("📦 Create collection response status: ${response.status}")
        val responseBody = response.bodyAsText()
        println("📦 Create collection response body: $responseBody")

        if (!response.status.isSuccess()) {
            println("❌ Failed to create collection: ${response.status}")
            return null
        }

        // Parse JSON response to get collection id
        val json = Json.parseToJsonElement(responseBody).jsonObject
        val collectionId = json["id"]?.jsonPrimitive?.content

        println("✅ Collection 'features' created with id: $collectionId")
        return collectionId
    }

    /**
     * Update schema of features collection
     * Uses adminToken for authorization
     *
     * PocketBase schema format requires specific field structure
     */
    private suspend fun updateFeaturesSchema(collectionId: String) {
        // Build schema using correct PocketBase format
        val schema = buildJsonArray {
            // icon field - text
            add(buildJsonObject {
                put("id", "text_icon")
                put("name", "icon")
                put("type", "text")
                put("required", true)
                put("presentable", false)
                put("unique", false)
                put("options", buildJsonObject {
                    put("min", JsonNull)
                    put("max", JsonNull)
                    put("pattern", "")
                })
            })
            // title field - translation key
            add(buildJsonObject {
                put("id", "text_title")
                put("name", "title")
                put("type", "text")
                put("required", true)
                put("presentable", true)
                put("unique", false)
                put("options", buildJsonObject {
                    put("min", JsonNull)
                    put("max", JsonNull)
                    put("pattern", "")
                })
            })
            // description field - translation key
            add(buildJsonObject {
                put("id", "text_desc")
                put("name", "description")
                put("type", "text")
                put("required", true)
                put("presentable", false)
                put("unique", false)
                put("options", buildJsonObject {
                    put("min", JsonNull)
                    put("max", JsonNull)
                    put("pattern", "")
                })
            })
            // order field - number
            add(buildJsonObject {
                put("id", "number_order")
                put("name", "order")
                put("type", "number")
                put("required", true)
                put("presentable", false)
                put("unique", false)
                put("options", buildJsonObject {
                    put("min", JsonNull)
                    put("max", JsonNull)
                    put("noDecimal", false)
                })
            })
            // active field - bool
            add(buildJsonObject {
                put("id", "bool_active")
                put("name", "active")
                put("type", "bool")
                put("required", false)
                put("presentable", false)
                put("unique", false)
                put("options", buildJsonObject {})
            })
        }

        // Update both schema AND list/view rules for public read access
        val updateBody = buildJsonObject {
            put("schema", schema)
            // Allow public read access (no auth required)
            put("listRule", "")   // Empty string = public access
            put("viewRule", "")   // Empty string = public access
        }

        println("🔧 Updating schema and rules for collection id: $collectionId")
        println("🔧 Schema JSON: $schema")

        val patchResponse: HttpResponse = PocketBaseClient.getClient().patch("/api/collections/$collectionId") {
            contentType(ContentType.Application.Json)
            // Use adminToken for updating schema
            PocketBaseClient.adminToken?.let { header("Authorization", it) }
            setBody(updateBody)
        }

        val patchBody = patchResponse.bodyAsText()
        println("🔧 Update response: ${patchResponse.status}")
        println("🔧 Response body: $patchBody")

        if (patchResponse.status.isSuccess()) {
            // Verify schema was applied
            val responseJson = Json.parseToJsonElement(patchBody).jsonObject
            val fields = responseJson["fields"]?.jsonArray
            println("✅ Schema updated - Fields count: ${fields?.size ?: 0}")
            if (fields != null && fields.size > 1) {
                println("✅ Schema and rules updated successfully!")
            } else {
                println("⚠️ Schema update may have failed - only ${fields?.size ?: 0} fields")
            }
        } else {
            println("❌ Failed to update schema: ${patchResponse.status}")
        }
    }

    /**
     * Check if seeding is needed by counting existing records
     * Also deletes existing records if found (cleanup old data without schema)
     * Returns true if collection is empty or was cleaned
     */
    private suspend fun checkIfSeedingNeeded(): Boolean {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.get("/api/collections/features/records") {
                parameter("perPage", 100)  // Get all records to delete
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }

            if (response.status.isSuccess()) {
                val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
                val totalItems = json["totalItems"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0
                println("🌱 Found $totalItems existing records")

                if (totalItems > 0) {
                    // Delete existing records (they might be incomplete/corrupt)
                    println("🗑️ Deleting existing records to reseed with proper schema...")
                    val items = json["items"]?.jsonArray ?: JsonArray(emptyList())
                    items.forEach { item ->
                        val recordId = item.jsonObject["id"]?.jsonPrimitive?.content
                        if (recordId != null) {
                            try {
                                client.delete("/api/collections/features/records/$recordId") {
                                    PocketBaseClient.adminToken?.let { header("Authorization", it) }
                                }
                                println("  🗑️ Deleted record: $recordId")
                            } catch (e: Exception) {
                                println("  ⚠️ Failed to delete record $recordId: ${e.message}")
                            }
                        }
                    }
                    println("✅ Cleaned up $totalItems old records")
                }

                true  // Always seed after cleanup
            } else {
                println("🌱 Could not check existing records, will attempt seeding")
                true
            }
        } catch (e: Exception) {
            println("🌱 Error checking records: ${e.message}, will attempt seeding")
            true
        }
    }

    /**
     * Seed production features data
     * Uses translation keys for title/description (not bilingual data)
     */
    private suspend fun seedFeaturesData() {
        val client = PocketBaseClient.getClient()

        // Data with translation keys (translated in app, not in DB)
        val features = listOf(
            buildJsonObject {
                put("icon", "🗳️")
                put("title", "feature_voting")  // Translation key
                put("description", "feature_voting_desc")  // Translation key
                put("order", 1)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "💰")
                put("title", "feature_cost_splitting")
                put("description", "feature_cost_splitting_desc")
                put("order", 2)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "📅")
                put("title", "feature_ai_itinerary")
                put("description", "feature_ai_itinerary_desc")
                put("order", 3)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "📸")
                put("title", "feature_shared_album")
                put("description", "feature_shared_album_desc")
                put("order", 4)
                put("active", true)
            }
        )

        features.forEach { feature ->
            try {
                val response: HttpResponse = client.post("/api/collections/features/records") {
                    contentType(ContentType.Application.Json)
                    // Use adminToken for creating records (admin has full permissions)
                    PocketBaseClient.adminToken?.let { header("Authorization", it) }
                    setBody(feature)
                }

                if (response.status.isSuccess()) {
                    val responseBody = response.bodyAsText()
                    println("  ✅ Created: ${feature["title"]}")
                    println("  📝 Response: $responseBody")
                } else {
                    println("  ⚠️ Failed to create: ${feature["title"]} - ${response.status}")
                }

                delay(200) // Avoid rate limiting
            } catch (e: Exception) {
                println("  ❌ Error creating feature: ${e.message}")
            }
        }

        println("✅ Seeded ${features.size} features")
    }
}
