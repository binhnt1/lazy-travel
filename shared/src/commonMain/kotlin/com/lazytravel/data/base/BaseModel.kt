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
        println("🌱 Starting seed execution for ${getSchema().name}")
        val active = true
        val seedData = getSeedData()
        val now = Clock.System.now().toEpochMilliseconds()

        println("📊 Found ${seedData.size} seed items for ${getSchema().name}")
        
        if (seedData.isEmpty()) {
            println("⚠️ No seed data available for ${getSchema().name}")
            return
        }

        var successCount = 0
        var failCount = 0

        val collectionName = getSchema().name
        seedData.forEachIndexed { index, item ->
            // Get collection name from the actual item type, not from BaseModel
            val jsonObj = Json.parseToJsonElement(serializeToJson(item)).jsonObject.toMutableMap()
            jsonObj["active"] = Json.parseToJsonElement(active.toString())
            jsonObj["createdAt"] = Json.parseToJsonElement(now.toString())
            jsonObj["updatedAt"] = Json.parseToJsonElement(now.toString())
            val updatedJson = Json.encodeToString(
                JsonObject.serializer(),
                JsonObject(jsonObj)
            )
            
            println("📝 Creating record ${index + 1}/${seedData.size} in $collectionName")
            val result = PocketBaseApi.createRecord(collectionName, updatedJson)
            
            result.fold(
                onSuccess = {
                    successCount++
                    println("✅ Successfully created record ${index + 1} in $collectionName")
                },
                onFailure = { error ->
                    failCount++
                    println("❌ Failed to create record ${index + 1} in $collectionName: ${error.message}")
                }
            )
        }
        
        println("📈 Seed execution completed for $collectionName: $successCount success, $failCount failed")
    }

    suspend fun setup(shouldRecreate: Boolean = false): Result<String> {
        val collectionName = getSchema().name
        println("🚀 Starting setup for collection: $collectionName (shouldRecreate: $shouldRecreate)")
        
        return try {
            // Step 1: Initialize client
            println("📡 Initializing PocketBase client...")
            PocketBaseClient.initialize()
            println("✅ PocketBase client initialized")

            // Step 2: Admin authentication
            println("🔐 Authenticating with admin credentials...")
            println("   Admin email: ${PocketBaseConfig.Admin.EMAIL}")
            val authResult = PocketBaseApi.adminAuth(
                PocketBaseConfig.Admin.EMAIL,
                PocketBaseConfig.Admin.PASSWORD
            )
            if (authResult.isFailure) {
                val error = authResult.exceptionOrNull()
                println("❌ Admin auth failed: ${error?.message}")
                return Result.failure(
                    Exception("Admin auth failed: ${error?.message}")
                )
            }
            println("✅ Admin authentication successful")

            // Step 3: Check collection existence
            var shouldSeed = shouldRecreate
            println("🔍 Checking if collection '$collectionName' exists...")
            val exists = PocketBaseApi.collectionExists(collectionName)
            println("   Collection exists: $exists")
            
            if (!exists) {
                println("📝 Creating collection '$collectionName'...")
                val createResult = PocketBaseApi.createCollection(collectionName)
                createResult.fold(
                    onSuccess = {
                        println("✅ Collection '$collectionName' created successfully")
                        delay(500)
                        shouldSeed = true
                    },
                    onFailure = { error ->
                        println("❌ Failed to create collection '$collectionName': ${error.message}")
                        return Result.failure(error)
                    }
                )
            } else {
                if (shouldRecreate) {
                    println("🗑️ Recreating collection '$collectionName'...")
                    val deleteResult = PocketBaseApi.deleteCollection(collectionName)
                    delay(500)
                    println("   Collection deletion completed")
                }
            }

            // Step 4: Schema migration
            println("🔄 Running schema migration for '$collectionName'...")
            val migrationSuccess = SchemaMigration.migrate(this.getSchema())
            if (!migrationSuccess) {
                println("❌ Schema migration failed for '$collectionName'")
                return Result.failure(
                    Exception("Schema migration failed for $collectionName")
                )
            }
            println("✅ Schema migration completed for '$collectionName'")

            // Step 5: Check existing data
            if (!shouldSeed && exists) {
                println("📊 Checking existing data in '$collectionName'...")
                val recordsResult = PocketBaseApi.getRecords(collectionName, 1, 1)
                recordsResult.fold(
                    onSuccess = { records ->
                        val itemCount = records.items.size
                        println("   Found $itemCount existing records")
                        if (itemCount == 0) {
                            println("📝 Collection is empty, will seed data")
                            shouldSeed = true
                        }
                    },
                    onFailure = { error ->
                        println("⚠️ Failed to check existing data: ${error.message}")
                        println("📝 Will attempt to seed data anyway")
                        shouldSeed = true
                    }
                )
            }

            // Step 6: Seed data if needed
            if (shouldSeed) {
                println("🌱 Starting data seeding for '$collectionName'...")
                this.executeSeed()
            } else {
                println("⏭️ Skipping data seeding for '$collectionName' (already has data)")
            }

            val result = "✅ $collectionName setup completed"
            println(result)
            Result.success(result)
        } catch (e: Exception) {
            println("💥 Setup failed for '$collectionName': ${e.message}")
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