package com.lazytravel.data.base

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseConfig
import kotlinx.serialization.json.Json

open class BaseRepository(val collectionName: String) {

    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    suspend inline fun <reified T : Any> getRecords(
        page: Int = 1,
        perPage: Int = 100,
        filter: String? = null,
        sort: String? = null
    ): Result<List<T>> {
        return try {
            val response = PocketBaseApi.getRecords(
                collection = collectionName,
                page = page,
                perPage = perPage,
                sort = sort,
                filter = filter
            )

            response.map { recordsResponse ->
                recordsResponse.items.map { jsonElement ->
                    json.decodeFromJsonElement(
                        deserializer = kotlinx.serialization.serializer(),
                        element = jsonElement
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend inline fun <reified T : Any> getRecord(id: String): Result<T> {
        return try {
            val response = PocketBaseApi.getRecord(collectionName, id)
            response.map { jsonString ->
                json.decodeFromString<T>(jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend inline fun <reified T : Any> createRecord(data: T): Result<T> {
        return try {
            val response = PocketBaseApi.createRecord(collectionName, data as Any)
            response.map { jsonString ->
                json.decodeFromString<T>(jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend inline fun <reified T : Any> updateRecord(
        id: String,
        data: T
    ): Result<T> {
        return try {
            val response = PocketBaseApi.updateRecord(collectionName, id, data as Any)
            response.map { jsonString ->
                json.decodeFromString<T>(jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun deleteRecord(id: String): Result<Boolean> {
        return try {
            PocketBaseApi.deleteRecord(collectionName, id)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun collectionExists(): Boolean {
        return PocketBaseApi.collectionExists(collectionName)
    }
}

fun featuresRepository() = BaseRepository(
    collectionName = PocketBaseConfig.Collections.FEATURES
)