package com.lazytravel.domain.model

import kotlinx.serialization.Serializable

/**
 * Buddy Request - Yêu cầu tìm bạn đồng hành
 */
@Serializable
data class BuddyRequest(
    val id: String = "",
    val createdBy: String,
    val tripName: String,
    val destinationId: String,
    val startDate: String,
    val endDate: String? = null,
    val totalSlots: Int,
    val filledSlots: Int = 1,
    val pricePerPerson: Double? = null,
    val description: String,
    val requirements: String? = null,
    val tags: List<String> = emptyList(),
    val status: BuddyRequestStatus,
    val created: String? = null,
    val updated: String? = null
)

enum class BuddyRequestStatus {
    OPEN,
    FULL,
    CLOSED
}

/**
 * Buddy Request Tag - Tag cho buddy request
 */
@Serializable
data class BuddyRequestTag(
    val id: String = "",
    val requestId: String,
    val tag: String,
    val created: String? = null
)
