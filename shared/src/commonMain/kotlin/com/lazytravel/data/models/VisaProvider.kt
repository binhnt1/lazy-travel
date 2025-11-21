package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class VisaProvider(
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

    // Visa specific
    // Supported countries (country codes): US, JP, KR, AU, GB, FR, DE, CA, SCHENGEN...
    @EncodeDefault val supportedCountries: List<String>? = null,

    // Visa types: TOURIST, BUSINESS, TRANSIT, E_VISA, WORK, STUDENT
    @EncodeDefault val visaTypes: List<String>? = null,

    @EncodeDefault val processingTime: String = "",  // "3-5 ngày làm việc"
    @EncodeDefault val expressAvailable: Boolean = false,
    @EncodeDefault val expressTime: String = "",     // "24-48 giờ"

    @EncodeDefault val minPrice: Double = 0.0,       // Giá dịch vụ thấp nhất (VND)
    @EncodeDefault val successRate: Double = 0.0,    // Tỷ lệ đậu visa (%)

    // Timestamps
    @EncodeDefault val foundedAt: Long = 0,
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as VisaProvider)
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

        json("supportedCountries") { required = false }
        json("visaTypes") { required = false }

        text("processingTime") { required = false; max = 50 }
        bool("expressAvailable") { required = false }
        text("expressTime") { required = false; max = 50 }

        number("minPrice") { required = false; min = 0.0 }
        number("successRate") { required = false; min = 0.0; max = 100.0 }

        number("foundedAt") { required = false; onlyInt = true }
    }

    fun getFormattedMinPrice(): String {
        val intAmount = minPrice.toLong()
        val formatted = intAmount.toString()
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
        return "${formatted}đ"
    }

    fun getFormattedSuccessRate(): String {
        return "${successRate.toInt()}%"
    }

    override suspend fun getSeedData(): List<VisaProvider> = listOf(
        VisaProvider(
            name = "Vietnam Visa Pro",
            slug = "vietnam-visa-pro",
            logo = "https://vietnamvisapro.com/images/logo.png",
            description = "Dịch vụ làm visa chuyên nghiệp với tỷ lệ đậu cao nhất thị trường",
            phone = "028 7300 7368",
            email = "support@vietnamvisapro.com",
            website = "https://vietnamvisapro.com",
            address = "123 Nguyễn Thị Minh Khai, Quận 1, TP.HCM",
            rating = 4.9,
            reviewCount = 5600,
            isVerified = true,
            licenseNumber = "VISA-001/2020/BNGV",
            supportedCountries = listOf("US", "JP", "KR", "AU", "GB", "SCHENGEN", "CA", "NZ"),
            visaTypes = listOf("TOURIST", "BUSINESS", "E_VISA"),
            processingTime = "5-7 ngày làm việc",
            expressAvailable = true,
            expressTime = "24-48 giờ",
            minPrice = 500000.0,
            successRate = 98.5,
            foundedAt = 1262304000000 // 2010-01-01
        ),
        VisaProvider(
            name = "Visa Express Vietnam",
            slug = "visa-express-vietnam",
            logo = "https://visaexpress.vn/images/logo.png",
            description = "Chuyên visa Mỹ, Châu Âu, Úc với đội ngũ tư vấn giàu kinh nghiệm",
            phone = "1900 636 018",
            email = "info@visaexpress.vn",
            website = "https://visaexpress.vn",
            address = "45 Lê Duẩn, Quận 1, TP.HCM",
            rating = 4.8,
            reviewCount = 4200,
            isVerified = true,
            licenseNumber = "VISA-025/2020/BNGV",
            supportedCountries = listOf("US", "AU", "GB", "SCHENGEN", "CA"),
            visaTypes = listOf("TOURIST", "BUSINESS", "STUDENT", "WORK"),
            processingTime = "7-10 ngày làm việc",
            expressAvailable = true,
            expressTime = "3-5 ngày",
            minPrice = 800000.0,
            successRate = 97.0,
            foundedAt = 1356998400000 // 2013-01-01
        ),
        VisaProvider(
            name = "Japan Visa Center",
            slug = "japan-visa-center",
            logo = "https://japanvisacenter.vn/images/logo.png",
            description = "Chuyên gia visa Nhật Bản - Hàn Quốc với hơn 10 năm kinh nghiệm",
            phone = "028 3822 9988",
            email = "contact@japanvisacenter.vn",
            website = "https://japanvisacenter.vn",
            address = "88 Đồng Khởi, Quận 1, TP.HCM",
            rating = 4.9,
            reviewCount = 3800,
            isVerified = true,
            licenseNumber = "VISA-042/2020/BNGV",
            supportedCountries = listOf("JP", "KR", "TW", "HK"),
            visaTypes = listOf("TOURIST", "BUSINESS", "TRANSIT"),
            processingTime = "5-7 ngày làm việc",
            expressAvailable = true,
            expressTime = "2-3 ngày",
            minPrice = 350000.0,
            successRate = 99.0,
            foundedAt = 1325376000000 // 2012-01-01
        ),
        VisaProvider(
            name = "Schengen Visa Vietnam",
            slug = "schengen-visa-vietnam",
            logo = "https://schengenvisa.vn/images/logo.png",
            description = "Đối tác chính thức làm visa Schengen - Châu Âu tại Việt Nam",
            phone = "024 3747 8899",
            email = "info@schengenvisa.vn",
            website = "https://schengenvisa.vn",
            address = "15 Ngô Quyền, Hoàn Kiếm, Hà Nội",
            rating = 4.7,
            reviewCount = 2900,
            isVerified = true,
            licenseNumber = "VISA-056/2020/BNGV",
            supportedCountries = listOf("SCHENGEN", "GB", "IE"),
            visaTypes = listOf("TOURIST", "BUSINESS", "STUDENT", "TRANSIT"),
            processingTime = "10-15 ngày làm việc",
            expressAvailable = false,
            expressTime = "",
            minPrice = 1200000.0,
            successRate = 95.5,
            foundedAt = 1420070400000 // 2015-01-01
        )
    )
}
