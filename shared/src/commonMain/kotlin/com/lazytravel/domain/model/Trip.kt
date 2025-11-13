package com.lazytravel.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Trip - Chuyến đi
 */
@Serializable
data class Trip(
    val id: String = "",
    val name: String,
    val destinationId: String,
    val createdBy: String,
    val startDate: String, // ISO 8601 format
    val endDate: String,
    val status: TripStatus,
    val budgetTotal: Double? = null,
    val budgetSpent: Double = 0.0,
    val description: String? = null,
    val coverImage: String? = null,
    val weatherInfo: Map<String, @Contextual Any>? = null,
    val flightInfo: Map<String, @Contextual Any>? = null,
    val created: String? = null,
    val updated: String? = null
)

enum class TripStatus {
    PLANNING,
    VOTING,
    CONFIRMED,
    UPCOMING,
    ONGOING,
    COMPLETED,
    CANCELLED
}

/**
 * Trip Member - Thành viên của chuyến đi
 */
@Serializable
data class TripMember(
    val id: String = "",
    val tripId: String,
    val userId: String,
    val role: TripRole,
    val invitedBy: String? = null,
    val joinedAt: String? = null,
    val status: MemberStatus,
    val created: String? = null
)

enum class TripRole {
    ORGANIZER,
    MEMBER
}

enum class MemberStatus {
    PENDING,
    ACCEPTED,
    DECLINED
}

/**
 * Trip Expense - Chi phí của chuyến đi
 */
@Serializable
data class TripExpense(
    val id: String = "",
    val tripId: String,
    val paidBy: String,
    val amount: Double,
    val category: ExpenseCategory,
    val description: String,
    val date: String,
    val sharedWith: List<String> = emptyList(), // User IDs
    val receipt: String? = null,
    val created: String? = null
)

enum class ExpenseCategory {
    ACCOMMODATION,
    TRANSPORTATION,
    FOOD,
    ACTIVITIES,
    SHOPPING,
    OTHER
}

/**
 * Trip Checklist - Danh sách việc cần làm
 */
@Serializable
data class TripChecklist(
    val id: String = "",
    val tripId: String,
    val title: String,
    val description: String? = null,
    val created: String? = null
)

/**
 * Trip Checklist Item - Mục trong checklist
 */
@Serializable
data class TripChecklistItem(
    val id: String = "",
    val checklistId: String,
    val title: String,
    val isCompleted: Boolean = false,
    val assignedTo: String? = null,
    val dueDate: String? = null,
    val priority: Priority,
    val created: String? = null
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

/**
 * Trip Photo - Ảnh của chuyến đi
 */
@Serializable
data class TripPhoto(
    val id: String = "",
    val tripId: String,
    val uploadedBy: String,
    val photo: String, // URL
    val caption: String? = null,
    val locationTagged: String? = null,
    val takenAt: String? = null,
    val likesCount: Int = 0,
    val created: String? = null
)
