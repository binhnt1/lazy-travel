package com.lazytravel.domain.repository

import com.lazytravel.domain.model.UserProfile
import com.lazytravel.domain.model.UserStats
import com.lazytravel.domain.model.Achievement
import com.lazytravel.domain.model.UserAchievement

/**
 * User Profile Repository Interface
 */
interface UserProfileRepository {
    // Profile
    suspend fun getUserProfile(userId: String): UserProfile
    suspend fun updateProfile(profile: UserProfile): UserProfile

    // Stats
    suspend fun getUserStats(userId: String): UserStats

    // Achievements
    suspend fun getAllAchievements(): List<Achievement>
    suspend fun getUserAchievements(userId: String): List<UserAchievement>
}
