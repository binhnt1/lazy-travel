package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TourPackage(
    @EncodeDefault val name: String = "",
    @EncodeDefault val description: String = "",
    @EncodeDefault val emoji: String = "",
    @EncodeDefault val thumbnail: String = "",
    @EncodeDefault val thumbnailColor: String = "",
    @EncodeDefault val bgImage: String = "",
    @EncodeDefault val duration: Int = 0,
    @EncodeDefault val durationNights: Int = 0,
    @EncodeDefault val minGroupSize: Int = 0,
    @EncodeDefault val maxGroupSize: Int = 0,
    @EncodeDefault val currentPrice: Double = 0.0,
    @EncodeDefault val originalPrice: Double = 0.0,
    @EncodeDefault val discount: Int = 0,
    @EncodeDefault val rating: Double = 0.0,
    @EncodeDefault val reviewCount: Int = 0,
    @EncodeDefault val highlights: List<String>? = null,
    @EncodeDefault val included: List<String>? = null,
    @EncodeDefault val excluded: List<String>? = null,
    @EncodeDefault val featured: Boolean = false,
    @EncodeDefault val provider: String = "",                  // e.g., "VieTravel", "Saigon Tourist"
    @EncodeDefault val location: String = "",                  // e.g., "Phú Quốc", "Sapa"
    @EncodeDefault val tourType: String = ""                   // e.g., "BUDGET", "LUXURY", "ADVENTURE"
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as TourPackage)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("name") { required = true; max = 200 }
        text("description") { required = false; max = 2000 }
        text("emoji") { required = false; max = 10 }
        text("thumbnail") { required = false; max = 500 }
        text("thumbnailColor") { required = false; max = 20 }
        text("bgImage") { required = false; max = 500 }
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
        bool("featured") { required = false }
        text("provider") { required = false; max = 100 }
        text("location") { required = false; max = 100 }
        text("tourType") { required = false; max = 50 }
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

    override suspend fun getSeedData(): List<TourPackage> {
        return listOf(
            TourPackage(
                name = "Phú Quốc 3N2Đ - Khám phá đảo ngọc",
                description = "Trải nghiệm thiên đường biển đảo với những bãi biển tuyệt đẹp và hoạt động vui chơi phong phú",
                emoji = "🏖️",
                thumbnailColor = "#4ECDC4",
                bgImage = "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800&h=600&fit=crop",
                duration = 3,
                durationNights = 2,
                minGroupSize = 2,
                maxGroupSize = 8,
                currentPrice = 3750000.0,
                originalPrice = 5000000.0,
                discount = 25,
                rating = 4.8,
                reviewCount = 234,
                highlights = listOf("🏖️ Bãi biển đẹp", "🤿 Lặn biển", "🍜 Ẩm thực địa phương"),
                included = listOf("Xe đưa đón sân bay", "Bữa sáng", "Hướng dẫn viên tiếng Anh"),
                excluded = listOf("Chi phí cá nhân", "Mẹo du lịch"),
                featured = true,
                provider = "VieTravel",
                location = "Phú Quốc",
                tourType = "BUDGET"
            ),
            TourPackage(
                name = "Sapa - Fansipan 4N3Đ từ Hà Nội",
                description = "Chinh phục nóc nhà Đông Dương, khám phá văn hóa người dân tộc và ngắm nhìn cảnh sắc thiên nhiên hùng vĩ",
                emoji = "🌸",
                thumbnailColor = "#667EEA",
                bgImage = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
                duration = 4,
                durationNights = 3,
                minGroupSize = 2,
                maxGroupSize = 10,
                currentPrice = 2800000.0,
                originalPrice = 4000000.0,
                discount = 30,
                rating = 4.9,
                reviewCount = 456,
                highlights = listOf("🏔️ Đỉnh Fansipan", "🏞️ Cảnh núi non", "🛖 Văn hoá dân tộc"),
                included = listOf("Xe du lịch 4*", "Bữa tối & sáng", "Tham quan địa điểm nổi tiếng"),
                excluded = listOf("Chi phí cá nhân", "Tiền tip"),
                featured = true,
                provider = "Saigon Tourist",
                location = "Sapa",
                tourType = "ADVENTURE"
            ),
            TourPackage(
                name = "Hội An - Đà Nẵng - Huế 5N4Đ",
                description = "Tour khám phá di sản miền Trung với phố cổ Hội An, bãi biển Đà Nẵng và cố đô Huế",
                emoji = "🏛️",
                thumbnailColor = "#FF6B35",
                bgImage = "https://images.unsplash.com/photo-1555400038-63f5ba517a47?w=800&h=600&fit=crop",
                duration = 5,
                durationNights = 4,
                minGroupSize = 2,
                maxGroupSize = 12,
                currentPrice = 4500000.0,
                originalPrice = 5625000.0,
                discount = 20,
                rating = 4.7,
                reviewCount = 189,
                highlights = listOf("🏰 Phố cổ Hội An", "🏖️ Bãi biển Đà Nẵng", "🏯 Cố đô Huế"),
                included = listOf("Khách sạn 3*", "Bữa sáng & tối", "Hướng dẫn viên tiếng Việt"),
                excluded = listOf("Chi phí cá nhân", "Vé máy bay"),
                featured = true,
                provider = "VieTravel",
                location = "Hội An",
                tourType = "CULTURAL"
            ),
            TourPackage(
                name = "Trekking Tà Xùa - Ngắm mây 2N1Đ",
                description = "Trải nghiệm trekking đầy thử thách, ngắm biển mây tuyệt đẹp tại Tà Xùa",
                emoji = "⛰️",
                thumbnailColor = "#11998e",
                bgImage = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop",
                duration = 2,
                durationNights = 1,
                minGroupSize = 4,
                maxGroupSize = 15,
                currentPrice = 1700000.0,
                originalPrice = 2000000.0,
                discount = 15,
                rating = 5.0,
                reviewCount = 312,
                highlights = listOf("🗻 Đỉnh núi Tà Xùa", "☁️ Ngắm mây", "🚶‍♂️ Trekking 6km"),
                included = listOf("Xe đưa đón", "Bữa trưa picnic", "Hướng dẫn viên chuyên nghiệp"),
                excluded = listOf("Chi phí cá nhân", "Trang phục đặc biệt"),
                featured = true,
                provider = "Adventure Plus",
                location = "Tà Xùa",
                tourType = "ADVENTURE"
            ),
            TourPackage(
                name = "Đà Lạt - Thành phố ngàn hoa 3N2Đ",
                description = "Khám phá thành phố sương mù với khí hậu mát mẻ, những vườn hoa rực rỡ và cà phê thơm ngon",
                emoji = "🌺",
                thumbnailColor = "#FA709A",
                bgImage = "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
                duration = 3,
                durationNights = 2,
                minGroupSize = 2,
                maxGroupSize = 8,
                currentPrice = 2200000.0,
                originalPrice = 2750000.0,
                discount = 20,
                rating = 4.6,
                reviewCount = 178,
                highlights = listOf("🌸 Vườn hoa Đà Lạt", "☕ Cà phê Đà Lạt", "🚡 Cáp treo Lang Biang"),
                included = listOf("Khách sạn 4*", "Bữa sáng & tối", "Xe đưa đón sân bay"),
                excluded = listOf("Chi phí cá nhân", "Tiền tip"),
                featured = false,
                provider = "Saigon Tourist",
                location = "Đà Lạt",
                tourType = "LUXURY"
            ),
            TourPackage(
                name = "Ninh Bình - Vịnh Hạ Long trên cạn 2N1Đ",
                description = "Khám phá Tràng An, Tam Cốc - Bích Động với cảnh quan thiên nhiên kỳ vĩ",
                emoji = "🚣",
                thumbnailColor = "#38B2AC",
                bgImage = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
                duration = 2,
                durationNights = 1,
                minGroupSize = 2,
                maxGroupSize = 10,
                currentPrice = 1500000.0,
                originalPrice = 2000000.0,
                discount = 25,
                rating = 4.8,
                reviewCount = 267,
                highlights = listOf("🏞️ Tràng An", "🚣‍♀️ Đò thuyền", "🏯 Bích Động"),
                included = listOf("Xe đưa đón", "Bữa trưa địa phương", "Hướng dẫn viên tiếng Việt"),
                excluded = listOf("Chi phí cá nhân", "Vé máy bay"),
                featured = false,
                provider = "VieTravel",
                location = "Ninh Bình",
                tourType = "BUDGET"
            )
        )
    }

    companion object {
        fun getSeedData(): List<TourPackage> {
            return listOf(
                TourPackage(
                    name = "Phú Quốc 3N2Đ - Khám phá đảo ngọc",
                    description = "Trải nghiệm thiên đường biển đảo với những bãi biển tuyệt đẹp và hoạt động vui chơi phong phú",
                    emoji = "🏖️",
                    thumbnailColor = "#4ECDC4",
                    bgImage = "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800&h=600&fit=crop",
                    duration = 3,
                    durationNights = 2,
                    minGroupSize = 2,
                    maxGroupSize = 8,
                    currentPrice = 3750000.0,
                    originalPrice = 5000000.0,
                    discount = 25,
                    rating = 4.8,
                    reviewCount = 234,
                    featured = true,
                    provider = "VieTravel",
                    location = "Phú Quốc",
                    tourType = "BUDGET"
                    ),
                TourPackage(
                    name = "Sapa - Fansipan 4N3Đ từ Hà Nội",
                    description = "Chinh phục nóc nhà Đông Dương, khám phá văn hóa người dân tộc và ngắm nhìn cảnh sắc thiên nhiên hùng vĩ",
                    emoji = "🌸",
                    thumbnailColor = "#667EEA",
                    bgImage = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
                    duration = 4,
                    durationNights = 3,
                    minGroupSize = 2,
                    maxGroupSize = 10,
                    currentPrice = 2800000.0,
                    originalPrice = 4000000.0,
                    discount = 30,
                    rating = 4.9,
                    reviewCount = 456,
                    featured = true,
                    provider = "Saigon Tourist",
                    location = "Sapa",
                    tourType = "ADVENTURE"
                    ),
                TourPackage(
                    name = "Hội An - Đà Nẵng - Huế 5N4Đ",
                    description = "Tour khám phá di sản miền Trung với phố cổ Hội An, bãi biển Đà Nẵng và cố đô Huế",
                    emoji = "🏛️",
                    thumbnailColor = "#FF6B35",
                    bgImage = "https://images.unsplash.com/photo-1555400038-63f5ba517a47?w=800&h=600&fit=crop",
                    duration = 5,
                    durationNights = 4,
                    minGroupSize = 2,
                    maxGroupSize = 12,
                    currentPrice = 4500000.0,
                    originalPrice = 5625000.0,
                    discount = 20,
                    rating = 4.7,
                    reviewCount = 189,
                    featured = true,
                    provider = "VieTravel",
                    location = "Hội An",
                    tourType = "CULTURAL"
                    ),
                TourPackage(
                    name = "Trekking Tà Xùa - Ngắm mây 2N1Đ",
                    description = "Trải nghiệm trekking đầy thử thách, ngắm biển mây tuyệt đẹp tại Tà Xùa",
                    emoji = "⛰️",
                    thumbnailColor = "#11998e",
                    bgImage = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop",
                    duration = 2,
                    durationNights = 1,
                    minGroupSize = 4,
                    maxGroupSize = 15,
                    currentPrice = 1700000.0,
                    originalPrice = 2000000.0,
                    discount = 15,
                    rating = 5.0,
                    reviewCount = 312,
                    featured = true,
                    provider = "Adventure Plus",
                    location = "Tà Xùa",
                    tourType = "ADVENTURE"
                    )
            )
        }
    }
}
