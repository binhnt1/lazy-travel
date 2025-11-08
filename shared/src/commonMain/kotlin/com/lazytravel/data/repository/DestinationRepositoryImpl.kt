package com.lazytravel.data.repository

import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.domain.model.Destination
import com.lazytravel.domain.repository.DestinationRepository
import io.github.agrevster.pocketbaseKotlin.dsl.query.RecordListOptions
import kotlinx.serialization.json.Json

/**
 * Repository Implementation - Using PocketBase
 * Lấy dữ liệu từ PocketBase server
 */
class DestinationRepositoryImpl : DestinationRepository {

    private val client = PocketBaseClient.getInstance()
    private val collectionName = PocketBaseConfig.Collections.DESTINATIONS

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getDestinations(): List<Destination> {
        return try {
            val records = client.records.getList(
                collection = collectionName,
                options = RecordListOptions(
                    page = 1,
                    perPage = 50,
                    sort = "-created" // Newest first
                )
            )

            // Parse records to Destination objects
            records.items.mapNotNull { record ->
                try {
                    json.decodeFromString<Destination>(record.toString())
                } catch (e: Exception) {
                    println("⚠️ Failed to parse destination: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            println("❌ Error fetching destinations: ${e.message}")
            // Return empty list on error (or throw exception)
            emptyList()
        }
    }

    override suspend fun getDestinationById(id: String): Destination? {
        return try {
            val record = client.records.getOne(
                collection = collectionName,
                id = id
            )

            json.decodeFromString<Destination>(record.toString())
        } catch (e: Exception) {
            println("❌ Error fetching destination $id: ${e.message}")
            null
        }
    }

    override suspend fun searchDestinations(query: String): List<Destination> {
        return try {
            // PocketBase filter syntax
            val filter = "name ~ '$query' || description ~ '$query'"

            val records = client.records.getList(
                collection = collectionName,
                options = RecordListOptions(
                    page = 1,
                    perPage = 50,
                    filter = filter
                )
            )

            records.items.mapNotNull { record ->
                try {
                    json.decodeFromString<Destination>(record.toString())
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            println("❌ Error searching destinations: ${e.message}")
            emptyList()
        }
    }

    /**
     * Create a new destination
     * Example usage for adding data
     */
    suspend fun createDestination(destination: Destination): Destination? {
        return try {
            val record = client.records.create(
                collection = collectionName,
                body = destination
            )

            json.decodeFromString<Destination>(record.toString())
        } catch (e: Exception) {
            println("❌ Error creating destination: ${e.message}")
            null
        }
    }

    /**
     * Update a destination
     */
    suspend fun updateDestination(id: String, destination: Destination): Destination? {
        return try {
            val record = client.records.update(
                collection = collectionName,
                id = id,
                body = destination
            )

            json.decodeFromString<Destination>(record.toString())
        } catch (e: Exception) {
            println("❌ Error updating destination: ${e.message}")
            null
        }
    }

    /**
     * Delete a destination
     */
    suspend fun deleteDestination(id: String): Boolean {
        return try {
            client.records.delete(
                collection = collectionName,
                id = id
            )
            true
        } catch (e: Exception) {
            println("❌ Error deleting destination: ${e.message}")
            false
        }
    }
}
