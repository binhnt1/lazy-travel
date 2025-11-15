package com.lazytravel.data.remote.schema

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

/**
 * Schema Migration System
 * Automatically creates or updates collections with proper schemas
 */
object SchemaMigration {

    /**
     * Migrate multiple schemas
     * Returns true if all migrations succeeded
     */
    suspend fun migrate(vararg schemas: CollectionSchema): Boolean {
        var successCount = 0
        var failCount = 0

        schemas.forEach { schema ->
            try {
                val result = migrateCollection(schema)
                if (result) successCount++ else failCount++
            } catch (_: Exception) {
                failCount++
            }
        }
        return failCount == 0
    }

    /**
     * Migrate a single collection schema
     */
    private suspend fun migrateCollection(schema: CollectionSchema): Boolean {
        return try {
            val exists = PocketBaseApi.collectionExists(schema.name)

            if (exists) {
                updateCollectionSchema(schema)
            } else {
                createCollectionWithSchema(schema)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Create collection with schema
     */
    private suspend fun createCollectionWithSchema(schema: CollectionSchema): Boolean {
        val client = PocketBaseClient.getClient()

        // Convert schema to JSON
        val schemaJson = buildSchemaJson(schema)
        try {
            val response: HttpResponse = client.post("/api/collections") {
                contentType(ContentType.Application.Json)
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
                setBody(schemaJson.toString())
            }

            val success = response.status.isSuccess()
            return success
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Update existing collection schema
     */
    private suspend fun updateCollectionSchema(schema: CollectionSchema): Boolean {
        val client = PocketBaseClient.getClient()

        try {
            // Get current collection data
            val getResponse: HttpResponse = client.get("/api/collections/${schema.name}") {
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }

            if (!getResponse.status.isSuccess()) {
                return false
            }

            val currentData = Json.parseToJsonElement(getResponse.bodyAsText()).jsonObject
            val collectionId = currentData["id"]?.jsonPrimitive?.content ?: return false
            val updateJson = buildSchemaJson(schema)
            val updateResponse: HttpResponse = client.patch("/api/collections/$collectionId") {
                contentType(ContentType.Application.Json)
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
                setBody(updateJson.toString())
            }

            val success = updateResponse.status.isSuccess()
            return success
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Build JSON object for PocketBase API
     * Converts CollectionSchema to JSON manually to avoid serialization issues
     */
    private fun buildSchemaJson(schema: CollectionSchema): JsonObject {
        return buildJsonObject {
            put("name", schema.name)
            put("type", schema.type.value)

            // Convert fields to JSON array
            // PocketBase v0.23+ uses "fields" instead of "schema"
            put("fields", buildJsonArray {
                schema.schema.forEach { field ->
                    add(buildFieldJson(field))
                }
            })

            // API Rules
            if (schema.listRule != null) {
                put("listRule", schema.listRule)
            } else {
                put("listRule", JsonNull)
            }

            if (schema.viewRule != null) {
                put("viewRule", schema.viewRule)
            } else {
                put("viewRule", JsonNull)
            }

            if (schema.createRule != null) {
                put("createRule", schema.createRule)
            } else {
                put("createRule", JsonNull)
            }

            if (schema.updateRule != null) {
                put("updateRule", schema.updateRule)
            } else {
                put("updateRule", JsonNull)
            }

            if (schema.deleteRule != null) {
                put("deleteRule", schema.deleteRule)
            } else {
                put("deleteRule", JsonNull)
            }

            // Indexes (if any)
            if (schema.indexes.isNotEmpty()) {
                put("indexes", buildJsonArray {
                    schema.indexes.forEach { add(it) }
                })
            }
        }
    }

    /**
     * Build JSON for a single field
     * Manually converts field options to proper JSON types
     */
    private fun buildFieldJson(field: FieldSchema): JsonObject {
        return buildJsonObject {
            put("name", field.name)
            put("type", field.type.value)
            put("required", field.required)

            // Convert options to JSON object
            if (field.options.isNotEmpty()) {
                put("options", buildJsonObject {
                    field.options.forEach { (key, value) ->
                        when (value) {
                            is String -> put(key, value)
                            is Int -> put(key, value)
                            is Double -> put(key, value)
                            is Boolean -> put(key, value)
                            is List<*> -> {
                                put(key, buildJsonArray {
                                    value.forEach { item ->
                                        when (item) {
                                            is String -> add(item)
                                            is Number -> add(item.toDouble())
                                            is Boolean -> add(item)
                                            else -> add(item.toString())
                                        }
                                    }
                                })
                            }
                            else -> put(key, value.toString())
                        }
                    }
                })
            }
        }
    }
}