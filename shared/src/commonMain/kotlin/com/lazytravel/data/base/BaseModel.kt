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
import kotlin.time.Instant

@Serializable
abstract class BaseModel {
    open val id: String = ""
    open var active: Boolean = true
    open var deleted: Boolean = false

    @OptIn(ExperimentalTime::class)
    open var createdAt: Instant? = null

    @OptIn(ExperimentalTime::class)
    open var updatedAt: Instant? = null

    abstract fun getSchema(): CollectionSchema
    abstract suspend fun getSeedData(): List<BaseModel>

    // Abstract method để mỗi model tự serialize
    abstract fun serializeToJson(item: BaseModel): String

    @OptIn(ExperimentalTime::class)
    suspend fun executeSeed() {
        val active = true
        val deleted = false
        val now = Clock.System.now()
        val seedData = getSeedData()
        val collection = (this::class.simpleName?.lowercase() + "s")

        seedData.forEach { item ->
            val jsonString = serializeToJson(item)
            val jsonElement = Json.parseToJsonElement(jsonString)
            val jsonObj = jsonElement.jsonObject.toMutableMap()
            jsonObj["createdAt"] = Json.parseToJsonElement("\"$now\"")
            jsonObj["updatedAt"] = Json.parseToJsonElement("\"$now\"")
            jsonObj["active"] = Json.parseToJsonElement("\"$active\"")
            jsonObj["deleted"] = Json.parseToJsonElement("\"$deleted\"")

            val updatedJson = Json.encodeToString(
                JsonObject.serializer(),
                JsonObject(jsonObj)
            )
            println(collection)
            println(updatedJson)
            PocketBaseApi.createRecord(collection, updatedJson)
        }
    }

    suspend fun setup(shouldRecreate: Boolean = true): Result<String> {
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

            val collectionName = this.collectionName()

            if (shouldRecreate) {
                val exists = PocketBaseApi.collectionExists(collectionName)
                if (exists) {
                    PocketBaseApi.deleteCollection(collectionName)
                    delay(500)
                }
            }

            val migrationSuccess = SchemaMigration.migrate(this.getSchema())
            if (!migrationSuccess) {
                return Result.failure(
                    Exception("Schema migration failed for $collectionName")
                )
            }

            if (shouldRecreate) {
                this.executeSeed()
            }

            val status = if (shouldRecreate) "(recreated & seeded)" else "(migrated)"
            Result.success("✅ $collectionName $status")

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    companion object {
        inline fun <reified T : BaseModel> collectionName(): String {
            return (T::class.simpleName?.lowercase() + "s")
        }
    }
}

inline fun <reified T : BaseModel> T.collectionName(): String {
    return (T::class.simpleName?.lowercase() + "s")
}