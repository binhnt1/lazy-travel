package com.lazytravel.data.base

import com.lazytravel.data.remote.PocketBaseApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer

open class BaseRepository<T : BaseModel> {

    inline fun <reified R : T> getCollectionName(): String {
        var className = R::class.simpleName?.lowercase()
        className = when (className) {
            "city" -> "cities"
            "buddy" -> "buddies"
            "country" -> "countries"
            "blogcategory" -> "blogcategories"
            else -> className + "s"
        }
        return className
    }

    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    suspend inline fun <reified R : T> getRecords(
        page: Int = 1,
        perPage: Int = 100,
        filter: String? = null,
        sort: String? = null,
        expand: String? = null
    ): Result<List<R>> {
        return try {
            val collectionName = getCollectionName<R>()
            val response = PocketBaseApi.getRecords(
                collection = collectionName,
                page = page,
                perPage = perPage,
                sort = sort,
                filter = filter,
                expand = expand
            )
            response.map { recordsResponse ->
                recordsResponse.items.map { jsonElement ->
                    val record = json.decodeFromJsonElement(serializer<R>(), jsonElement)

                    // Populate expand field if present
                    if (expand != null) {
                        try {
                            val jsonObject = jsonElement.jsonObject
                            val expandObject = jsonObject["expand"]?.jsonObject

                            if (expandObject != null) {
                                // Convert JsonObject to Map<String, JsonElement>
                                record.expand = expandObject.toMap()
                            }
                        } catch (e: Exception) {
                            println("⚠️ Failed to populate expand: ${e.message}")
                        }
                    }

                    record
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend inline fun <reified R : T> getRecord(id: String): Result<R> {
        return try {
            val collectionName = getCollectionName<R>()
            val response = PocketBaseApi.getRecord(collectionName, id)
            response.map { jsonString ->
                json.decodeFromString(serializer<R>(), jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend inline fun <reified R : T> createRecord(data: R): Result<R> {
        return try {
            val collectionName = getCollectionName<R>()
            val response = PocketBaseApi.createRecord(collectionName, data as Any)
            response.map { jsonString ->
                json.decodeFromString(serializer<R>(), jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend inline fun <reified R : T> updateRecord(id: String, data: R): Result<R> {
        return try {
            val collectionName = getCollectionName<R>()
            val response = PocketBaseApi.updateRecord(collectionName, id, data as Any)
            response.map { jsonString ->
                json.decodeFromString(serializer<R>(), jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend inline fun <reified R : T> deleteRecord(id: String): Result<Boolean> {
        return try {
            val collectionName = getCollectionName<R>()
            PocketBaseApi.deleteRecord(collectionName, id)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend inline fun <reified R : T> getCount(filter: String? = null): Result<Int> {
        return try {
            val collectionName = getCollectionName<R>()
            val response = PocketBaseApi.getRecords(
                collection = collectionName,
                page = 1,
                perPage = 1,
                filter = filter
            )
            response.map { it.totalItems }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend inline fun <reified R : T> collectionExists(): Boolean {
        val collectionName = getCollectionName<R>()
        return PocketBaseApi.collectionExists(collectionName)
    }
}
