package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import com.lazytravel.data.models.enums.PlaceType
import kotlinx.serialization.Serializable
import kotlin.collections.emptyList

@Serializable
data class Place(
    val name: String = "",
    val address: String = "",
    val cityId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val placeType: String = PlaceType.OTHER.name,
    val rating: Double = 0.0,
    val images: List<String> = emptyList(),
    val checkInsCount: Int = 0,

    // Fields from Destination (for popular destinations)
    val emoji: String = "",                // banner emoji (🏖️, ⛰️, 🌸)
    val badgeType: String = "",            // TRENDING, BEST_SEASON, ADVENTURE, etc
    val badgeText: String = "",            // e.g., "🔥 Trending #1", "❄️ Mùa đẹp nhất"
    val viewersCount: Int = 0,             // e.g., 156
    val reviewsCount: Int = 0,             // e.g., 1200
    val tripsCount: Int = 0,               // e.g., 1234
    val costRange: String = "",            // e.g., "3-5tr"
    val temperature: String = "",          // e.g., "🌡️ 28°C"
    val tripDuration: String = "",         // e.g., "2-4 ngày"
    val highlights: List<String> = emptyList(), // e.g., ["🏖️ Biển đẹp", "🤿 Lặn biển"]
    val bestSeasonEmoji: String = "",      // e.g., "❄️" for winter, "🌸" for spring
    val bestSeasonName: String = "",       // e.g., "Mùa đông", "Mùa xuân"
    val isDestination: Boolean = false     // true if this is a major destination (featured)
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Place)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("name") { required = true; max = 200 }
        text("address") { required = false; max = 500 }
        relation("cityId") {
            required = true
            collectionId = City().collectionName()
            cascadeDelete = false
            maxSelect = 1
        }
        number("latitude") { required = true }
        number("longitude") { required = true }
        text("placeType") { required = true; max = 50 }
        number("rating") { required = false }
        json("images") { required = false }
        number("checkInsCount") { required = false; onlyInt = true }

        // Destination fields
        text("emoji") { required = false; max = 10 }
        text("badgeType") { required = false; max = 50 }
        text("badgeText") { required = false; max = 100 }
        number("viewersCount") { required = false; min = 0.0; onlyInt = true }
        number("reviewsCount") { required = false; min = 0.0; onlyInt = true }
        number("tripsCount") { required = false; min = 0.0; onlyInt = true }
        text("costRange") { required = false; max = 50 }
        text("temperature") { required = false; max = 50 }
        text("tripDuration") { required = false; max = 50 }
        json("highlights") { required = false }
        text("bestSeasonEmoji") { required = false; max = 10 }
        text("bestSeasonName") { required = false; max = 100 }
        bool("isDestination") { required = false }
    }

    override suspend fun getSeedData(): List<Place> {
        // Get cities
        val cityRepo = BaseRepository<City>()
        val cities = cityRepo.getRecords<City>().getOrNull() ?: emptyList()
        val cityMap = cities.associateBy { it.name }

        return listOf(
            // Hanoi
            Place(
                name = "Hồ Hoàn Kiếm",
                address = "Hoàn Kiếm, Hà Nội",
                cityId = cityMap["Hanoi"]?.id ?: "",
                latitude = 21.0285,
                longitude = 105.8542,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.8,
                images = listOf(
                    "https://images.unsplash.com/photo-1555880679-c2d074c4f026?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"
                ),
                checkInsCount = 5200
            ),
            Place(
                name = "Văn Miếu Quốc Tử Giám",
                address = "58 Quốc Tử Giám, Đống Đa, Hà Nội",
                cityId = cityMap["Hanoi"]?.id ?: "",
                latitude = 21.0277,
                longitude = 105.8355,
                placeType = PlaceType.TEMPLE.name,
                rating = 4.7,
                images = listOf(
                    "https://images.unsplash.com/photo-1555880679-c2d074c4f026?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1609137144813-7d9921338f24?w=800&h=600&fit=crop"
                ),
                checkInsCount = 3800
            ),
            Place(
                name = "Phố Cổ Hà Nội",
                address = "Hoàn Kiếm, Hà Nội",
                cityId = cityMap["Hanoi"]?.id ?: "",
                latitude = 21.0350,
                longitude = 105.8490,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.6,
                images = listOf(
                    "https://images.unsplash.com/photo-1555880679-c2d074c4f026?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=800&h=600&fit=crop"
                ),
                checkInsCount = 6500
            ),

            // Ho Chi Minh City
            Place(
                name = "Nhà Thờ Đức Bà",
                address = "01 Công xã Paris, Bến Nghé, Quận 1",
                cityId = cityMap["Ho Chi Minh City"]?.id ?: "",
                latitude = 10.7797,
                longitude = 106.6990,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.5,
                images = listOf(
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800&h=600&fit=crop"
                ),
                checkInsCount = 4200
            ),
            Place(
                name = "Chợ Bến Thành",
                address = "Lê Lợi, Phường Bến Thành, Quận 1",
                cityId = cityMap["Ho Chi Minh City"]?.id ?: "",
                latitude = 10.7725,
                longitude = 106.6980,
                placeType = PlaceType.OTHER.name,
                rating = 4.3,
                images = listOf(
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=800&h=600&fit=crop"
                ),
                checkInsCount = 7800
            ),
            Place(
                name = "Dinh Độc Lập",
                address = "135 Nam Kỳ Khởi Nghĩa, Phường Bến Thành, Quận 1",
                cityId = cityMap["Ho Chi Minh City"]?.id ?: "",
                latitude = 10.7769,
                longitude = 106.6955,
                placeType = PlaceType.MUSEUM.name,
                rating = 4.6,
                images = listOf(
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"
                ),
                checkInsCount = 3500
            ),

            // Da Nang
            Place(
                name = "Cầu Rồng",
                address = "Cầu Rồng, Hải Châu, Đà Nẵng",
                cityId = cityMap["Da Nang"]?.id ?: "",
                latitude = 16.0608,
                longitude = 108.2279,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.7,
                images = listOf(
                    "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop"
                ),
                checkInsCount = 5600
            ),
            Place(
                name = "Chùa Linh Ứng",
                address = "Hoàng Sa, Ngũ Hành Sơn, Đà Nẵng",
                cityId = cityMap["Da Nang"]?.id ?: "",
                latitude = 16.1022,
                longitude = 108.2590,
                placeType = PlaceType.TEMPLE.name,
                rating = 4.8,
                images = listOf(
                    "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop"
                ),
                checkInsCount = 3456
            ),
            Place(
                name = "Bãi Biển Mỹ Khê",
                address = "Phường Phước Mỹ, Sơn Trà, Đà Nẵng",
                cityId = cityMap["Da Nang"]?.id ?: "",
                latitude = 16.0471,
                longitude = 108.2425,
                placeType = PlaceType.BEACH.name,
                rating = 4.9,
                images = listOf(
                    "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop"
                ),
                checkInsCount = 8900
            ),

            // Hoi An
            Place(
                name = "Phố Cổ Hội An",
                address = "Hội An Ancient Town",
                cityId = cityMap["Hoi An"]?.id ?: "",
                latitude = 15.8801,
                longitude = 108.3380,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.9,
                images = listOf(
                    "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop"
                ),
                checkInsCount = 5678
            ),
            Place(
                name = "Chùa Cầu",
                address = "Nguyễn Thị Minh Khai, Hội An",
                cityId = cityMap["Hoi An"]?.id ?: "",
                latitude = 15.8794,
                longitude = 108.3276,
                placeType = PlaceType.TEMPLE.name,
                rating = 4.8,
                images = listOf(
                    "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop"
                ),
                checkInsCount = 4200
            ),
            Place(
                name = "Bãi Biển An Bàng",
                address = "An Bàng, Cẩm An, Hội An",
                cityId = cityMap["Hoi An"]?.id ?: "",
                latitude = 15.9047,
                longitude = 108.3425,
                placeType = PlaceType.BEACH.name,
                rating = 4.7,
                images = listOf(
                    "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop"
                ),
                checkInsCount = 3800
            ),

            // Nha Trang
            Place(
                name = "Vinpearl Land",
                address = "Hòn Tre, Vĩnh Nguyên, Nha Trang",
                cityId = cityMap["Nha Trang"]?.id ?: "",
                latitude = 12.2138,
                longitude = 109.2475,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.6,
                images = listOf(
                    "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800&h=600&fit=crop"
                ),
                checkInsCount = 9500
            ),
            Place(
                name = "Bãi Biển Nha Trang",
                address = "Trần Phú, Nha Trang",
                cityId = cityMap["Nha Trang"]?.id ?: "",
                latitude = 12.2388,
                longitude = 109.1967,
                placeType = PlaceType.BEACH.name,
                rating = 4.7,
                images = listOf(
                    "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop"
                ),
                checkInsCount = 7200
            ),
            Place(
                name = "Tháp Bà Ponagar",
                address = "2 Tháng 4, Vĩnh Phước, Nha Trang",
                cityId = cityMap["Nha Trang"]?.id ?: "",
                latitude = 12.2654,
                longitude = 109.1961,
                placeType = PlaceType.TEMPLE.name,
                rating = 4.5,
                images = listOf(
                    "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop"
                ),
                checkInsCount = 3100
            ),

            // Da Lat
            Place(
                name = "Crazy House",
                address = "03 Huỳnh Thúc Kháng, Phường 4, Đà Lạt",
                cityId = cityMap["Da Lat"]?.id ?: "",
                latitude = 11.9404,
                longitude = 108.4583,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.5,
                images = listOf(
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1609137144813-7d9921338f24?w=800&h=600&fit=crop"
                ),
                checkInsCount = 1234
            ),
            Place(
                name = "Hồ Xuân Hương",
                address = "Trần Quốc Toản, Phường 1, Đà Lạt",
                cityId = cityMap["Da Lat"]?.id ?: "",
                latitude = 11.9369,
                longitude = 108.4345,
                placeType = PlaceType.PARK.name,
                rating = 4.6,
                images = listOf(
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"
                ),
                checkInsCount = 4500
            ),
            Place(
                name = "Thung Lũng Tình Yêu",
                address = "Phường 4, Đà Lạt",
                cityId = cityMap["Da Lat"]?.id ?: "",
                latitude = 11.9298,
                longitude = 108.4505,
                placeType = PlaceType.PARK.name,
                rating = 4.4,
                images = listOf(
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"
                ),
                checkInsCount = 2800
            ),

            // Hue
            Place(
                name = "Đại Nội Huế",
                address = "Thuận Thành, Huế",
                cityId = cityMap["Hue"]?.id ?: "",
                latitude = 16.4674,
                longitude = 107.5780,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.8,
                images = listOf(
                    "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop"
                ),
                checkInsCount = 6200
            ),
            Place(
                name = "Lăng Khải Định",
                address = "Thủy Bằng, Huế",
                cityId = cityMap["Hue"]?.id ?: "",
                latitude = 16.4414,
                longitude = 107.6424,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.7,
                images = listOf(
                    "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop"
                ),
                checkInsCount = 3900
            ),
            Place(
                name = "Chùa Thiên Mụ",
                address = "Kim Long, Huế",
                cityId = cityMap["Hue"]?.id ?: "",
                latitude = 16.4526,
                longitude = 107.5534,
                placeType = PlaceType.TEMPLE.name,
                rating = 4.6,
                images = listOf(
                    "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop"
                ),
                checkInsCount = 3300
            ),

            // Phu Quoc
            Place(
                name = "Vinpearl Land Phú Quốc",
                address = "Bãi Dài, Gành Dầu, Phú Quốc",
                cityId = cityMap["Phu Quoc"]?.id ?: "",
                latitude = 10.3524,
                longitude = 103.9694,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.6,
                images = listOf(
                    "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop"
                ),
                checkInsCount = 8901
            ),
            Place(
                name = "Bãi Sao",
                address = "An Thới, Phú Quốc",
                cityId = cityMap["Phu Quoc"]?.id ?: "",
                latitude = 10.1675,
                longitude = 103.9983,
                placeType = PlaceType.BEACH.name,
                rating = 4.8,
                images = listOf(
                    "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop"
                ),
                checkInsCount = 5400
            ),
            Place(
                name = "Dinh Cậu",
                address = "Dương Đông, Phú Quốc",
                cityId = cityMap["Phu Quoc"]?.id ?: "",
                latitude = 10.2176,
                longitude = 103.9613,
                placeType = PlaceType.TEMPLE.name,
                rating = 4.4,
                images = listOf(
                    "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop"
                ),
                checkInsCount = 2100
            ),

            // Bangkok
            Place(
                name = "Grand Palace",
                address = "Phra Nakhon, Bangkok",
                cityId = cityMap["Bangkok"]?.id ?: "",
                latitude = 13.7500,
                longitude = 100.4913,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.9,
                images = listOf(
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop"
                ),
                checkInsCount = 12000
            ),
            Place(
                name = "Wat Arun",
                address = "Bangkok Yai, Bangkok",
                cityId = cityMap["Bangkok"]?.id ?: "",
                latitude = 13.7436,
                longitude = 100.4887,
                placeType = PlaceType.TEMPLE.name,
                rating = 4.8,
                images = listOf(
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop"
                ),
                checkInsCount = 8900
            ),

            // Tokyo
            Place(
                name = "Tokyo Tower",
                address = "Minato, Tokyo",
                cityId = cityMap["Tokyo"]?.id ?: "",
                latitude = 35.6586,
                longitude = 139.7454,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.7,
                images = listOf(
                    "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800&h=600&fit=crop"
                ),
                checkInsCount = 15000
            ),
            Place(
                name = "Senso-ji Temple",
                address = "Asakusa, Taito, Tokyo",
                cityId = cityMap["Tokyo"]?.id ?: "",
                latitude = 35.7148,
                longitude = 139.7967,
                placeType = PlaceType.TEMPLE.name,
                rating = 4.8,
                images = listOf(
                    "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=800&h=600&fit=crop"
                ),
                checkInsCount = 11000
            ),

            // ========== FEATURED DESTINATIONS (from Destination model) ==========
            // These are major destinations with full destination info

            Place(
                name = "Nha Trang",
                address = "Nha Trang City",
                cityId = cityMap["Nha Trang"]?.id ?: "",
                latitude = 12.2388,
                longitude = 109.1967,
                placeType = PlaceType.BEACH.name,
                rating = 4.8,
                images = listOf(
                    "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop"
                ),
                checkInsCount = 5600,
                emoji = "🏖️",
                badgeType = "TRENDING",
                badgeText = "🔥 Trending #1",
                viewersCount = 156,
                reviewsCount = 1200,
                tripsCount = 1234,
                costRange = "3-5tr",
                temperature = "🌡️ 28°C",
                tripDuration = "2-4 ngày",
                highlights = listOf("🏖️ Biển đẹp", "🤿 Lặn biển", "🍜 Ẩm thực", "🌊 Hoạt động nước", "🏝️ Đảo"),
                bestSeasonEmoji = "☀️",
                bestSeasonName = "Hè",
                isDestination = true
            ),

            Place(
                name = "Đà Lạt",
                address = "Đà Lạt City",
                cityId = cityMap["Da Lat"]?.id ?: "",
                latitude = 11.9404,
                longitude = 108.4583,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.9,
                images = listOf(
                    "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"
                ),
                checkInsCount = 4500,
                emoji = "⛰️",
                badgeType = "BEST_SEASON",
                badgeText = "❄️ Mùa đẹp nhất",
                viewersCount = 89,
                reviewsCount = 987,
                tripsCount = 987,
                costRange = "2-4tr",
                temperature = "🌡️ 18°C",
                tripDuration = "2-3 ngày",
                highlights = listOf("🌸 Hoa", "☕ Cà phê", "🏞️ Thiên nhiên", "🏔️ Đồi núi", "🍓 Dâu tây"),
                bestSeasonEmoji = "❄️",
                bestSeasonName = "Mùa đông",
                isDestination = true
            ),

            Place(
                name = "Sapa",
                address = "Sa Pa, Lào Cai",
                cityId = cityMap["Hanoi"]?.id ?: "", // Use Hanoi as closest major city
                latitude = 22.3364,
                longitude = 103.8438,
                placeType = PlaceType.TOURIST_ATTRACTION.name,
                rating = 4.7,
                images = listOf(
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1609137144813-7d9921338f24?w=800&h=600&fit=crop"
                ),
                checkInsCount = 3200,
                emoji = "🌸",
                badgeType = "ADVENTURE",
                badgeText = "🏔️ Phiêu lưu",
                viewersCount = 67,
                reviewsCount = 756,
                tripsCount = 756,
                costRange = "3-6tr",
                temperature = "🌡️ 15°C",
                tripDuration = "3-4 ngày",
                highlights = listOf("🥾 Trekking", "🌾 Ruộng bậc thang", "🏔️ Fansipan", "🌄 Bình minh", "👥 Dân tộc"),
                bestSeasonEmoji = "🌸",
                bestSeasonName = "Mùa xuân",
                isDestination = true
            )
        )
    }
}

