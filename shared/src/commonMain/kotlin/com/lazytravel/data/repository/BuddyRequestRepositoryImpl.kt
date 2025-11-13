package com.lazytravel.data.repository

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.domain.model.BuddyRequest
import com.lazytravel.domain.repository.BuddyRequestRepository
import kotlinx.serialization.json.Json

/**
 * Buddy Request Repository Implementation - Tìm bạn đồng hành
 */
class BuddyRequestRepositoryImpl : BuddyRequestRepository {

    private val requestsCollection = PocketBaseConfig.Collections.BUDDY_REQUESTS

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getBuddyRequests(): List<BuddyRequest> {
        val result = PocketBaseApi.getRecords(
            collection = requestsCollection,
            page = 1,
            perPage = 100,
            sort = "-created"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(BuddyRequest.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse buddy request: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching buddy requests: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getOpenRequests(): List<BuddyRequest> {
        val filter = "status = 'open'"

        val result = PocketBaseApi.getRecords(
            collection = requestsCollection,
            filter = filter,
            sort = "-created"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(BuddyRequest.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse buddy request: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching open requests: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getRequestById(id: String): BuddyRequest {
        val result = PocketBaseApi.getRecord(requestsCollection, id)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(BuddyRequest.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error fetching buddy request $id: ${error.message}")
            }
        )
    }

    override suspend fun createRequest(request: BuddyRequest): BuddyRequest {
        val result = PocketBaseApi.createRecord(requestsCollection, request)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(BuddyRequest.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error creating buddy request: ${error.message}")
            }
        )
    }

    override suspend fun updateRequest(request: BuddyRequest): BuddyRequest {
        val result = PocketBaseApi.updateRecord(requestsCollection, request.id, request)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(BuddyRequest.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error updating buddy request: ${error.message}")
            }
        )
    }

    override suspend fun deleteRequest(id: String) {
        val result = PocketBaseApi.deleteRecord(requestsCollection, id)

        result.fold(
            onSuccess = { /* Success */ },
            onFailure = { error ->
                throw Exception("Error deleting buddy request: ${error.message}")
            }
        )
    }
}
