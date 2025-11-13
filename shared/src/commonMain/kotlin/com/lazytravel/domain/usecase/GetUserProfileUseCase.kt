package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.UserProfile
import com.lazytravel.domain.repository.UserProfileRepository

/**
 * Use Case - Lấy thông tin profile
 */
class GetUserProfileUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(userId: String): Result<UserProfile> {
        return try {
            val profile = repository.getUserProfile(userId)
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
