package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TourProvider(
    @EncodeDefault val name: String = "",
    @EncodeDefault val slug: String = "",
    @EncodeDefault val logo: String = "",
    @EncodeDefault val description: String = "",

    // Contact
    @EncodeDefault val phone: String = "",
    @EncodeDefault val email: String = "",
    @EncodeDefault val website: String = "",
    @EncodeDefault val address: String = "",

    // Rating
    @EncodeDefault val rating: Double = 0.0,
    @EncodeDefault val reviewCount: Int = 0,

    // Verification
    @EncodeDefault val isVerified: Boolean = false,
    @EncodeDefault val licenseNumber: String = "",

    // Specialties: BUDGET, LUXURY, ADVENTURE, CULTURAL, BEACH, MOUNTAIN
    @EncodeDefault val specialties: List<String>? = null,

    // Operating regions (country codes)
    @EncodeDefault val operatingRegions: List<String>? = null,

    // Timestamps
    @EncodeDefault val foundedAt: Long = 0,
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as TourProvider)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("name") { required = true; max = 200 }
        text("slug") { required = true; max = 100 }
        text("logo") { required = false; max = 500 }
        text("description") { required = false; max = 2000 }

        text("phone") { required = false; max = 20 }
        text("email") { required = false; max = 100 }
        text("website") { required = false; max = 200 }
        text("address") { required = false; max = 500 }

        number("rating") { required = false; min = 0.0; max = 5.0 }
        number("reviewCount") { required = false; min = 0.0; onlyInt = true }

        bool("isVerified") { required = false }
        text("licenseNumber") { required = false; max = 100 }

        json("specialties") { required = false }
        json("operatingRegions") { required = false }

        number("foundedAt") { required = false; onlyInt = true }
    }

    override suspend fun getSeedData(): List<TourProvider> = listOf(
        TourProvider(
            name = "VieTravel",
            slug = "vietravel",
            logo = "https://vietravel.com/images/logo.png",
            description = "Công ty lữ hành hàng đầu Việt Nam với hơn 25 năm kinh nghiệm",
            phone = "1900 1839",
            email = "info@vietravel.com",
            website = "https://vietravel.com",
            address = "190 Pasteur, Quận 3, TP.HCM",
            rating = 4.8,
            reviewCount = 12500,
            isVerified = true,
            licenseNumber = "79-234/2020/TCDL-GP LHQT",
            specialties = listOf("BUDGET", "CULTURAL", "BEACH"),
            operatingRegions = listOf("VN", "TH", "JP", "KR", "SG"),
            foundedAt = 788918400000 // 1995-01-01
        ),
        TourProvider(
            name = "Saigon Tourist",
            slug = "saigon-tourist",
            logo = "https://saigontourist.net/images/logo.png",
            description = "Tổng công ty Du lịch Sài Gòn - Đơn vị lữ hành uy tín hàng đầu",
            phone = "028 3829 8914",
            email = "info@saigontourist.net",
            website = "https://saigontourist.net",
            address = "23 Lê Lợi, Quận 1, TP.HCM",
            rating = 4.9,
            reviewCount = 18000,
            isVerified = true,
            licenseNumber = "79-001/2020/TCDL-GP LHQT",
            specialties = listOf("LUXURY", "CULTURAL", "ADVENTURE"),
            operatingRegions = listOf("VN", "TH", "JP", "KR", "SG", "MY", "ID"),
            foundedAt = 441763200000 // 1984-01-01
        ),
        TourProvider(
            name = "Fiditour",
            slug = "fiditour",
            logo = "https://fiditour.com/images/logo.png",
            description = "Công ty du lịch với nhiều tour đa dạng trong và ngoài nước",
            phone = "028 3838 8683",
            email = "info@fiditour.com",
            website = "https://fiditour.com",
            address = "127-129 Nguyễn Huệ, Quận 1, TP.HCM",
            rating = 4.6,
            reviewCount = 8500,
            isVerified = true,
            licenseNumber = "79-156/2020/TCDL-GP LHQT",
            specialties = listOf("BUDGET", "ADVENTURE", "MOUNTAIN"),
            operatingRegions = listOf("VN", "TH", "KH", "LA"),
            foundedAt = 946684800000 // 2000-01-01
        ),
        TourProvider(
            name = "Vietravel Adventures",
            slug = "vietravel-adventures",
            logo = "https://adventures.vietravel.com/images/logo.png",
            description = "Chuyên tour mạo hiểm, trekking và khám phá thiên nhiên",
            phone = "1900 1839",
            email = "adventures@vietravel.com",
            website = "https://adventures.vietravel.com",
            address = "190 Pasteur, Quận 3, TP.HCM",
            rating = 4.7,
            reviewCount = 3200,
            isVerified = true,
            licenseNumber = "79-235/2020/TCDL-GP LHQT",
            specialties = listOf("ADVENTURE", "MOUNTAIN", "TREKKING"),
            operatingRegions = listOf("VN"),
            foundedAt = 1420070400000 // 2015-01-01
        )
    )
}
