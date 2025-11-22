package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import com.lazytravel.data.models.enums.BuddyStatus
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.jsonArray

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Buddy(
    // Link back to Trip (if converted from Trip)
    @EncodeDefault val tripId: String = "",              // relation to Trip (optional) - link back to original Trip proposal

    // Core trip information
    @EncodeDefault val userId: String = "",              // relation to User (trip organizer)
    @EncodeDefault val destination: String = "",         // e.g., "Phú Quốc", "Sapa", "Hội An - Huế" (manual entry)
    @EncodeDefault val placeId: String = "",             // Optional: relation to Place (if user selects from existing places)
    @EncodeDefault val region: String = "",              // e.g., "Miền Nam", "Miền Bắc"
    @EncodeDefault val tripTitle: String = "",           // e.g., "Phú Quốc 4N3Đ"
    @EncodeDefault val startDate: Long = 0L,             // Unix timestamp (milliseconds)
    @EncodeDefault val duration: Int = 3,                // trip duration in days (can calculate endDate from this)

    // Budget & pricing
    @EncodeDefault val budgetMin: Double = 0.0,          // e.g., 3.5 (in millions VND) - minimum budget
    @EncodeDefault val budgetMax: Double = 0.0,          // e.g., 5.5 (in millions VND) - maximum budget
    @EncodeDefault val estimatedBudget: Long = 0L,       // estimated budget in VND
    @EncodeDefault val priceNote: String = "",           // e.g., "Bao gồm vé máy bay"

    // Participants (computed from BuddyParticipant table)
    @EncodeDefault val maxParticipants: Int = 6,         // max participants
    @EncodeDefault val ageRange: String = "18-35",       // age range
    @EncodeDefault val requirements: List<String> = emptyList(), // e.g., ["Nữ, 25-35 tuổi", "Hòa đồng, vui vẻ"]

    // Description & details
    @EncodeDefault val description: String = "",         // detailed trip description
    @EncodeDefault val emoji: String = "",               // banner emoji (🏖️, ⛰️, 🛕)
    @EncodeDefault val coverImage: String = "",          // trip cover image URL

    // Trip metadata
    @EncodeDefault val tags: List<String> = emptyList(), // list of tag names (Phượt, Luxury, Backpacker...)
    @EncodeDefault val interests: List<String> = emptyList(), // trip interests/activities
    @EncodeDefault val status: String = BuddyStatus.AVAILABLE.name,  // AVAILABLE, URGENT, FULL
    @EncodeDefault val cityId: String = "",              // relation to City

    // Card display type (for UI rendering)
    @EncodeDefault val cardType: String = "STANDARD",    // HOT, LUXURY, STANDARD
    @EncodeDefault val badgeText: String = "",           // e.g., "🔥 HOT", "✨ LUXURY"
    @EncodeDefault val isFeatured: Boolean = false,      // show in featured section
    @EncodeDefault val viewCount: Int = 0                // number of views
) : BaseModel() {

    @kotlinx.serialization.Transient
    var expandedUser: User? = null

    @kotlinx.serialization.Transient
    var expandedPlace: Place? = null

    @kotlinx.serialization.Transient
    var expandedCity: City? = null

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

    // Computed properties
    val endDate: Long
        get() = startDate + (duration * 86400000L) // Add duration days in milliseconds

    val tripDuration: String
        get() = if (duration > 1) "$duration ngày ${duration - 1} đêm" else "$duration ngày"

    // Get verified status from expanded user
    val verified: Boolean
        get() = expandedUser?.verified ?: false

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Buddy)
    }

    fun populateExpandedData() {
        val expandData = expand ?: return
        expandData["userId"]?.let { userJson ->
            try {
                val user = json.decodeFromJsonElement(User.serializer(), userJson)
                expandedUser = user
            } catch (e: Exception) {
            }
        }
        expandData["cityId"]?.let { cityJson ->
            try {
                val city = json.decodeFromJsonElement(City.serializer(), cityJson)
                city.populateExpandedData()
                expandedCity = city
            } catch (e: Exception) {
                println("❌ Buddy: Failed to parse cityId: ${e.message}")
            }
        }
        expandData["placeId"]?.let { placeJson ->
            try {
                val place = json.decodeFromJsonElement(Place.serializer(), placeJson)
                expandedPlace = place
            } catch (e: Exception) {
            }
        }
        expandData["buddyreviews_via_buddy"]?.let { reviewsJson ->
            val reviews = reviewsJson.jsonArray.map {
                json.decodeFromJsonElement(BuddyReview.serializer(), it)
            }
            reviewsCount = reviews.size
            if (reviews.isNotEmpty()) {
                rating = reviews.map { it.rating }.average()
            }
        }
        expandData["buddyparticipant_via_buddy"]?.let { participantsJson ->
            val participants = participantsJson.jsonArray.mapNotNull {
                try {
                    val participant = json.decodeFromJsonElement(BuddyParticipant.serializer(), it)
                    // Only get APPROVED participants
                    if (participant.status == "APPROVED") {
                        participant.expand?.get("userId")?.let { userJson ->
                            json.decodeFromJsonElement(User.serializer(), userJson)
                        }
                    } else null
                } catch (e: Exception) {
                    null
                }
            }
            expandedParticipants = participants
            totalCapacity = maxParticipants
            availableSlots = maxParticipants - participants.size - 1 // -1 for host
        }
    }

    override suspend fun getSeedData(): List<Buddy> {
        val usersRepo = BaseRepository<User>()
        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()

        if (users.isEmpty()) {
            return emptyList()
        }

        // Get cities for relation
        val citiesRepo = BaseRepository<City>()
        val cities = citiesRepo.getRecords<City>().getOrNull() ?: emptyList()

        if (cities.isEmpty()) {
            return emptyList()
        }

        // Get places for placeId relation (optional)
        val placesRepo = BaseRepository<Place>()
        val places = placesRepo.getRecords<Place>().getOrNull() ?: emptyList()
        val placeMap = places.associateBy { it.name }

        // Định nghĩa các destination với đầy đủ thông tin
        data class DestinationInfo(
            val name: String,
            val region: String,
            val emoji: String,
            val interests: List<String>,
            val images: List<String>
        )

        // Map city names to find cityId
        val cityMap = cities.associateBy { it.name }

        val destinations = listOf(
            DestinationInfo("Phu Quoc", "Miền Nam", "🏖️",
                listOf("Biển", "Lặn biển", "Chụp ảnh", "Thư giãn"),
                listOf("https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop")),
            DestinationInfo("Sapa", "Miền Bắc", "⛰️",
                listOf("Núi", "Trekking", "Homestay", "Ruộng bậc thang"),
                listOf("https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop")),
            DestinationInfo("Da Lat", "Miền Trung", "🌸",
                listOf("Hoa", "Cà phê", "Rừng thông", "Phượt"),
                listOf("https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1609137144813-7d9921338f24?w=800&h=600&fit=crop")),
            DestinationInfo("Hoi An", "Miền Trung", "🏮",
                listOf("Phố cổ", "Đạp xe", "Gốm sứ", "Đèn lồng"),
                listOf("https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop")),
            DestinationInfo("Nha Trang", "Miền Trung", "🏊",
                listOf("Biển", "Vui chơi", "Hải sản", "Du thuyền"),
                listOf("https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop")),
            DestinationInfo("Da Nang", "Miền Trung", "🌉",
                listOf("Cầu Rồng", "Biển", "Bà Nà", "Ẩm thực"),
                listOf("https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1609137144813-7d9921338f24?w=800&h=600&fit=crop")),
            DestinationInfo("Ha Long", "Miền Bắc", "⛵",
                listOf("Du thuyền", "Đảo", "Chèo kayak", "Biển"),
                listOf("https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop")),
            DestinationInfo("Hue", "Miền Trung", "🛕",
                listOf("Cố đô", "Bún bò", "Di tích", "Sông Hương"),
                listOf("https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1609137144813-7d9921338f24?w=800&h=600&fit=crop")),
            DestinationInfo("Mui Ne", "Miền Nam", "🏜️",
                listOf("Đồi cát", "Biển", "Lướt ván", "Hải sản"),
                listOf("https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop")),
            DestinationInfo("Phan Thiet", "Miền Nam", "🌅",
                listOf("Hoàng hôn", "Biển", "Lướt sóng", "Rượu nho"),
                listOf("https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=800&h=600&fit=crop"))
        )

        val tripTitles = listOf("3N2Đ", "4N3Đ", "5N4Đ", "2N1Đ", "6N5Đ")

        val descriptions = listOf(
            "Đi biển thư giãn! Lặn ngắm san hô, xem hoàng hôn, BBQ tối. Resort 4* gần biển. Phù hợp người thích nghỉ dưỡng, chụp ảnh đẹp.",
            "Chinh phục đỉnh núi! Trekking qua ruộng bậc thang, ngủ homestay người dân tộc, ăn món đặc sản vùng cao. Phù hợp người yêu phiêu lưu, thích khám phá.",
            "Khám phá phố cổ! Đạp xe quanh phố, thưởng thức ẩm thực địa phương, chụp ảnh check-in. Phù hợp người thích văn hóa và ẩm thực.",
            "Du lịch văn hóa! Tham quan di tích lịch sử, tìm hiểu văn hóa địa phương, thưởng thức món ăn truyền thống. Phù hợp người yêu lịch sử.",
            "Nghỉ dưỡng cao cấp! Resort 5*, spa, massage, thư giãn hoàn toàn. Phù hợp người muốn nghỉ ngơi và thư giãn.",
            "Phượt bụi! Xe máy, lều trại, cắm trại bãi biển, nấu nướng cùng nhau. Phù hợp người thích phiêu lưu và tiết kiệm.",
            "Gia đình vui vẻ! Khu du lịch gia đình, trẻ em, hoạt động vui chơi. Phù hợp cho gia đình có trẻ nhỏ.",
            "Khám phá ẩm thực! Tour ăn uống, thử món đặc sản, học nấu ăn địa phương. Phù hợp người yêu thích ẩm thực.",
            "Chụp ảnh nghệ thuật! Săn location đẹp, sunrise/sunset, workshop nhiếp ảnh. Phù hợp photographer và instagrammer."
        )

        val allTags = listOf(
            listOf("Phượt", "Budget", "Backpacker"),
            listOf("Luxury", "Resort", "5 sao"),
            listOf("Nhiếp ảnh", "Check-in", "Sống ảo"),
            listOf("Ẩm thực", "Food tour", "Món ngon"),
            listOf("Văn hóa", "Lịch sử", "Di sản"),
            listOf("Thư giãn", "Spa", "Nghỉ dưỡng"),
            listOf("Phiêu lưu", "Trekking", "Extreme"),
            listOf("Gia đình", "Trẻ em", "Family"),
            listOf("Biển", "Lặn", "Bơi lội"),
            listOf("Núi", "Leo núi", "Chinh phục")
        )

        val buddies = mutableListOf<Buddy>()

        // Tạo 10 Hot trips (featured)
        for (i in 0 until 10) {
            val user = users[i % users.size]
            val dest = destinations[i % destinations.size]
            val tripDur = tripTitles[i % tripTitles.size]
            val desc = descriptions[i % descriptions.size]
            val tags = allTags[i % allTags.size]

            val budget = (3.5 + (i % 5) * 1.0)
            val capacity = 6 + (i % 2) * 2

            val durationDays = when (tripDur) {
                "3N2Đ" -> 3
                "4N3Đ" -> 4
                "5N4Đ" -> 5
                "6N5Đ" -> 6
                else -> 2
            }

            // Tạo date theo timestamp Unix - dùng ngày trong tương lai
            // Phân bổ trips trong tháng 12/2025 và 01/2026
            val isDecember = i % 2 == 0
            val month = if (isDecember) 12 else 1
            val year = if (isDecember) 2025 else 2026
            val startDay = 5 + (i % 20) // Ngày từ 5-24

            // Chuyển đổi sang Unix timestamp (milliseconds)
            // Sử dụng helper function để tính timestamp
            val startDateTs = getTimestampForDate(year, month, startDay, 9, 0, 0)

            buddies.add(Buddy(
                userId = user.id,
                destination = dest.name,
                placeId = placeMap[dest.name]?.id ?: "", // Link to place if exists, otherwise empty
                region = dest.region,
                tripTitle = "${dest.name} $tripDur",
                startDate = startDateTs,
                duration = durationDays,
                budgetMin = budget - 0.5,
                budgetMax = budget + 0.5,
                estimatedBudget = (budget * 1_000_000).toLong(),
                priceNote = "Bao gồm vé máy bay, khách sạn",
                maxParticipants = capacity,
                ageRange = "20-35",
                description = desc,
                emoji = dest.emoji,
                coverImage = dest.images[0],
                tags = listOf("🔥 HOT") + tags,
                interests = dest.interests,
                status = BuddyStatus.URGENT.name,
                cityId = cityMap[dest.name]?.id ?: cities.first().id,
                cardType = "HOT",
                badgeText = "🔥 HOT",
                isFeatured = true,
                viewCount = 500 + (i * 100),
                requirements = listOf("Hòa đồng, vui vẻ", "Tôn trọng lịch trình nhóm")
            ))
        }

        // Tạo 10 Luxury trips
        for (i in 10 until 20) {
            val user = users[i % users.size]
            val dest = destinations[i % destinations.size]
            val tripDur = listOf("4N3Đ", "5N4Đ", "6N5Đ")[i % 3]
            val desc = descriptions[i % descriptions.size]
            val tags = listOf("Luxury", "5 sao", "Sang trọng", "VIP")

            val budget = (8.0 + (i % 5) * 2.0)
            val capacity = 4 + (i % 2) * 2

            val durationDays = when (tripDur) {
                "4N3Đ" -> 4
                "5N4Đ" -> 5
                else -> 6
            }

            val startDay = 15 + (i - 10)

            // Tạo timestamp cho Luxury trips
            val startDateTs = getTimestampForDate(2025, 12, startDay, 9, 0, 0)

            buddies.add(Buddy(
                userId = user.id,
                destination = dest.name,
                placeId = placeMap[dest.name]?.id ?: "", // Link to place if exists
                region = dest.region,
                tripTitle = "${dest.name} Luxury $tripDur",
                startDate = startDateTs,
                duration = durationDays,
                budgetMin = budget - 1.0,
                budgetMax = budget + 1.0,
                estimatedBudget = (budget * 1_000_000).toLong(),
                priceNote = "Full service - Resort 5*, xe riêng",
                maxParticipants = capacity,
                ageRange = "25-45",
                description = desc,
                emoji = dest.emoji,
                coverImage = dest.images[1],
                tags = listOf("✨ LUXURY") + tags,
                interests = dest.interests,
                status = BuddyStatus.AVAILABLE.name,
                cityId = cityMap[dest.name]?.id ?: cities.first().id,
                cardType = "LUXURY",
                badgeText = "✨ LUXURY",
                isFeatured = false,
                viewCount = 300 + (i * 80),
                requirements = listOf("Thích trải nghiệm cao cấp", "Tôn trọng sự riêng tư")
            ))
        }

        // Tạo 30 Standard trips
        for (i in 20 until 50) {
            val user = users[i % users.size]
            val dest = destinations[i % destinations.size]
            val tripDur = tripTitles[i % tripTitles.size]
            val desc = descriptions[i % descriptions.size]
            val tags = allTags[i % allTags.size]

            val budget = (2.0 + (i % 8) * 0.5)
            val capacity = 4 + (i % 4) * 2

            val status = when (i % 5) {
                0 -> BuddyStatus.URGENT.name
                3 -> BuddyStatus.FULL.name
                else -> BuddyStatus.AVAILABLE.name
            }

            val durationDays = when (tripDur) {
                "3N2Đ" -> 3
                "4N3Đ" -> 4
                "5N4Đ" -> 5
                "6N5Đ" -> 6
                else -> 2
            }

            val startDay = 10 + (i - 20)

            // Tạo timestamp cho Standard trips (tháng 01/2026)
            val startDateTs = getTimestampForDate(2026, 1, startDay, 9, 0, 0)

            buddies.add(Buddy(
                userId = user.id,
                destination = dest.name,
                placeId = placeMap[dest.name]?.id ?: "", // Link to place if exists
                region = dest.region,
                tripTitle = "${dest.name} $tripDur",
                startDate = startDateTs,
                duration = durationDays,
                budgetMin = budget - 0.5,
                budgetMax = budget + 1.0,
                estimatedBudget = (budget * 1_000_000).toLong(),
                priceNote = if (i % 3 == 0) "Không bao gồm vé máy bay" else "Bao gồm khách sạn",
                maxParticipants = capacity,
                ageRange = when (i % 4) {
                    0 -> "18-25"
                    1 -> "25-35"
                    2 -> "35-45"
                    else -> "18-45"
                },
                description = desc,
                emoji = dest.emoji,
                coverImage = dest.images[i % 2],
                tags = tags,
                interests = dest.interests,
                status = status,
                cityId = cityMap[dest.name]?.id ?: cities.first().id,
                requirements = listOf("Hòa đồng", "Đúng giờ", "Có kinh nghiệm đi du lịch"),
                cardType = "STANDARD",
                badgeText = when {
                    status == BuddyStatus.URGENT.name -> "⚡ GẤP"
                    i % 7 == 0 -> "⭐ PHỔ BIẾN"
                    else -> ""
                },
                isFeatured = false,
                viewCount = 100 + (i * 25)
            ))
        }

        return buddies
    }

    override fun getSchema() = baseCollection(collectionName()) {
        // Link back to Trip (if converted from Trip)
        relation("tripId") {
            required = false
            collectionId = "trips"
            cascadeDelete = false
        }

        // Core trip information
        relation("userId") {
            required = true
            collectionId = User().collectionName()
            cascadeDelete = false
        }
        text("destination") { required = true; max = 200 }
        relation("placeId") {
            required = false
            collectionId = Place().collectionName()
            cascadeDelete = false
            maxSelect = 1
        }
        text("region") { required = false; max = 100 }
        text("tripTitle") { required = true; max = 200 }
        number("startDate") { required = true; min = 0.0; onlyInt = true }
        number("duration") { required = true; min = 1.0; onlyInt = true }

        // Budget & pricing
        number("budgetMin") { required = true; min = 0.0 }
        number("budgetMax") { required = true; min = 0.0 }
        number("estimatedBudget") { required = false; min = 0.0; onlyInt = true }
        text("priceNote") { required = false; max = 200 }

        // Participants
        number("maxParticipants") { required = false; min = 1.0; onlyInt = true }
        text("ageRange") { required = false; max = 50 }
        json("requirements") { required = false }

        // Description & details
        text("description") { required = true; max = 2000 }
        text("emoji") { required = false; max = 10 }
        text("coverImage") { required = false; max = 500 }

        // Trip metadata
        json("tags") { required = false }
        json("interests") { required = false }
        text("status") { required = true; max = 50 }
        relation("cityId") {
            required = true
            collectionId = City().collectionName()
            cascadeDelete = false
        }

        // Card display type
        text("cardType") { required = false; max = 50 }
        text("badgeText") { required = false; max = 100 }
        bool("isFeatured") { required = false }
        number("viewCount") { required = false; min = 0.0; onlyInt = true }
    }
    private fun getTimestampForDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Long {
        // Tính số ngày kể từ Unix epoch (01/01/1970)
        // Sử dụng Gregorian calendar

        // Số ngày trong mỗi tháng (không phải năm nhuận)
        val daysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        // Kiểm tra năm nhuận
        val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
        if (isLeapYear) {
            daysInMonth[1] = 29
        }

        // Tính số ngày từ 01/01/1970 đến 01/01/year
        var totalDays = 0L
        for (y in 1970 until year) {
            totalDays += if ((y % 4 == 0 && y % 100 != 0) || (y % 400 == 0)) 366 else 365
        }

        // Tính số ngày từ 01/01/year đến 01/month/day
        for (m in 1 until month) {
            totalDays += daysInMonth[m - 1]
        }
        totalDays += day - 1

        // Tính total seconds
        val totalSeconds = totalDays * 86400L + hour * 3600L + minute * 60L + second

        // Convert to milliseconds
        return totalSeconds * 1000
    }
}
