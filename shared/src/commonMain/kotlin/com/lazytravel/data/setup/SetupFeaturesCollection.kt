package com.lazytravel.data.setup

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.data.remote.schema.SchemaMigration
import com.lazytravel.data.remote.schema.featuresSchema
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.*

/**
 * Setup Features Collection on PocketBase Server
 * Uses schema migration system for consistency
 */
object SetupFeaturesCollection {

    /**
     * Full setup: Admin auth + Schema migration + Seed data
     */
    suspend fun setup(recreate: Boolean = false): Result<String> {
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

            // 3. If recreate flag is true, delete existing collection
            if (recreate) {
                val exists = PocketBaseApi.collectionExists(PocketBaseConfig.Collections.FEATURES)
                if (exists) {
                    println("🗑️ Deleting existing collection to recreate...")
                    deleteCollection(PocketBaseConfig.Collections.FEATURES)
                    delay(500) // Wait for deletion to complete
                }
            }

            // 4. Run schema migration (will create or update collection)
            println("📦 Running schema migration for 'features' collection...")
            val migrationSuccess = SchemaMigration.migrate(featuresSchema)

            if (!migrationSuccess) {
                return Result.failure(Exception("Schema migration failed"))
            }
            println("✅ Schema migration successful")

            // 5. Get collection ID for verification
            val collectionId = PocketBaseApi.getCollectionId(PocketBaseConfig.Collections.FEATURES)
            if (collectionId != null) {
                println("✅ Collection ID: $collectionId")
                verifySchema(collectionId)
            }

            // 6. Clear old records (if not recreated)
            if (!recreate) {
                println("🧹 Clearing old records...")
                clearAllRecords()
            }

            // 7. Seed production data
            println("🌱 Seeding production features data...")
            seedFeaturesData()

            // 8. Verify a seeded record has all fields
            println("🔍 Verifying seeded records...")
            verifySeededRecord()

            Result.success("✅ Features collection setup complete!")
        } catch (e: Exception) {
            println("❌ Setup failed: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Delete collection by name
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
     * Clear all records from features collection
     */
    private suspend fun clearAllRecords() {
        val client = PocketBaseClient.getClient()
        try {
            // Fetch all record IDs
            val response: HttpResponse = client.get("/api/collections/features/records") {
                parameter("perPage", 500)
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }

            if (response.status.isSuccess()) {
                val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
                val items = json["items"]?.jsonArray ?: return

                println("🧹 Deleting ${items.size} old records...")

                items.forEach { item ->
                    val recordId = item.jsonObject["id"]?.jsonPrimitive?.content
                    if (recordId != null) {
                        try {
                            client.delete("/api/collections/features/records/$recordId") {
                                PocketBaseClient.adminToken?.let { header("Authorization", it) }
                            }
                        } catch (e: Exception) {
                            println("⚠️ Failed to delete record $recordId")
                        }
                    }
                }

                println("✅ Cleared all old records")
                delay(500) // Wait for deletions to complete
            }
        } catch (e: Exception) {
            println("⚠️ Error clearing records: ${e.message}")
        }
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
     * Verify seeded records have all custom fields
     */
    private suspend fun verifySeededRecord() {
        val client = PocketBaseClient.getClient()
        try {
            // Fetch first record
            val response: HttpResponse = client.get("/api/collections/features/records") {
                parameter("perPage", 1)
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }

            if (response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                println("🔍 First record response:")
                println("   $responseBody")

                val json = Json.parseToJsonElement(responseBody).jsonObject
                val items = json["items"]?.jsonArray
                if (items != null && items.isNotEmpty()) {
                    val firstRecord = items[0].jsonObject
                    println("🔍 First record fields:")
                    firstRecord.keys.forEach { key ->
                        val value = firstRecord[key]
                        println("   ✓ $key: $value")
                    }

                    // Check for our custom fields
                    val requiredFields = listOf("icon", "title", "description", "order", "active")
                    val missingFields = requiredFields.filter { !firstRecord.containsKey(it) }

                    if (missingFields.isEmpty()) {
                        println("✅ Record has ALL custom fields!")
                    } else {
                        println("❌ Record MISSING fields:")
                        missingFields.forEach { println("   ❌ $it") }
                    }
                } else {
                    println("⚠️ No records found")
                }
            } else {
                println("⚠️ Failed to fetch records: ${response.status}")
            }
        } catch (e: Exception) {
            println("⚠️ Verify seeded record error: ${e.message}")
            e.printStackTrace()
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