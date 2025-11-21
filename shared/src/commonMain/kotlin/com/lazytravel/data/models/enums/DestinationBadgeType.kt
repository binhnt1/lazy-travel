package com.lazytravel.data.models.enums

enum class DestinationBadgeType {
    TRENDING,           // 🔥 Trending #1, #2, etc
    BEST_SEASON,        // ❄️ Mùa đẹp nhất (best season)
    ADVENTURE,          // 🏔️ Phiêu lưu
    CULTURAL,           // 🏮 Văn hóa
    BEACH,              // 🏖️ Bãi biển
    FOOD,               // 🍜 Ẩm thực
    NATURE;             // 🌿 Thiên nhiên

    companion object {
        fun allValues(): List<String> = values().map { it.name }
    }
}
