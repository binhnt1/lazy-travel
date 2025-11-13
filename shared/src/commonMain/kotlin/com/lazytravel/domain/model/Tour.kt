package com.lazytravel.domain.model

import kotlinx.serialization.Serializable

/**
 * Tour - Gói tour du lịch
 */
@Serializable
data class Tour(
    val id: String = "",
    val name: String,
    val destinationId: String,
    val pricePerPerson: Double,
    val durationDays: Int,
    val description: String,
    val highlights: List<String> = emptyList(),
    val included: List<String> = emptyList(),
    val excluded: List<String> = emptyList(),
    val images: List<String> = emptyList(),
    val badge: TourBadge? = null,
    val rating: Double = 0.0,
    val reviewsCount: Int = 0,
    val maxParticipants: Int? = null,
    val availableSlots: Int? = null,
    val isActive: Boolean = true,
    val created: String? = null,
    val updated: String? = null
)

enum class TourBadge {
    HOT,
    NEW,
    DISCOUNT
}

/**
 * Tour Review - Đánh giá tour
 */
@Serializable
data class TourReview(
    val id: String = "",
    val tourId: String,
    val userId: String,
    val rating: Int, // 1-5
    val comment: String,
    val photos: List<String> = emptyList(),
    val helpfulCount: Int = 0,
    val verifiedPurchase: Boolean = false,
    val created: String? = null
)
