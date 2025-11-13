package com.lazytravel.data.repository

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.domain.model.UserProfile
import com.lazytravel.domain.model.UserStats
import com.lazytravel.domain.model.Achievement
import com.lazytravel.domain.model.UserAchievement
import com.lazytravel.domain.repository.UserProfileRepository
import kotlinx.serialization.json.Json

/**
 * User Profile Repository Implementation
 */
class UserProfileRepositoryImpl : UserProfileRepository {

    private val profilesCollection = PocketBaseConfig.Collections.USER_PROFILES
    private val statsCollection = PocketBaseConfig.Collections.USER_STATS
    private val achievementsCollection = PocketBaseConfig.Collections.ACHIEVEMENTS
    private val userAchievementsCollection = PocketBaseConfig.Collections.USER_ACHIEVEMENTS

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // Profile
    override suspend fun getUserProfile(userId: String): UserProfile {
        val filter = "user_id = '$userId'"

        val result = PocketBaseApi.getRecords(
            collection = profilesCollection,
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.firstOrNull()?.let { jsonElement ->
                    json.decodeFromJsonElement(UserProfile.serializer(), jsonElement)
                } ?: throw Exception("User profile not found")
            },
            onFailure = { error ->
                throw Exception("Error fetching user profile: ${error.message}")
            }
        )
    }

    override suspend fun updateProfile(profile: UserProfile): UserProfile {
        val result = PocketBaseApi.updateRecord(profilesCollection, profile.id, profile)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(UserProfile.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error updating profile: ${error.message}")
            }
        )
    }

    // Stats
    override suspend fun getUserStats(userId: String): UserStats {
        val filter = "user_id = '$userId'"

        val result = PocketBaseApi.getRecords(
            collection = statsCollection,
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.firstOrNull()?.let { jsonElement ->
                    json.decodeFromJsonElement(UserStats.serializer(), jsonElement)
                } ?: throw Exception("User stats not found")
            },
            onFailure = { error ->
                throw Exception("Error fetching user stats: ${error.message}")
            }
        )
    }

    // Achievements
    override suspend fun getAllAchievements(): List<Achievement> {
        val filter = "is_active = true"

        val result = PocketBaseApi.getRecords(
            collection = achievementsCollection,
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Achievement.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse achievement: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching achievements: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getUserAchievements(userId: String): List<UserAchievement> {
        val filter = "user_id = '$userId'"

        val result = PocketBaseApi.getRecords(
            collection = userAchievementsCollection,
            filter = filter,
            sort = "-unlocked_at"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(UserAchievement.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse user achievement: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching user achievements: ${error.message}")
                emptyList()
            }
        )
    }
}
