package com.lazytravel.data.remote.schema

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.remote.PocketBaseConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Schema Migration Engine
 *
 * Syncs CollectionSchema definitions to PocketBase server.
 * Handles creating and updating collections with full schema definitions.
 */
object SchemaMigration {

    /**
     * Run migrations for multiple schemas
     */
    suspend fun migrate(vararg schemas: CollectionSchema): Boolean = withContext(Dispatchers.Default) {
        try {
            println("🚀 Starting schema migration...")

            // Authenticate as admin
            val authResult = PocketBaseApi.adminAuth(
                PocketBaseConfig.Admin.EMAIL,
                PocketBaseConfig.Admin.PASSWORD
            )

            if (authResult.isFailure) {
                println("❌ Admin auth failed: ${authResult.exceptionOrNull()?.message}")
                println("⚠️ Skipping schema migration. Please check admin credentials.")
                return@withContext false
            }

            println("✅ Admin authenticated")

            // Migrate each schema
            var successCount = 0
            var failCount = 0

            schemas.forEach { schema ->
                try {
                    val result = migrateSchema(schema)
                    if (result) {
                        successCount++
                    } else {
                        failCount++
                    }
                } catch (e: Exception) {
                    println("❌ Error migrating ${schema.name}: ${e.message}")
                    failCount++
                }
            }

            println("✅ Schema migration complete: $successCount succeeded, $failCount failed")
            return@withContext failCount == 0

        } catch (e: Exception) {
            println("❌ Schema migration failed: ${e.message}")
            return@withContext false
        }
    }

    /**
     * Migrate a single collection schema
     */
    private suspend fun migrateSchema(schema: CollectionSchema): Boolean {
        return try {
            // Validate schema
            if (schema.name.isEmpty()) {
                println("❌ Schema name cannot be empty")
                return false
            }

            val exists = PocketBaseApi.collectionExists(schema.name)

            if (exists) {
                println("🔄 Updating collection '${schema.name}'...")
                updateCollection(schema)
            } else {
                println("➕ Creating collection '${schema.name}'...")
                createCollection(schema)
            }

        } catch (e: Exception) {
            println("❌ Failed to migrate '${schema.name}': ${e.message}")
            false
        }
    }

    /**
     * Create new collection with schema
     */
    private suspend fun createCollection(schema: CollectionSchema): Boolean {
        return try {
            val client = PocketBaseClient.getClient()
            val payload = buildCollectionPayload(schema)

            val response: HttpResponse = client.post("/api/collections") {
                contentType(ContentType.Application.Json)
                PocketBaseClient.authToken?.let {
                    header("Authorization", it)
                }
                setBody(payload)
            }

            if (response.status.isSuccess()) {
                println("✅ Created collection '${schema.name}'")
                true
            } else {
                println("❌ Failed to create '${schema.name}': ${response.status}")
                println("   Response: ${response.bodyAsText()}")
                false
            }
        } catch (e: Exception) {
            println("❌ Error creating '${schema.name}': ${e.message}")
            false
        }
    }

    /**
     * Update existing collection with schema
     */
    private suspend fun updateCollection(schema: CollectionSchema): Boolean {
        return try {
            val client = PocketBaseClient.getClient()
            val payload = buildCollectionPayload(schema)

            // Get collection ID first
            val collectionId = getCollectionId(schema.name)
            if (collectionId == null) {
                println("❌ Could not get ID for collection '${schema.name}'")
                return false
            }

            val response: HttpResponse = client.patch("/api/collections/$collectionId") {
                contentType(ContentType.Application.Json)
                PocketBaseClient.authToken?.let {
                    header("Authorization", it)
                }
                setBody(payload)
            }

            if (response.status.isSuccess()) {
                println("✅ Updated collection '${schema.name}'")
                true
            } else {
                println("❌ Failed to update '${schema.name}': ${response.status}")
                println("   Response: ${response.bodyAsText()}")
                false
            }
        } catch (e: Exception) {
            println("❌ Error updating '${schema.name}': ${e.message}")
            false
        }
    }

    /**
     * Get collection ID by name
     */
    private suspend fun getCollectionId(name: String): String? {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.get("/api/collections/$name") {
                PocketBaseClient.authToken?.let {
                    header("Authorization", it)
                }
            }

            if (response.status.isSuccess()) {
                val json = kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
                val jsonElement = json.parseToJsonElement(response.bodyAsText())
                jsonElement.jsonObject["id"]?.jsonPrimitive?.content
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Build PocketBase API payload from CollectionSchema
     */
    private fun buildCollectionPayload(schema: CollectionSchema): Map<String, Any> {
        val payload = mutableMapOf<String, Any>(
            "name" to schema.name,
            "type" to schema.type.value,
            "schema" to schema.schema.map { buildFieldPayload(it) }
        )

        // Add indexes if any
        if (schema.indexes.isNotEmpty()) {
            payload["indexes"] = schema.indexes
        }

        // Add API rules if defined
        schema.listRule?.let { payload["listRule"] = it }
        schema.viewRule?.let { payload["viewRule"] = it }
        schema.createRule?.let { payload["createRule"] = it }
        schema.updateRule?.let { payload["updateRule"] = it }
        schema.deleteRule?.let { payload["deleteRule"] = it }

        return payload
    }

    /**
     * Build field payload from FieldSchema
     */
    private fun buildFieldPayload(field: FieldSchema): Map<String, Any> {
        val payload = mutableMapOf<String, Any>(
            "name" to field.name,
            "type" to field.type.value,
            "required" to field.required
        )

        // Add options if any
        if (field.options.isNotEmpty()) {
            payload["options"] = field.options
        }

        return payload
    }

    /**
     * Utility: Print schema info for debugging
     */
    fun printSchema(schema: CollectionSchema) {
        println("\n📋 Schema: ${schema.name}")
        println("   Type: ${schema.type.value}")
        println("   Fields:")
        schema.schema.forEach { field ->
            val required = if (field.required) "*" else ""
            println("      - ${field.name}$required (${field.type.value})")
            if (field.options.isNotEmpty()) {
                println("        Options: ${field.options}")
            }
        }
        if (schema.indexes.isNotEmpty()) {
            println("   Indexes: ${schema.indexes.size}")
        }
        println()
    }
}

/**
 * Extension function to convert kotlinx.serialization JsonElement to JsonObject
 */
private val kotlinx.serialization.json.JsonElement.jsonObject: kotlinx.serialization.json.JsonObject
    get() = this as kotlinx.serialization.json.JsonObject

/**
 * Extension function to convert kotlinx.serialization JsonElement to JsonPrimitive
 */
private val kotlinx.serialization.json.JsonElement.jsonPrimitive: kotlinx.serialization.json.JsonPrimitive
    get() = this as kotlinx.serialization.json.JsonPrimitive
