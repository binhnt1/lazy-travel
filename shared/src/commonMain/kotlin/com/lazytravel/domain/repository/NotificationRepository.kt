package com.lazytravel.domain.repository

import com.lazytravel.domain.model.Notification

/**
 * Notification Repository Interface
 */
interface NotificationRepository {
    suspend fun getNotifications(userId: String): List<Notification>
    suspend fun getUnreadNotifications(userId: String): List<Notification>
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead(userId: String)
    suspend fun deleteNotification(notificationId: String)
}
