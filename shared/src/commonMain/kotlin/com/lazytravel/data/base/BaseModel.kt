package com.lazytravel.data.base

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.data.remote.schema.CollectionSchema
import com.lazytravel.data.remote.schema.SchemaMigration
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
abstract class BaseModel {
    open val id: String = ""
    open var active: Boolean = true
    open var createdAt: Long? = null
    open var updatedAt: Long? = null

    // Expanded relations from API (populated when using expand parameter)
    @kotlinx.serialization.Transient
    var expand: Map<String, kotlinx.serialization.json.JsonElement>? = null

    companion object {
        val json = Json {
            isLenient = true
            encodeDefaults = true
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    abstract fun getSchema(): CollectionSchema
    abstract suspend fun getSeedData(): List<BaseModel>
    abstract fun serializeToJson(item: BaseModel): String

    @OptIn(ExperimentalTime::class)
    suspend fun executeSeed() {
        val active = true
        val seedData = getSeedData()
        val now = Clock.System.now().toEpochMilliseconds()

        seedData.forEach { item ->
            // Get collection name from the actual item type, not from BaseModel
            val collectionName = getSchema().name
            val jsonObj = Json.parseToJsonElement(serializeToJson(item)).jsonObject.toMutableMap()
            jsonObj["active"] = Json.parseToJsonElement(active.toString())
            jsonObj["createdAt"] = Json.parseToJsonElement(now.toString())
            jsonObj["updatedAt"] = Json.parseToJsonElement(now.toString())
            val updatedJson = Json.encodeToString(
                JsonObject.serializer(),
                JsonObject(jsonObj)
            )
            PocketBaseApi.createRecord(collectionName, updatedJson)
        }
    }

    suspend fun setup(shouldRecreate: Boolean = false): Result<String> {
        return try {
            PocketBaseClient.initialize()
            val authResult = PocketBaseApi.adminAuth(
                PocketBaseConfig.Admin.EMAIL,
                PocketBaseConfig.Admin.PASSWORD
            )
            if (authResult.isFailure) {
                return Result.failure(
                    Exception("Admin auth failed: ${authResult.exceptionOrNull()?.message}")
                )
            }

            var shouldSeed = shouldRecreate
            val collectionName = getSchema().name
            val exists = PocketBaseApi.collectionExists(collectionName)
            if (!exists) {
                PocketBaseApi.createCollection(collectionName)
                delay(500)
                shouldSeed = true
            } else {
                if (shouldRecreate) {
                    if (exists) {
                        PocketBaseApi.deleteCollection(collectionName)
                        delay(500)
                    }
                }
            }

            val migrationSuccess = SchemaMigration.migrate(this.getSchema())
            if (!migrationSuccess) {
                return Result.failure(
                    Exception("Schema migration failed for $collectionName")
                )
            }

            // Check if collection has data, if empty then seed
            if (!shouldSeed && exists) {
                val recordsResult = PocketBaseApi.getRecords(collectionName, 1, 1)
                if (recordsResult.isSuccess) {
                    val records = recordsResult.getOrNull()
                    if (records == null || records.items.isEmpty()) {
                        shouldSeed = true
                    }
                }
            }

            if (shouldSeed) {
                this.executeSeed()
            }
            Result.success("✅ $collectionName setup completed")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

inline fun <reified T : BaseModel> T.collectionName(): String {
    var className = T::class.simpleName?.lowercase()
    className = when (className) {
        "city" -> "cities"
        "buddy" -> "buddies"
        "country" -> "countries"
        "blogcategory" -> "blogcategories"
        else -> className + "s"
    }
    return className
}