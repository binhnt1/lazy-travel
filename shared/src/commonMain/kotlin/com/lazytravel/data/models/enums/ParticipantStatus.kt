package com.lazytravel.data.models.enums

enum class ParticipantStatus {
    PENDING,        // Chờ duyệt
    APPROVED,       // Đã duyệt
    REJECTED,       // Bị từ chối
    CANCELLED,      // Người dùng hủy
    COMPLETED;      // Đã hoàn thành chuyến đi

    companion object {
        fun allValues(): List<String> = entries.map { it.name }
    }
}

