package com.lazytravel.data.models.enums

enum class TravelStyle {
    BEACH,              // 🏖️ Biển
    ADVENTURE,          // 🥾 Phiêu lưu/Trekking
    MOUNTAIN,           // 🏔️ Leo núi
    PHOTOGRAPHY,        // 📸 Nhiếp ảnh/Chụp ảnh
    RELAXATION,         // 🍹 Thư giãn
    CULTURE,            // 🛕 Văn hóa
    FOOD,               // 🍜 Ẩm thực
    DIVING,             // 🤿 Lặn biển
    HISTORICAL,         // 🏮 Phố cổ/Lịch sử
    FESTIVAL,           // 🎆 Sự kiện/Năm mới
    NATURE,             // 🌾 Thiên nhiên
    URBAN,              // 🏙️ Thành phố
    WILDLIFE;           // 🐾 Động vật/Thiên nhiên hoang dã

    companion object {
        fun allValues(): List<String> = values().map { it.name }
    }
}
