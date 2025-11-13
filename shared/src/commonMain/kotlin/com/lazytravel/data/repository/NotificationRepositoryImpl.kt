package com.lazytravel.data.repository

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.domain.model.Notification
import com.lazytravel.domain.repository.NotificationRepository
import kotlinx.serialization.json.Json

/**
 * Notification Repository Implementation
 */
class NotificationRepositoryImpl : NotificationRepository {

    private val notificationsCollection = PocketBaseConfig.Collections.NOTIFICATIONS

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getNotifications(userId: String): List<Notification> {
        val filter = "user_id = '$userId'"

        val result = PocketBaseApi.getRecords(
            collection = notificationsCollection,
            page = 1,
            perPage = 100,
            sort = "-created",
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Notification.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse notification: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching notifications: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getUnreadNotifications(userId: String): List<Notification> {
        val filter = "user_id = '$userId' && is_read = false"

        val result = PocketBaseApi.getRecords(
            collection = notificationsCollection,
            filter = filter,
            sort = "-created"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Notification.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse notification: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching unread notifications: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun markAsRead(notificationId: String) {
        val update = mapOf("is_read" to true)

        val result = PocketBaseApi.updateRecord(notificationsCollection, notificationId, update)

        result.fold(
            onSuccess = { /* Success */ },
            onFailure = { error ->
                println("❌ Error marking notification as read: ${error.message}")
            }
        )
    }

    override suspend fun markAllAsRead(userId: String) {
        // Get all unread notifications first
        val unreadNotifications = getUnreadNotifications(userId)

        // Mark each as read
        unreadNotifications.forEach { notification ->
            markAsRead(notification.id)
        }
    }

    override suspend fun deleteNotification(notificationId: String) {
        val result = PocketBaseApi.deleteRecord(notificationsCollection, notificationId)

        result.fold(
            onSuccess = { /* Success */ },
            onFailure = { error ->
                throw Exception("Error deleting notification: ${error.message}")
            }
        )
    }
}
