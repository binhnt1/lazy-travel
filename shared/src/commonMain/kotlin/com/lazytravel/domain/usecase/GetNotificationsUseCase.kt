package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.Notification
import com.lazytravel.domain.repository.NotificationRepository

/**
 * Use Case - Lấy danh sách thông báo
 */
class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Notification>> {
        return try {
            val notifications = repository.getNotifications(userId)
            Result.success(notifications)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
