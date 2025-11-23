package com.lazytravel.data.models

/**
 * Data class chứa thông tin thống kê của user
 * Được tính toán từ BuddyReview, không lưu vào database
 */
data class UserStatsSummary(
    val userId: String,
    val averageRating: Double = 0.0,
    val totalReviews: Int = 0,
    val totalTrips: Int = 0,
    val verifiedBadge: Boolean = false
)

