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
     * Full setup: Admin auth + Delete old collection + Create with schema + Seed data
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

            // 3. Check if collection exists - if yes, DELETE it to recreate with proper schema
            val collectionExists = PocketBaseApi.collectionExists(PocketBaseConfig.Collections.FEATURES)
            println("📋 Collection 'features' exists: $collectionExists")

            if (collectionExists) {
                println("🗑️ Deleting existing collection to recreate with proper schema...")
                deleteCollection(PocketBaseConfig.Collections.FEATURES)
            }

            // 4. Create collection WITH schema from the start (like JS SDK example)
            println("📦 Creating 'features' collection with schema...")
            val collectionId = createFeaturesCollectionWithSchema()

            if (collectionId == null) {
                println("❌ Failed to create collection")
                return Result.failure(Exception("Failed to create collection"))
            }

            println("✅ Collection created with id: $collectionId")

            // 5. Verify schema was applied
            verifySchema(collectionId)

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
     * Delete collection by name
     * Uses adminToken for authorization
     */
    private suspend fun deleteCollection(name: String) {
        val client = PocketBaseClient.getClient()
        try {
            val response: HttpResponse = client.delete("/api/collections/$name") {
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }

            if (response.status.isSuccess()) {
                println("✅ Deleted collection '$name'")
            } else {
                println("⚠️ Failed to delete collection: ${response.status}")
            }
        } catch (e: Exception) {
            println("⚠️ Error deleting collection: ${e.message}")
        }
    }

    /**
     * Create features collection WITH schema in one call
     * Matches PocketBase JS SDK approach: pb.collections.create({ name, type, schema })
     */
    private suspend fun createFeaturesCollectionWithSchema(): String? {
        val client = PocketBaseClient.getClient()

        // Build schema array (like JS SDK example)
        val schema = buildJsonArray {
            // icon field - text
            add(buildJsonObject {
                put("name", "icon")
                put("type", "text")
                put("required", true)
                put("options", buildJsonObject {
                    put("min", JsonNull)
                    put("max", 50)
                    put("pattern", "")
                })
            })
            // title field - translation key
            add(buildJsonObject {
                put("name", "title")
                put("type", "text")
                put("required", true)
                put("options", buildJsonObject {
                    put("min", JsonNull)
                    put("max", 100)
                    put("pattern", "")
                })
            })
            // description field - translation key
            add(buildJsonObject {
                put("name", "description")
                put("type", "text")
                put("required", true)
                put("options", buildJsonObject {
                    put("min", JsonNull)
                    put("max", 200)
                    put("pattern", "")
                })
            })
            // order field - number
            add(buildJsonObject {
                put("name", "order")
                put("type", "number")
                put("required", true)
                put("options", buildJsonObject {
                    put("min", JsonNull)
                    put("max", JsonNull)
                    put("noDecimal", false)
                })
            })
            // active field - bool
            add(buildJsonObject {
                put("name", "active")
                put("type", "bool")
                put("required", false)
                put("options", buildJsonObject {})
            })
        }

        // Create collection with schema (like JS SDK)
        val createBody = buildJsonObject {
            put("name", "features")
            put("type", "base")
            put("schema", schema)
            put("listRule", "")   // Public read
            put("viewRule", "")   // Public read
            put("createRule", JsonNull)  // No public create
            put("updateRule", JsonNull)  // No public update
            put("deleteRule", JsonNull)  // No public delete
        }

        println("📦 Creating collection with schema...")
        println("📦 Schema has ${schema.size} fields: icon, title, description, order, active")

        val response: HttpResponse = client.post("/api/collections") {
            contentType(ContentType.Application.Json)
            PocketBaseClient.adminToken?.let { header("Authorization", it) }
            setBody(createBody)
        }

        val responseBody = response.bodyAsText()
        println("📦 Response status: ${response.status}")
        println("📦 Response body: $responseBody")

        if (!response.status.isSuccess()) {
            println("❌ Failed to create collection: ${response.status}")
            return null
        }

        // Parse response to get collection id
        val json = Json.parseToJsonElement(responseBody).jsonObject
        val collectionId = json["id"]?.jsonPrimitive?.content

        return collectionId
    }

    /**
     * Verify schema was applied correctly
     */
    private suspend fun verifySchema(collectionId: String) {
        val client = PocketBaseClient.getClient()
        try {
            val response: HttpResponse = client.get("/api/collections/$collectionId") {
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }

            if (response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                val json = Json.parseToJsonElement(responseBody).jsonObject
                val schema = json["schema"]?.jsonArray

                println("✅ Schema verification:")
                println("   Total fields: ${schema?.size ?: 0}")
                schema?.forEach { field ->
                    val fieldName = field.jsonObject["name"]?.jsonPrimitive?.content
                    val fieldType = field.jsonObject["type"]?.jsonPrimitive?.content
                    val required = field.jsonObject["required"]?.jsonPrimitive?.booleanOrNull
                    println("   ✓ $fieldName ($fieldType) - required: $required")
                }
            }
        } catch (e: Exception) {
            println("⚠️ Schema verification error: ${e.message}")
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
