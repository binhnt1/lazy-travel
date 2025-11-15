package com.lazytravel.data.setup

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.data.remote.schema.SchemaMigration
import com.lazytravel.data.remote.schema.featuresSchema
import kotlinx.coroutines.delay
import kotlinx.serialization.json.*

/**
 * Setup Features Collection on PocketBase Server
 * Uses schema migration system for consistency
 */
object SetupFeaturesCollection {

    /**
     * Full setup: Admin auth + Schema migration + Seed data
     */
    suspend fun setup(recreate: Boolean = false): Result<String> {
        return try {
            // 1. Initialize client
            PocketBaseClient.initialize()

            // 2. Admin authentication
            val authResult = PocketBaseApi.adminAuth(
                PocketBaseConfig.Admin.EMAIL,
                PocketBaseConfig.Admin.PASSWORD
            )
            if (authResult.isFailure) {
                return Result.failure(Exception("Admin auth failed: ${authResult.exceptionOrNull()?.message}"))
            }

            // 3. If recreate flag is true, delete existing collection
            if (recreate) {
                val exists = PocketBaseApi.collectionExists(PocketBaseConfig.Collections.FEATURES)
                if (exists) {
                    PocketBaseApi.deleteCollection(PocketBaseConfig.Collections.FEATURES)
                    delay(500) // Wait for deletion to complete
                }
            }

            // 4. Run schema migration (will create or update collection)
            val migrationSuccess = SchemaMigration.migrate(featuresSchema)
            if (!migrationSuccess) {
                return Result.failure(Exception("Schema migration failed"))
            }

            // 7. Seed production data
            if (recreate) {
                seedFeaturesData()
            }
            Result.success("✅ Features collection setup complete!")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Seed production features data
     * Uses translation keys for title/description (not bilingual data)
     */
    private fun seedFeaturesData() {
        val features = listOf(
            buildJsonObject {
                put("icon", "🗳️")
                put("title", "Vote Điểm Đến")
                put("description", "Mọi người bỏ phiếu, hệ thống tự chọn nơi phù hợp nhất")
                put("order", 1)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "💰")
                put("title", "Chia Chi Phí")
                put("description", "Tính toán tự động, thanh toán công bằng")
                put("order", 2)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "📅")
                put("title", "Lịch Trình Chi Tiết")
                put("description", "Timeline rõ ràng cho từng ngày du lịch")
                put("order", 3)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "📸")
                put("title", "Album Chung")
                put("description", "Lưu và chia sẻ ảnh cùng nhóm bạn")
                put("order", 4)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "💬")
                put("title", "Chat Nhóm")
                put("description", "Thảo luận mọi quyết định trong app")
                put("order", 5)
                put("active", true)
            },
            buildJsonObject {
                put("icon", "🏆")
                put("title", "Huy Hiệu")
                put("description", "Nhận thành tích khi hoàn thành chuyến đi")
                put("order", 6)
                put("active", true)
            }
        )
        println("✅ Seeded ${features.size} features")
    }
}