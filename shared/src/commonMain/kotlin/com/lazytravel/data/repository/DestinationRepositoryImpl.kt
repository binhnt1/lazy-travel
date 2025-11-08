package com.lazytravel.data.repository

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.domain.model.Destination
import com.lazytravel.domain.repository.DestinationRepository
import kotlinx.serialization.json.Json

/**
 * Repository Implementation - Using PocketBase
 * Lấy dữ liệu từ PocketBase server
 */
class DestinationRepositoryImpl : DestinationRepository {

    private val collectionName = PocketBaseConfig.Collections.DESTINATIONS

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getDestinations(): List<Destination> {
        val result = PocketBaseApi.getRecords(
            collection = collectionName,
            page = 1,
            perPage = 50,
            sort = "-created"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Destination.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse destination: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching destinations: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getDestinationById(id: String): Destination? {
        val result = PocketBaseApi.getRecord(collectionName, id)

        return result.fold(
            onSuccess = { responseText ->
                try {
                    json.decodeFromString(Destination.serializer(), responseText)
                } catch (e: Exception) {
                    println("⚠️ Failed to parse destination: ${e.message}")
                    null
                }
            },
            onFailure = { error ->
                println("❌ Error fetching destination $id: ${error.message}")
                null
            }
        )
    }

    override suspend fun searchDestinations(query: String): List<Destination> {
        // PocketBase filter syntax
        val filter = "name ~ '$query' || description ~ '$query'"

        val result = PocketBaseApi.getRecords(
            collection = collectionName,
            page = 1,
            perPage = 50,
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Destination.serializer(), jsonElement)
                    } catch (e: Exception) {
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error searching destinations: ${error.message}")
                emptyList()
            }
        )
    }

    /**
     * Create a new destination
     */
    suspend fun createDestination(destination: Destination): Destination? {
        val result = PocketBaseApi.createRecord(collectionName, destination)

        return result.fold(
            onSuccess = { responseText ->
                try {
                    json.decodeFromString(Destination.serializer(), responseText)
                } catch (e: Exception) {
                    println("⚠️ Failed to parse created destination: ${e.message}")
                    null
                }
            },
            onFailure = { error ->
                println("❌ Error creating destination: ${error.message}")
                null
            }
        )
    }

    /**
     * Update a destination
     */
    suspend fun updateDestination(id: String, destination: Destination): Destination? {
        val result = PocketBaseApi.updateRecord(collectionName, id, destination)

        return result.fold(
            onSuccess = { responseText ->
                try {
                    json.decodeFromString(Destination.serializer(), responseText)
                } catch (e: Exception) {
                    println("⚠️ Failed to parse updated destination: ${e.message}")
                    null
                }
            },
            onFailure = { error ->
                println("❌ Error updating destination: ${error.message}")
                null
            }
        )
    }

    /**
     * Delete a destination
     */
    suspend fun deleteDestination(id: String): Boolean {
        val result = PocketBaseApi.deleteRecord(collectionName, id)

        return result.fold(
            onSuccess = { success -> success },
            onFailure = { error ->
                println("❌ Error deleting destination: ${error.message}")
                false
            }
        )
    }
}
