package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Destination(
    @EncodeDefault val name: String = "",                  // e.g., "Nha Trang", "Đà Lạt"
    @EncodeDefault val emoji: String = "",                // banner emoji (🏖️, ⛰️, 🌸)
    @EncodeDefault val bgImage: String = "",              // background image URL
    @EncodeDefault val badgeType: String = "",            // TRENDING, BEST_SEASON, ADVENTURE, etc
    @EncodeDefault val badgeText: String = "",            // e.g., "🔥 Trending #1", "❄️ Mùa đẹp nhất"
    @EncodeDefault val viewersCount: Int = 0,             // e.g., 156
    @EncodeDefault val rating: Double = 0.0,              // e.g., 4.8
    @EncodeDefault val reviewsCount: Int = 0,             // e.g., 1200
    @EncodeDefault val tripsCount: Int = 0,               // e.g., 1234
    @EncodeDefault val costRange: String = "",            // e.g., "3-5tr"
    @EncodeDefault val temperature: String = "",          // e.g., "🌡️ 28°C"
    @EncodeDefault val tripDuration: String = "",         // e.g., "2-4 ngày"
    @EncodeDefault val highlights: List<String>? = null, // e.g., ["🏖️ Biển đẹp", "🤿 Lặn biển"]
    @EncodeDefault val bestSeasonEmoji: String = "",      // e.g., "❄️" for winter, "🌸" for spring
    @EncodeDefault val bestSeasonName: String = ""        // e.g., "Mùa đông", "Mùa xuân"
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Destination)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("name") { required = true; max = 100 }
        text("emoji") { required = false; max = 10 }
        text("bgImage") { required = false; max = 500 }
        text("badgeType") { required = true; max = 50 }
        text("badgeText") { required = true; max = 100 }
        number("viewersCount") { required = false; min = 0.0; onlyInt = true }
        number("rating") { required = true; min = 0.0; max = 5.0 }
        number("reviewsCount") { required = false; min = 0.0; onlyInt = true }
        number("tripsCount") { required = false; min = 0.0; onlyInt = true }
        text("costRange") { required = true; max = 50 }
        text("temperature") { required = true; max = 50 }
        text("tripDuration") { required = true; max = 50 }
        json("highlights") { required = false }
        text("bestSeasonEmoji") { required = false; max = 10 }
        text("bestSeasonName") { required = false; max = 100 }
    }

    override suspend fun getSeedData(): List<Destination> = listOf(
        Destination(
            name = "Nha Trang",
            emoji = "🏖️",
            bgImage = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
            badgeType = "TRENDING",
            badgeText = "🔥 Trending #1",
            viewersCount = 156,
            rating = 4.8,
            reviewsCount = 1200,
            tripsCount = 1234,
            costRange = "3-5tr",
            temperature = "🌡️ 28°C",
            tripDuration = "2-4 ngày",
            highlights = listOf("🏖️ Biển đẹp", "🤿 Lặn biển", "🍜 Ẩm thực", "🌊 Hoạt động nước", "🏝️ Đảo"),
            bestSeasonEmoji = "☀️",
            bestSeasonName = "Hè"
        ),
        Destination(
            name = "Đà Lạt",
            emoji = "⛰️",
            bgImage = "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
            badgeType = "BEST_SEASON",
            badgeText = "❄️ Mùa đẹp nhất",
            viewersCount = 89,
            rating = 4.9,
            reviewsCount = 987,
            tripsCount = 987,
            costRange = "2-4tr",
            temperature = "🌡️ 18°C",
            tripDuration = "2-3 ngày",
            highlights = listOf("🌸 Hoa", "☕ Cà phê", "🏞️ Thiên nhiên", "🏔️ Đồi núi", "🍓 Dâu tây"),
            bestSeasonEmoji = "❄️",
            bestSeasonName = "Mùa đông"
        ),
        Destination(
            name = "Sapa",
            emoji = "🌸",
            bgImage = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
            badgeType = "ADVENTURE",
            badgeText = "🏔️ Phiêu lưu",
            viewersCount = 67,
            rating = 4.7,
            reviewsCount = 756,
            tripsCount = 756,
            costRange = "3-6tr",
            temperature = "🌡️ 15°C",
            tripDuration = "3-4 ngày",
            highlights = listOf("🥾 Trekking", "🌾 Ruộng bậc thang", "🏔️ Fansipan", "🌄 Bình minh", "👥 Dân tộc"),
            bestSeasonEmoji = "🌸",
            bestSeasonName = "Mùa xuân"
        )
    )
}
