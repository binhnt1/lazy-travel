package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Tour(
    @EncodeDefault val name: String = "",
    @EncodeDefault val description: String = "",
    @EncodeDefault val emoji: String = "",
    @EncodeDefault val thumbnail: String = "",
    @EncodeDefault val thumbnailColor: String = "",
    @EncodeDefault val bgImage: String = "",
    @EncodeDefault val images: List<String>? = null,  // Gallery images

    // Relations
    @EncodeDefault val tourProviderId: String = "",  // → TourProvider
    @EncodeDefault val cityId: String = "",          // → City (main destination)
    @EncodeDefault val placeId: String = "",         // → Place (main attraction)
    @EncodeDefault val airlineId: String = "",       // → FlightProvider (optional for air tours)

    // Visited places (list of place names for display)
    @EncodeDefault val visitedPlaces: List<String>? = null,

    // Duration
    @EncodeDefault val duration: Int = 0,            // days
    @EncodeDefault val durationNights: Int = 0,      // nights

    // Group size
    @EncodeDefault val minGroupSize: Int = 0,
    @EncodeDefault val maxGroupSize: Int = 0,

    // Pricing
    @EncodeDefault val currentPrice: Double = 0.0,
    @EncodeDefault val originalPrice: Double = 0.0,
    @EncodeDefault val discount: Int = 0,

    // Rating
    @EncodeDefault val rating: Double = 0.0,
    @EncodeDefault val reviewCount: Int = 0,

    // Tour details
    @EncodeDefault val highlights: List<String>? = null,
    @EncodeDefault val included: List<String>? = null,
    @EncodeDefault val excluded: List<String>? = null,

    // Tour type: BUDGET, LUXURY, ADVENTURE, CULTURAL, BEACH, MOUNTAIN, TREKKING
    @EncodeDefault val tourType: String = "",

    // Difficulty: EASY, MODERATE, CHALLENGING
    @EncodeDefault val difficulty: String = "",

    // Languages supported: vi, en, ja, ko, zh
    @EncodeDefault val languages: List<String>? = null,

    // Status
    @EncodeDefault val featured: Boolean = false,
    @EncodeDefault val isActive: Boolean = true,

    // Timestamps
    @EncodeDefault val startDate: Long = 0,          // Tour start date
    @EncodeDefault val endDate: Long = 0,            // Tour end date

    // Additional fields
    @EncodeDefault val departureDates: List<Long>? = null,  // Available departure dates
    @EncodeDefault val badges: List<String>? = null,        // ["HOT", "NEW", "SALE", "FEATURED"]
    @EncodeDefault val bookedCount: Int = 0,                // Number of bookings
) : BaseModel() {

    // Expanded relations
    @kotlinx.serialization.Transient
    var expandedTourProvider: TourProvider? = null

    @kotlinx.serialization.Transient
    var expandedCity: City? = null

    @kotlinx.serialization.Transient
    var expandedPlace: Place? = null

    @kotlinx.serialization.Transient
    var expandedFlightProvider: FlightProvider? = null

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Tour)
    }

    fun populateExpandedData() {
        val expandData = expand ?: return

        expandData["tourProviderId"]?.let { providerJson ->
            try {
                expandedTourProvider = json.decodeFromJsonElement(TourProvider.serializer(), providerJson)
            } catch (_: Exception) {}
        }

        expandData["cityId"]?.let { cityJson ->
            try {
                expandedCity = json.decodeFromJsonElement(City.serializer(), cityJson)
            } catch (_: Exception) {}
        }

        expandData["placeId"]?.let { placeJson ->
            try {
                expandedPlace = json.decodeFromJsonElement(Place.serializer(), placeJson)
            } catch (_: Exception) {}
        }

        expandData["airlineId"]?.let { airlineJson ->
            try {
                expandedFlightProvider = json.decodeFromJsonElement(FlightProvider.serializer(), airlineJson)
            } catch (_: Exception) {}
        }
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("name") { required = true; max = 200 }
        text("description") { required = false; max = 2000 }
        text("emoji") { required = false; max = 10 }
        text("thumbnail") { required = false; max = 500 }
        text("thumbnailColor") { required = false; max = 20 }
        text("bgImage") { required = false; max = 500 }
        json("images") { required = false }

        // Relations
        relation("tourProviderId") {
            required = true
            collectionId = TourProvider().collectionName()
            cascadeDelete = false
            maxSelect = 1
        }
        relation("cityId") {
            required = true
            collectionId = City().collectionName()
            cascadeDelete = false
            maxSelect = 1
        }
        relation("placeId") {
            required = false
            collectionId = Place().collectionName()
            cascadeDelete = false
            maxSelect = 1
        }
        relation("airlineId") {
            required = false
            collectionId = FlightProvider().collectionName()
            cascadeDelete = false
            maxSelect = 1
        }

        json("visitedPlaces") { required = false }

        number("duration") { required = true; min = 1.0; max = 100.0; onlyInt = true }
        number("durationNights") { required = false; min = 0.0; max = 100.0; onlyInt = true }
        number("minGroupSize") { required = false; min = 1.0; max = 100.0; onlyInt = true }
        number("maxGroupSize") { required = false; min = 1.0; max = 200.0; onlyInt = true }

        number("currentPrice") { required = true; min = 0.0 }
        number("originalPrice") { required = false; min = 0.0 }
        number("discount") { required = false; min = 0.0; max = 100.0; onlyInt = true }

        number("rating") { required = false; min = 0.0; max = 5.0 }
        number("reviewCount") { required = false; min = 0.0; onlyInt = true }

        json("highlights") { required = false }
        json("included") { required = false }
        json("excluded") { required = false }

        text("tourType") { required = false; max = 50 }
        text("difficulty") { required = false; max = 20 }
        json("languages") { required = false }

        bool("featured") { required = false }
        bool("isActive") { required = false }

        number("startDate") { required = false; onlyInt = true }
        number("endDate") { required = false; onlyInt = true }

        json("departureDates") { required = false }
        json("badges") { required = false }
        number("bookedCount") { required = false; min = 0.0; onlyInt = true }
    }

    fun getDurationText(): String {
        return if (durationNights > 0) {
            "${duration}N${durationNights}Đ"
        } else {
            "$duration ngày"
        }
    }

    fun getGroupSizeText(): String {
        return "$minGroupSize-$maxGroupSize người"
    }

    fun getFormattedPrice(): String {
        return formatCurrency(currentPrice)
    }

    fun getFormattedOriginalPrice(): String {
        return if (originalPrice > 0) {
            formatCurrency(originalPrice)
        } else ""
    }

    private fun formatCurrency(amount: Double): String {
        val intAmount = amount.toLong()
        val formatted = intAmount.toString()
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
        return "${formatted}đ"
    }

    override suspend fun getSeedData(): List<Tour> {
        // Get providers, cities, places, airlines first
        val providerRepo = BaseRepository<TourProvider>()
        val cityRepo = BaseRepository<City>()
        val placeRepo = BaseRepository<Place>()
        val airlineRepo = BaseRepository<FlightProvider>()

        val providers = providerRepo.getRecords<TourProvider>().getOrNull() ?: emptyList()
        val cities = cityRepo.getRecords<City>().getOrNull() ?: emptyList()
        val places = placeRepo.getRecords<Place>().getOrNull() ?: emptyList()
        val airlines = airlineRepo.getRecords<FlightProvider>().getOrNull() ?: emptyList()

        val providerMap = providers.associateBy { it.slug }
        val cityMap = cities.associateBy { it.name }
        val placeMap = places.associateBy { it.name }
        val airlineMap = airlines.associateBy { it.code }

        return listOf(
            Tour(
                name = "Phú Quốc 3N2Đ - Khám phá đảo ngọc",
                description = "Trải nghiệm thiên đường biển đảo với những bãi biển tuyệt đẹp và hoạt động vui chơi phong phú",
                emoji = "🏖️",
                thumbnailColor = "#4ECDC4",
                bgImage = "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800&h=600&fit=crop",
                images = listOf(
                    "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800",
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800",
                    "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=800"
                ),
                tourProviderId = providerMap["vietravel"]?.id ?: "",
                cityId = cityMap["Phu Quoc"]?.id ?: "",
                placeId = placeMap["Bãi Sao"]?.id ?: "",
                airlineId = airlineMap["VJ"]?.id ?: "",
                visitedPlaces = listOf("Bãi Sao", "Vinpearl Land Phú Quốc", "Dinh Cậu", "Chợ đêm Phú Quốc"),
                duration = 3,
                durationNights = 2,
                minGroupSize = 2,
                maxGroupSize = 8,
                currentPrice = 3750000.0,
                originalPrice = 5000000.0,
                discount = 25,
                rating = 4.8,
                reviewCount = 234,
                highlights = listOf("🏖️ Bãi biển đẹp", "🤿 Lặn biển ngắm san hô", "🍜 Ẩm thực hải sản"),
                included = listOf("Xe đưa đón sân bay", "Khách sạn 3*", "Bữa sáng", "Hướng dẫn viên", "Bay VietJet Air"),
                excluded = listOf("Vé máy bay", "Chi phí cá nhân", "Tiền tip"),
                tourType = "BEACH",
                difficulty = "EASY",
                languages = listOf("vi", "en"),
                featured = true,
                isActive = true,
                startDate = 1734220800000, // 2024-12-15
                endDate = 1734480000000,   // 2024-12-18
                departureDates = listOf(
                    1734220800000L, // 2024-12-15
                    1734652800000L, // 2024-12-20
                    1735084800000L  // 2024-12-25
                ),
                badges = listOf(BADGE_HOT, BADGE_SALE),
                bookedCount = 2345
            ),
            Tour(
                name = "Sapa - Fansipan 4N3Đ từ Hà Nội",
                description = "Chinh phục nóc nhà Đông Dương, khám phá văn hóa người dân tộc và ngắm nhìn cảnh sắc thiên nhiên hùng vĩ",
                emoji = "🌸",
                thumbnailColor = "#667EEA",
                bgImage = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
                tourProviderId = providerMap["saigon-tourist"]?.id ?: "",
                cityId = cityMap["Hanoi"]?.id ?: "",
                placeId = placeMap["Hồ Hoàn Kiếm"]?.id ?: "",
                visitedPlaces = listOf("Đỉnh Fansipan", "Bản Cát Cát", "Thác Bạc", "Cầu Kính Rồng Mây"),
                duration = 4,
                durationNights = 3,
                minGroupSize = 2,
                maxGroupSize = 10,
                currentPrice = 2800000.0,
                originalPrice = 4000000.0,
                discount = 30,
                rating = 4.9,
                reviewCount = 456,
                highlights = listOf("🏔️ Đỉnh Fansipan 3143m", "🏞️ Ruộng bậc thang", "🛖 Văn hóa H'Mông"),
                included = listOf("Xe limousine VIP", "Khách sạn 4*", "Bữa sáng & tối", "Cáp treo Fansipan"),
                excluded = listOf("Vé máy bay", "Chi phí cá nhân"),
                tourType = "ADVENTURE",
                difficulty = "MODERATE",
                languages = listOf("vi", "en"),
                featured = true,
                isActive = true,
                startDate = 1734652800000, // 2024-12-20
                endDate = 1734998400000    // 2024-12-24
            ),
            Tour(
                name = "Hội An - Đà Nẵng 5N4Đ",
                description = "Tour khám phá di sản miền Trung với phố cổ Hội An, bãi biển Đà Nẵng và cố đô Huế",
                emoji = "🏛️",
                thumbnailColor = "#FF6B35",
                bgImage = "https://images.unsplash.com/photo-1555400038-63f5ba517a47?w=800&h=600&fit=crop",
                tourProviderId = providerMap["vietravel"]?.id ?: "",
                cityId = cityMap["Hoi An"]?.id ?: "",
                placeId = placeMap["Phố Cổ Hội An"]?.id ?: "",
                visitedPlaces = listOf("Phố Cổ Hội An", "Cầu Rồng", "Bà Nà Hills", "Chùa Linh Ứng", "Bãi Biển Mỹ Khê"),
                duration = 5,
                durationNights = 4,
                minGroupSize = 2,
                maxGroupSize = 12,
                currentPrice = 4500000.0,
                originalPrice = 5625000.0,
                discount = 20,
                rating = 4.7,
                reviewCount = 189,
                highlights = listOf("🏰 Phố cổ Hội An", "🌉 Cầu Vàng Bà Nà", "🏖️ Biển Mỹ Khê"),
                included = listOf("Khách sạn 3*", "Bữa sáng & tối", "Vé Bà Nà Hills", "Hướng dẫn viên"),
                excluded = listOf("Vé máy bay", "Chi phí cá nhân", "Tiền tip"),
                tourType = "CULTURAL",
                difficulty = "EASY",
                languages = listOf("vi", "en", "ja"),
                featured = true,
                isActive = true,
                startDate = 1734825600000, // 2024-12-22
                endDate = 1735171200000    // 2024-12-26
            ),
            Tour(
                name = "Trekking Tà Xùa 2N1Đ - Săn mây",
                description = "Trải nghiệm trekking đầy thử thách, ngắm biển mây tuyệt đẹp tại Tà Xùa",
                emoji = "⛰️",
                thumbnailColor = "#11998e",
                bgImage = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop",
                tourProviderId = providerMap["vietravel-adventures"]?.id ?: "",
                cityId = cityMap["Hanoi"]?.id ?: "",
                placeId = placeMap["Hồ Hoàn Kiếm"]?.id ?: "",
                visitedPlaces = listOf("Đỉnh Tà Xùa", "Sống lưng khủng long", "Bản Háng Đồng"),
                duration = 2,
                durationNights = 1,
                minGroupSize = 4,
                maxGroupSize = 15,
                currentPrice = 1700000.0,
                originalPrice = 2000000.0,
                discount = 15,
                rating = 5.0,
                reviewCount = 312,
                highlights = listOf("🗻 Đỉnh Tà Xùa 2865m", "☁️ Săn mây lúc bình minh", "🚶 Trekking 6km"),
                included = listOf("Xe đưa đón", "Homestay", "Bữa tối & sáng", "Hướng dẫn viên chuyên nghiệp"),
                excluded = listOf("Chi phí cá nhân", "Trang phục trekking"),
                tourType = "TREKKING",
                difficulty = "CHALLENGING",
                languages = listOf("vi"),
                featured = true,
                isActive = true,
                startDate = 1734393600000, // 2024-12-17
                endDate = 1734480000000    // 2024-12-18
            ),
            Tour(
                name = "Đà Lạt 3N2Đ - Thành phố ngàn hoa",
                description = "Khám phá thành phố sương mù với khí hậu mát mẻ, những vườn hoa rực rỡ và cà phê thơm ngon",
                emoji = "🌺",
                thumbnailColor = "#FA709A",
                bgImage = "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
                tourProviderId = providerMap["saigon-tourist"]?.id ?: "",
                cityId = cityMap["Da Lat"]?.id ?: "",
                placeId = placeMap["Hồ Xuân Hương"]?.id ?: "",
                visitedPlaces = listOf("Hồ Xuân Hương", "Crazy House", "Thung Lũng Tình Yêu", "Đồi chè Cầu Đất"),
                duration = 3,
                durationNights = 2,
                minGroupSize = 2,
                maxGroupSize = 8,
                currentPrice = 2200000.0,
                originalPrice = 2750000.0,
                discount = 20,
                rating = 4.6,
                reviewCount = 178,
                highlights = listOf("🌸 Vườn hoa Đà Lạt", "☕ Cà phê view đẹp", "🚡 Cáp treo Robin Hill"),
                included = listOf("Khách sạn 4*", "Bữa sáng & tối", "Xe đưa đón sân bay"),
                excluded = listOf("Vé máy bay", "Chi phí cá nhân", "Tiền tip"),
                tourType = "LUXURY",
                difficulty = "EASY",
                languages = listOf("vi", "en"),
                featured = false,
                isActive = true,
                startDate = 1734566400000, // 2024-12-19
                endDate = 1734825600000    // 2024-12-22
            ),
            Tour(
                name = "Ninh Bình 2N1Đ - Vịnh Hạ Long cạn",
                description = "Khám phá Tràng An, Tam Cốc - Bích Động với cảnh quan thiên nhiên kỳ vĩ",
                emoji = "🚣",
                thumbnailColor = "#38B2AC",
                bgImage = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
                tourProviderId = providerMap["fiditour"]?.id ?: "",
                cityId = cityMap["Hanoi"]?.id ?: "",
                placeId = placeMap["Hồ Hoàn Kiếm"]?.id ?: "",
                visitedPlaces = listOf("Tràng An", "Tam Cốc", "Hang Múa", "Chùa Bái Đính"),
                duration = 2,
                durationNights = 1,
                minGroupSize = 2,
                maxGroupSize = 10,
                currentPrice = 1500000.0,
                originalPrice = 2000000.0,
                discount = 25,
                rating = 4.8,
                reviewCount = 267,
                highlights = listOf("🏞️ Tràng An di sản", "🚣 Đò Tam Cốc", "🏯 Chùa Bái Đính"),
                included = listOf("Xe đưa đón", "Khách sạn 3*", "Bữa trưa & tối", "Vé tham quan"),
                excluded = listOf("Chi phí cá nhân", "Đồ uống"),
                tourType = "CULTURAL",
                difficulty = "EASY",
                languages = listOf("vi", "en"),
                featured = false,
                isActive = true,
                startDate = 1734307200000, // 2024-12-16
                endDate = 1734393600000    // 2024-12-17
            )
        )
    }

    companion object {
        // Badge constants
        const val BADGE_HOT = "HOT"
        const val BADGE_NEW = "NEW"
        const val BADGE_SALE = "SALE"
        const val BADGE_FEATURED = "FEATURED"

        fun getSeedDataStatic(): List<Tour> {
            // Static seed data without relations (for testing)
            return listOf(
                Tour(
                    name = "Phú Quốc 3N2Đ - Khám phá đảo ngọc",
                    description = "Trải nghiệm thiên đường biển đảo",
                    emoji = "🏖️",
                    thumbnailColor = "#4ECDC4",
                    duration = 3,
                    durationNights = 2,
                    currentPrice = 3750000.0,
                    originalPrice = 5000000.0,
                    discount = 25,
                    rating = 4.8,
                    reviewCount = 234,
                    tourType = "BEACH",
                    difficulty = "EASY",
                    featured = true
                )
            )
        }
    }
}
