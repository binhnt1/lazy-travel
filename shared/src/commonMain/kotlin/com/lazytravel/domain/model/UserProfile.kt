package com.lazytravel.domain.model

import kotlinx.serialization.Serializable

/**
 * User Profile - Hồ sơ người dùng
 */
@Serializable
data class UserProfile(
    val id: String = "",
    val userId: String,
    val bio: String? = null,
    val location: String? = null,
    val dateOfBirth: String? = null,
    val gender: Gender? = null,
    val interests: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val level: UserLevel,
    val points: Int = 0,
    val avatar: String? = null,
    val coverPhoto: String? = null,
    val verified: Boolean = false,
    val created: String? = null,
    val updated: String? = null
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER,
    PREFER_NOT_TO_SAY
}

enum class UserLevel {
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM,
    DIAMOND
}

/**
 * User Stats - Thống kê người dùng
 */
@Serializable
data class UserStats(
    val id: String = "",
    val userId: String,
    val tripsCompleted: Int = 0,
    val tripsPlanned: Int = 0,
    val countriesVisited: Int = 0,
    val citiesVisited: Int = 0,
    val totalDistance: Double = 0.0, // km
    val postsCreated: Int = 0,
    val photosUploaded: Int = 0,
    val reviewsWritten: Int = 0,
    val helpfulReviews: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val created: String? = null,
    val updated: String? = null
)

/**
 * Achievement - Thành tựu
 */
@Serializable
data class Achievement(
    val id: String = "",
    val name: String,
    val description: String,
    val icon: String,
    val category: AchievementCategory,
    val pointsReward: Int,
    val requirement: String,
    val isActive: Boolean = true,
    val created: String? = null
)

enum class AchievementCategory {
    TRIPS,
    SOCIAL,
    EXPLORER,
    COMMUNITY,
    SPECIAL
}

/**
 * User Achievement - Thành tựu của user
 */
@Serializable
data class UserAchievement(
    val id: String = "",
    val userId: String,
    val achievementId: String,
    val unlockedAt: String? = null,
    val created: String? = null
)
