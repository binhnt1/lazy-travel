package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.jsonArray

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Tour(
    @EncodeDefault val name: String = "",
    @EncodeDefault val description: String = "",
    @EncodeDefault val emoji: String = "",
    @EncodeDefault val thumbnail: String = "",
    @EncodeDefault val thumbnailColor: String = "",
    @EncodeDefault val images: List<String>? = null,  // Gallery images
    @EncodeDefault val tags: List<String>? = null,     // Tour tags for filtering

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

    // Rating
    @EncodeDefault val reviewCount: Int = 0,

    // Tour details
    @EncodeDefault val highlights: List<String>? = null,
    @EncodeDefault val included: List<String>? = null,
    @EncodeDefault val excluded: List<String>? = null,

    // Languages supported: vi, en, ja, ko, zh
    @EncodeDefault val languages: List<String>? = null,

    // Timestamps
    @EncodeDefault val startDate: Long = 0,          // Tour start date

    // Additional fields
    @EncodeDefault val bookedCount: Int = 0,         // Number of bookings
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

    @kotlinx.serialization.Transient
    var rating: Double = 0.0

    @kotlinx.serialization.Transient
    var reviewsCount: Int = 0

    @kotlinx.serialization.Transient
    var totalCapacity: Int = 0

    @kotlinx.serialization.Transient
    var availableSlots: Int = 0

    @kotlinx.serialization.Transient
    var expandedParticipants: List<User> = emptyList()

    // Get all images for this Tour, combining Tour images and Place images
    val allImages: List<String>
        get() {
            val allImageList = mutableListOf<String>()

            // Add Tour images first
            if (!images.isNullOrEmpty()) {
                allImageList.addAll(images)
            }

            // Add Place images as well
            expandedPlace?.let { place ->
                if (place.images.isNotEmpty()) {
                    allImageList.addAll(place.images)
                }
            }

            // Return combined list with all images
            return allImageList
        }

    // Get limited images for card display (6 images max)
    val cardImages: List<String>
        get() = allImages.take(6)

    // Calculate discount percentage from prices
    val discount: Int
        get() {
            if (originalPrice <= 0 || currentPrice >= originalPrice) {
                return 0
            }
            return ((originalPrice - currentPrice) / originalPrice * 100).toInt()
        }

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
                val city = json.decodeFromJsonElement(City.serializer(), cityJson)
                city.populateExpandedData()
                expandedCity = city
            } catch (e: Exception) {
                println("❌ Tour: Failed to parse cityId: ${e.message}")
            }
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

        // Parse reviews
        expandData["tourreviews_via_tour"]?.let { reviewsJson ->
            val reviews = reviewsJson.jsonArray.map {
                json.decodeFromJsonElement(TourReview.serializer(), it)
            }
            reviewsCount = reviews.size
            if (reviews.isNotEmpty()) {
                rating = reviews.map { it.rating }.average()
            }
        }

        // Parse participants
        expandData["tourparticipant_via_tour"]?.let { participantsJson ->
            val participants = participantsJson.jsonArray.mapNotNull {
                try {
                    val participant = json.decodeFromJsonElement(TourParticipant.serializer(), it)
                    // Only get APPROVED participants
                    if (participant.status == "APPROVED") {
                        participant.expand?.get("userId")?.let { userJson ->
                            json.decodeFromJsonElement(User.serializer(), userJson)
                        }
                    } else null
                } catch (_: Exception) {
                    null
                }
            }
            expandedParticipants = participants
            totalCapacity = maxGroupSize
            availableSlots = maxGroupSize - participants.sumOf {
                // Get numberOfPeople from participant if available
                1 // Default to 1 per participant if we can't get the actual count
            }
        }
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("name") { required = true; max = 200 }
        text("description") { required = false; max = 2000 }
        text("emoji") { required = false; max = 10 }
        text("thumbnail") { required = false; max = 500 }
        text("thumbnailColor") { required = false; max = 20 }
        json("images") { required = false }
        json("tags") { required = false }

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

        number("rating") { required = false; min = 0.0; max = 5.0 }
        number("reviewCount") { required = false; min = 0.0; onlyInt = true }

        json("highlights") { required = false }
        json("included") { required = false }
        json("excluded") { required = false }

        json("languages") { required = false }

        number("startDate") { required = false; onlyInt = true }

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
        // Try to get providers, cities, places, airlines from DB (optional)
        val providerRepo = BaseRepository<TourProvider>()
        val cityRepo = BaseRepository<City>()
        val placeRepo = BaseRepository<Place>()
        val airlineRepo = BaseRepository<FlightProvider>()

        val providers = providerRepo.getRecords<TourProvider>().getOrNull() ?: emptyList()
        val cities = cityRepo.getRecords<City>().getOrNull() ?: emptyList()
        val places = placeRepo.getRecords<Place>().getOrNull() ?: emptyList()
        val airlines = airlineRepo.getRecords<FlightProvider>().getOrNull() ?: emptyList()

        // If no required entities exist, return empty list
        if (providers.isEmpty() || cities.isEmpty()) {
            return emptyList()
        }

        // Helper function to find provider by slug
        fun findProvider(slug: String): String {
            return providers.find { it.slug == slug }?.id ?: providers.firstOrNull()?.id ?: ""
        }

        // Helper function to find city by slug or name
        fun findCity(slug: String): String {
            return cities.find { it.name.contains(slug, ignoreCase = true) }?.id
                ?: cities.firstOrNull()?.id ?: ""
        }

        // Helper function to find place by slug (optional - returns empty if not found)
        fun findPlace(slug: String): String {
            return places.find { it.name.contains(slug, ignoreCase = true) }?.id ?: ""
        }

        // Helper function to find airline by code (optional - returns empty if not found)
        fun findAirline(code: String): String {
            return airlines.find { it.code == code }?.id ?: ""
        }

        // Tour templates data
        val tourTemplates = listOf(
            // Beach destinations
            Triple("Phú Quốc", "phu-quoc", listOf("🏖️ Bãi biển đẹp", "🤿 Lặn biển", "🍜 Hải sản tươi")),
            Triple("Nha Trang", "nha-trang", listOf("🏝️ Đảo đẹp", "🏊 Bơi lội", "🎢 Vinpearl")),
            Triple("Đà Nẵng", "da-nang", listOf("🌉 Cầu Vàng", "🏖️ Bãi Mỹ Khê", "🏛️ Phố cổ Hội An")),
            Triple("Vũng Tàu", "vung-tau", listOf("🏖️ Bãi Sau", "🗿 Tượng Chúa", "🍲 Bánh khọt")),
            Triple("Quy Nhơn", "quy-nhon", listOf("🏖️ Kỳ Co", "🏝️ Eo Gió", "🌊 Biển xanh")),

            // Mountain destinations
            Triple("Sapa", "sapa", listOf("🏔️ Fansipan", "🏞️ Ruộng bậc thang", "🛖 Văn hóa H'Mông")),
            Triple("Đà Lạt", "da-lat", listOf("🌸 Vườn hoa", "☕ Cà phê", "🌲 Thác Datanla")),
            Triple("Mù Cang Chải", "mu-cang-chai", listOf("🌾 Ruộng bậc thang", "🏔️ Núi non", "📸 Check-in")),
            Triple("Tam Đảo", "tam-dao", listOf("🌲 Rừng thông", "🏔️ Núi cao", "⛪ Nhà thờ đá")),
            Triple("Mai Châu", "mai-chau", listOf("🏡 Nhà sàn", "🚴 Đạp xe", "🌾 Cánh đồng")),

            // City/Cultural
            Triple("Hà Nội", "hanoi", listOf("🏛️ Hoàn Kiếm", "🍜 Phở", "🏺 Văn Miếu")),
            Triple("Hồ Chí Minh", "ho-chi-minh", listOf("🏛️ Dinh Độc Lập", "🏙️ Bitexco", "🍲 Ẩm thực")),
            Triple("Huế", "hue", listOf("🏰 Cố đô", "🛶 Sông Hương", "👑 Hoàng cung")),
            Triple("Hội An", "hoi-an", listOf("🏮 Phố cổ", "🏛️ Hội quán", "🎨 Nghệ thuật")),

            // Nature/Adventure
            Triple("Ninh Bình", "ninh-binh", listOf("🏞️ Tràng An", "🚣 Tam Cốc", "🏯 Bái Đính")),
            Triple("Hạ Long", "ha-long", listOf("⛵ Vịnh Hạ Long", "🏝️ Đảo Titop", "🛥️ Du thuyền")),
            Triple("Phong Nha", "phong-nha", listOf("🏞️ Hang động", "🌳 Vườn quốc gia", "🚣 Suối Nước Mọọc")),
            Triple("Cát Bà", "cat-ba", listOf("🏝️ Đảo Cát Bà", "🏖️ Bãi biển", "🌳 Vườn quốc gia"))
        )

        val tourTypes = listOf(
            "Khám phá", "Du lịch", "Nghỉ dưỡng", "Phiêu lưu", "Trải nghiệm"
        )

        val durations = listOf(
            Pair(2, 1), Pair(3, 2), Pair(4, 3), Pair(5, 4), Pair(6, 5)
        )

        val allTags = listOf(
            listOf("🔥 HOT", "Best Seller", "Top Rated"),
            listOf("✨ LUXURY", "5 sao", "Cao cấp"),
            listOf("Biển", "Resort", "Nghỉ dưỡng"),
            listOf("Núi", "Trekking", "Phiêu lưu"),
            listOf("Văn hóa", "Lịch sử", "Di sản"),
            listOf("Ẩm thực", "Food tour", "Khám phá"),
            listOf("Gia đình", "Trẻ em", "Family"),
            listOf("Budget", "Tiết kiệm", "Phượt"),
            listOf("Nhiếp ảnh", "Check-in", "Sống ảo"),
            listOf("Thành phố", "City tour", "Shopping")
        )

        val imageUrls = listOf(
            "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800",
            "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800",
            "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=800",
            "https://images.unsplash.com/photo-1555400038-63f5ba517a47?w=800",
            "https://images.unsplash.com/photo-1528127269322-539801943592?w=800",
            "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800",
            "https://images.unsplash.com/photo-1609137144813-7d9921338f24?w=800",
            "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800"
        )

        val emojis = listOf("🏖️", "🏔️", "🏛️", "⛰️", "🌺", "🚣", "🏝️", "🌄", "🏰", "🎭")
        val colors = listOf("#4ECDC4", "#667EEA", "#FF6B35", "#11998e", "#FA709A", "#38B2AC", "#FF8C42", "#764BA2")

        val providerSlugs = listOf("vietravel", "saigon-tourist", "fiditour", "vietravel-adventures")
        val airlineCodes = listOf("VJ", "VN", "QH", "")

        val tours = mutableListOf<Tour>()

        // Generate 100 tours
        for (i in 0 until 100) {
            val template = tourTemplates[i % tourTemplates.size]
            val (destinationName, citySlug, highlights) = template
            val duration = durations[i % durations.size]
            val tourType = tourTypes[i % tourTypes.size]
            val emoji = emojis[i % emojis.size]
            val color = colors[i % colors.size]
            val providerSlug = providerSlugs[i % providerSlugs.size]
            val airlineCode = airlineCodes[i % airlineCodes.size]

            // Select 2-3 tag groups
            val tourTags = mutableListOf<String>()
            val tagGroups = allTags.shuffled().take((2..3).random())
            tagGroups.forEach { group ->
                tourTags.addAll(group.take((1..2).random()))
            }

            // Select 3-6 images
            val tourImages = imageUrls.shuffled().take((3..6).random())

            // Price varies by tour type and tags
            val basePrice = when {
                tourTags.any { it.contains("LUXURY") } -> (8000000..15000000).random()
                tourTags.any { it.contains("Budget") } -> (1500000..3000000).random()
                else -> (3000000..8000000).random()
            }
            val originalPrice = (basePrice * 1.2).toLong().toDouble()

            tours.add(Tour(
                name = "$tourType $destinationName ${duration.first}N${duration.second}Đ",
                description = "Khám phá $destinationName với tour ${duration.first} ngày ${duration.second} đêm. ${highlights.joinToString(", ")}. Trải nghiệm khó quên cùng dịch vụ chuyên nghiệp.",
                emoji = emoji,
                thumbnailColor = color,
                images = tourImages,
                tags = tourTags,
                tourProviderId = findProvider(providerSlug),
                cityId = findCity(citySlug),
                placeId = if (i % 3 == 0) findPlace(destinationName) else "", // 33% have place
                airlineId = if (airlineCode.isNotEmpty() && i % 4 == 0) findAirline(airlineCode) else "", // 25% have airline
                visitedPlaces = highlights,
                duration = duration.first,
                durationNights = duration.second,
                minGroupSize = (2..4).random(),
                maxGroupSize = (8..20).random(),
                currentPrice = basePrice.toDouble(),
                originalPrice = originalPrice,
                reviewCount = (50..500).random(),
                highlights = highlights,
                included = listOf("Khách sạn", "Bữa ăn", "Hướng dẫn viên", "Vé tham quan"),
                excluded = listOf("Vé máy bay", "Chi phí cá nhân"),
                languages = if (tourTags.any { it.contains("LUXURY") }) listOf("vi", "en", "ja") else listOf("vi", "en"),
                startDate = System.currentTimeMillis() + ((7..60).random() * 24 * 60 * 60 * 1000L),
                bookedCount = (100..3000).random()
            ))
        }

        return tours
    }

    companion object {
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
                    reviewCount = 234,
                    tags = listOf("🔥 HOT", "Biển", "Resort")
                )
            )
        }
    }
}
