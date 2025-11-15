package com.lazytravel.data.remote

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * PocketBase API Service
 * Helper functions for common PocketBase operations
 */
object PocketBaseApi {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Admin auth - for superuser operations
     * Sets adminToken for collection management (create/update/delete collections)
     */
    suspend fun adminAuth(email: String, password: String): Result<AdminAuthResponse> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.post("/api/collections/_superusers/auth-with-password") {
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "identity" to email,
                    "password" to password
                ))
            }
            val responseBody = response.bodyAsText()
            if (response.status.isSuccess()) {
                val authResponse = json.decodeFromString<AdminAuthResponse>(responseBody)
                PocketBaseClient.setAdminToken(authResponse.token)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Admin auth failed: ${response.status} - $responseBody"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Create collection (admin only)
     * Requires adminToken
     */
    suspend fun createCollection(name: String): Result<Boolean> {
        return withContext(Dispatchers.Default) {
            try {
                val client = PocketBaseClient.getClient()
                val response: HttpResponse = client.post("/api/collections") {
                    contentType(ContentType.Application.Json)
                    PocketBaseClient.adminToken?.let {
                        header("Authorization", it)
                    }
                    setBody(mapOf(
                        "name" to name,
                        "type" to "base",
                        "schema" to emptyList<Any>()
                    ))
                }

                Result.success(response.status.isSuccess())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Delete collection by name
     */
    suspend fun deleteCollection(name: String) {
        val client = PocketBaseClient.getClient()
        try {
            client.delete("/api/collections/$name") {
                PocketBaseClient.adminToken?.let { header("Authorization", it) }
            }
        } catch (_: Exception) {
        }
    }

    /**
     * Check if collection exists (admin only)
     * Requires adminToken
     */
    suspend fun collectionExists(name: String): Boolean {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.get("/api/collections/$name") {
                PocketBaseClient.adminToken?.let {
                    header("Authorization", it)
                }
            }

            val exists = response.status.isSuccess()
            exists
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Get records from collection
     */
    suspend fun getRecords(
        collection: String,
        page: Int = 1,
        perPage: Int = 50,
        sort: String? = null,
        filter: String? = null
    ): Result<RecordsListResponse> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.get("/api/collections/$collection/records") {
                parameter("page", page)
                parameter("perPage", perPage)
                sort?.let { parameter("sort", it) }
                filter?.let { parameter("filter", it) }
            }

            if (response.status.isSuccess()) {
                val recordsResponse = json.decodeFromString<RecordsListResponse>(response.bodyAsText())
                Result.success(recordsResponse)
            } else {
                Result.failure(Exception("Failed to get records: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get single record
     */
    suspend fun getRecord(collection: String, id: String): Result<String> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.get("/api/collections/$collection/records/$id")

            if (response.status.isSuccess()) {
                Result.success(response.bodyAsText())
            } else {
                Result.failure(Exception("Failed to get record: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Create record
     */
    suspend fun createRecord(collection: String, data: Any): Result<String> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.post("/api/collections/$collection/records") {
                contentType(ContentType.Application.Json)
                setBody(data)
            }

            if (response.status.isSuccess()) {
                Result.success(response.bodyAsText())
            } else {
                Result.failure(Exception("Failed to create record: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update record
     */
    suspend fun updateRecord(collection: String, id: String, data: Any): Result<String> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.patch("/api/collections/$collection/records/$id") {
                contentType(ContentType.Application.Json)
                setBody(data)
            }

            if (response.status.isSuccess()) {
                Result.success(response.bodyAsText())
            } else {
                Result.failure(Exception("Failed to update record: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete record
     */
    suspend fun deleteRecord(collection: String, id: String): Result<Boolean> {
        return try {
            val client = PocketBaseClient.getClient()
            val response: HttpResponse = client.delete("/api/collections/$collection/records/$id")
            Result.success(response.status.isSuccess())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Response models
@Serializable
data class AdminAuthResponse(
    val token: String,
    val admin: AdminRecord? = null
)

@Serializable
data class AdminRecord(
    val id: String = "",
    val email: String = ""
)

@Serializable
data class RecordsListResponse(
    val page: Int = 1,
    val perPage: Int = 50,
    val totalItems: Int = 0,
    val totalPages: Int = 0,
    val items: List<kotlinx.serialization.json.JsonElement> = emptyList()
)
