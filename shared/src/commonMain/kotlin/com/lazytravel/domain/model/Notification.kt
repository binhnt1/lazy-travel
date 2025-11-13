package com.lazytravel.domain.model

import kotlinx.serialization.Serializable

/**
 * Notification - Thông báo cho user
 */
@Serializable
data class Notification(
    val id: String = "",
    val userId: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val actionUrl: String? = null,
    val relatedTripId: String? = null,
    val relatedPostId: String? = null,
    val priority: NotificationPriority,
    val isRead: Boolean = false,
    val created: String? = null
)

enum class NotificationType {
    VOTE_REQUEST,
    TRIP_INVITATION,
    VOTE_COMPLETED,
    TRIP_UPDATE,
    COMMENT_REPLY,
    POST_LIKE,
    MESSAGE,
    REMINDER,
    ACHIEVEMENT_UNLOCKED
}

enum class NotificationPriority {
    NORMAL,
    HIGH,
    URGENT
}
