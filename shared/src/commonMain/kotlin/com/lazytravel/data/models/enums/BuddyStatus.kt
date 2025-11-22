package com.lazytravel.data.models.enums

enum class BuddyStatus {
    AVAILABLE,      // Còn chỗ trống
    URGENT,         // Sắp đầy - chỉ còn 1-2 chỗ
    FULL,           // Đã đầy
    CLOSED;         // Đã kết thúc hoặc hủy

    companion object {
        fun allValues(): List<String> = entries.map { it.name }
    }
}
