package com.lazytravel.data.models.enums

/**
 * Trip Status Enum
 * Quản lý lifecycle của Trip (proposal stage)
 */
enum class TripStatus {
    DRAFT,              // Đang soạn thảo
    INVITING,           // Đang mời members
    VOTING,             // Đang vote destinations
    VOTE_ENDED,         // Vote kết thúc, chờ convert
    CONVERTED_TO_BUDDY, // Đã convert sang Buddy/Schedule
    CANCELLED           // Huỷ
}
