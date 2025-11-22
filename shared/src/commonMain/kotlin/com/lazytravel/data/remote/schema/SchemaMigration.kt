package com.lazytravel.data.remote.schema

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

object SchemaMigration {

    private val collectionIdCache = mutableMapOf<String, String>()

    suspend fun migrate(vararg schemas: CollectionSchema): Boolean {
        var successCount = 0
        var failCount = 0

        schemas.forEach { schema ->
            try {
                val result = migrateCollection(schema)
                if (result) successCount++ else failCount++
            } catch (e: Exception) {
                println("Error migrating collection ${schema.name}: ${e.message}")
                e.printStackTrace()
                failCount++
            }
        }
        return failCount == 0
    }

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

    private suspend fun createCollectionWithSchema(schema: CollectionSchema): Boolean {
        val client = PocketBaseClient.getClient()

        resolveRelationCollectionIds(schema)
        val schemaJson = buildSchemaJson(schema)
        try {
            val response: HttpResponse = client.post("/api/collections") {
                contentType(ContentType.Application.Json)
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
                setBody(schemaJson.toString())
            }

            val responseBody = response.bodyAsText()
            val success = response.status.isSuccess()

            if (!success) {
                println("❌ Failed to create ${schema.name}: ${response.status}")
                println("❌ Response: $responseBody")
            } else {
                println("✅ Created ${schema.name}")
                try {
                    val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject
                    val collectionId = jsonResponse["id"]?.jsonPrimitive?.content
                    if (collectionId != null) {
                        collectionIdCache[schema.name] = collectionId
                    }
                } catch (e: Exception) {
                    println("⚠️ Could not cache collection ID for ${schema.name}")
                }
            }

            return success
        } catch (e: Exception) {
            println("❌ Exception creating ${schema.name}: ${e.message}")
            e.printStackTrace()
            return false
        }
    }

    private suspend fun resolveRelationCollectionIds(schema: CollectionSchema) {
        schema.schema.forEach { field ->
            if (field.type == FieldType.RELATION) {
                val collectionName = field.options["collectionId"] as? String
                if (collectionName != null) {
                    val cachedId = collectionIdCache[collectionName]
                    if (cachedId != null) {
                        field.options["collectionId"] = cachedId
                        println("🔗 Resolved relation ${field.name}: $collectionName -> $cachedId")
                    } else {
                        val resolvedId = fetchCollectionId(collectionName)
                        if (resolvedId != null) {
                            field.options["collectionId"] = resolvedId
                            collectionIdCache[collectionName] = resolvedId
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchCollectionId(collectionName: String): String? {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.get("/api/collections/$collectionName") {
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }

            if (response.status.isSuccess()) {
                val jsonResponse = Json.parseToJsonElement(response.bodyAsText()).jsonObject
                jsonResponse["id"]?.jsonPrimitive?.content
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

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

    private fun buildSchemaJson(schema: CollectionSchema): JsonObject {
        return buildJsonObject {
            put("type", schema.type.value)
            put("name", schema.name)

            // API Rules - null values as JsonNull, empty string as empty string
            put("listRule", schema.listRule?.let { JsonPrimitive(it) } ?: JsonNull)
            put("viewRule", schema.viewRule?.let { JsonPrimitive(it) } ?: JsonNull)
            put("createRule", schema.createRule?.let { JsonPrimitive(it) } ?: JsonNull)
            put("updateRule", schema.updateRule?.let { JsonPrimitive(it) } ?: JsonNull)
            put("deleteRule", schema.deleteRule?.let { JsonPrimitive(it) } ?: JsonNull)
            put("fields", buildJsonArray {
                schema.schema.forEach { field ->
                    add(buildFieldJson(field))
                }
            })

            // Indexes (if any)
            if (schema.indexes.isNotEmpty()) {
                put("indexes", buildJsonArray {
                    schema.indexes.forEach { add(JsonPrimitive(it)) }
                })
            }
        }
    }

    private fun buildFieldJson(field: FieldSchema): JsonObject {
        return buildJsonObject {
            put("name", field.name)
            put("type", field.type.value)
            put("required", field.required)

            // Spread options directly at root level (no nested "options" object)
            when (field.type) {
                FieldType.TEXT -> {
                    val minValue = field.options["min"]
                    val maxValue = field.options["max"]

                    if (minValue != null) put("min", JsonPrimitive(minValue as Int))
                    if (maxValue != null) put("max", JsonPrimitive(maxValue as Int))
                }

                FieldType.NUMBER -> {
                    val minValue = field.options["min"]
                    val maxValue = field.options["max"]
                    val onlyInt = field.options["onlyInt"] as? Boolean

                    if (minValue != null) {
                        put("min", JsonPrimitive(minValue as? Double ?: (minValue as? Int)?.toDouble()))
                    }
                    if (maxValue != null) {
                        put("max", JsonPrimitive(maxValue as? Double ?: (maxValue as? Int)?.toDouble()))
                    }
                    if (onlyInt == true) put("onlyInt", JsonPrimitive(true))
                }

                FieldType.RELATION -> {
                    val collectionId = field.options["collectionId"]
                    val cascadeDelete = field.options["cascadeDelete"] as? Boolean ?: false
                    val maxSelect = field.options["maxSelect"] as? Int ?: 1

                    if (collectionId != null) {
                        put("collectionId", JsonPrimitive(collectionId.toString()))
                    }
                    put("cascadeDelete", JsonPrimitive(cascadeDelete))
                    put("maxSelect", JsonPrimitive(maxSelect))
                }

                FieldType.SELECT -> {
                    val maxSelect = field.options["maxSelect"] as? Int ?: 1
                    val values = field.options["values"] as? List<*>

                    put("maxSelect", JsonPrimitive(maxSelect))
                    if (values != null && values.isNotEmpty()) {
                        put("values", buildJsonArray {
                            values.forEach { item ->
                                add(JsonPrimitive(item.toString()))
                            }
                        })
                    }
                }

                FieldType.FILE -> {
                    val maxSelect = field.options["maxSelect"] as? Int ?: 1
                    val maxSize = field.options["maxSize"] as? Int ?: 5242880
                    val mimeTypes = field.options["mimeTypes"]

                    put("maxSelect", JsonPrimitive(maxSelect))
                    put("maxSize", JsonPrimitive(maxSize))
                    if (mimeTypes is List<*>) {
                        put("mimeTypes", buildJsonArray {
                            mimeTypes.forEach { add(JsonPrimitive(it.toString())) }
                        })
                    }
                }

                FieldType.JSON, FieldType.EDITOR -> {
                    val maxSize = field.options["maxSize"] as? Int ?: 2000000
                    put("maxSize", JsonPrimitive(maxSize))
                }

                FieldType.BOOL, FieldType.DATE, FieldType.EMAIL, FieldType.URL -> {
                    // These types don't need extra options at root level
                }
            }
        }
    }
}