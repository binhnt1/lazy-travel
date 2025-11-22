package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class InsuranceProvider(
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

    // Insurance specific
    // Coverage types: MEDICAL, TRIP_CANCELLATION, BAGGAGE, FLIGHT_DELAY, ACCIDENT, COVID
    @EncodeDefault val coverageTypes: List<String>? = null,

    // Coverage regions: DOMESTIC, ASIA, GLOBAL, SCHENGEN
    @EncodeDefault val coverageRegions: List<String>? = null,

    @EncodeDefault val minPrice: Double = 0.0,       // Giá gói thấp nhất (VND)
    @EncodeDefault val maxCoverage: Double = 0.0,    // Mức bồi thường tối đa (VND)

    @EncodeDefault val claimHotline: String = "",    // Hotline bồi thường 24/7
    @EncodeDefault val claimEmail: String = "",

    // Timestamps
    @EncodeDefault val foundedAt: Long = 0,
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as InsuranceProvider)
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

        json("coverageTypes") { required = false }
        json("coverageRegions") { required = false }

        number("minPrice") { required = false; min = 0.0 }
        number("maxCoverage") { required = false; min = 0.0 }

        text("claimHotline") { required = false; max = 20 }
        text("claimEmail") { required = false; max = 100 }

        number("foundedAt") { required = false; onlyInt = true }
    }

    fun getFormattedMinPrice(): String {
        return formatCurrency(minPrice)
    }

    fun getFormattedMaxCoverage(): String {
        return formatCurrency(maxCoverage)
    }

    private fun formatCurrency(amount: Double): String {
        val intAmount = amount.toLong()
        if (intAmount >= 1_000_000_000) {
            return "${intAmount / 1_000_000_000} tỷ"
        }
        if (intAmount >= 1_000_000) {
            return "${intAmount / 1_000_000} triệu"
        }
        val formatted = intAmount.toString()
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
        return "${formatted}đ"
    }

    override suspend fun getSeedData(): List<InsuranceProvider> = listOf(
        InsuranceProvider(
            name = "Bảo Minh",
            slug = "bao-minh",
            logo = "https://baominh.com.vn/images/logo.png",
            description = "Tổng Công ty Cổ phần Bảo Minh - Đơn vị bảo hiểm uy tín hàng đầu Việt Nam",
            phone = "1900 558 826",
            email = "cskh@baominh.com.vn",
            website = "https://baominh.com.vn",
            address = "26 Tôn Thất Đạm, Quận 1, TP.HCM",
            rating = 4.7,
            reviewCount = 8500,
            isVerified = true,
            licenseNumber = "BH-001/2020/BTC",
            coverageTypes = listOf("MEDICAL", "TRIP_CANCELLATION", "BAGGAGE", "FLIGHT_DELAY", "ACCIDENT"),
            coverageRegions = listOf("DOMESTIC", "ASIA", "GLOBAL", "SCHENGEN"),
            minPrice = 50000.0,
            maxCoverage = 2_000_000_000.0,
            claimHotline = "1900 558 826",
            claimEmail = "boithuong@baominh.com.vn",
            foundedAt = 694224000000 // 1992-01-01
        ),
        InsuranceProvider(
            name = "Bảo Việt",
            slug = "bao-viet",
            logo = "https://baoviet.com.vn/images/logo.png",
            description = "Tập đoàn Bảo Việt - Tập đoàn Tài chính - Bảo hiểm lớn nhất Việt Nam",
            phone = "1900 55 88 99",
            email = "cskh@baoviet.com.vn",
            website = "https://baoviet.com.vn",
            address = "8 Lê Thái Tổ, Hoàn Kiếm, Hà Nội",
            rating = 4.8,
            reviewCount = 15000,
            isVerified = true,
            licenseNumber = "BH-002/2020/BTC",
            coverageTypes = listOf("MEDICAL", "TRIP_CANCELLATION", "BAGGAGE", "FLIGHT_DELAY", "ACCIDENT", "COVID"),
            coverageRegions = listOf("DOMESTIC", "ASIA", "GLOBAL", "SCHENGEN"),
            minPrice = 45000.0,
            maxCoverage = 3_000_000_000.0,
            claimHotline = "1900 55 88 99",
            claimEmail = "boithuong@baoviet.com.vn",
            foundedAt = -157766400000 // 1965-01-01
        ),
        InsuranceProvider(
            name = "PVI",
            slug = "pvi",
            logo = "https://pvi.com.vn/images/logo.png",
            description = "Tổng Công ty Bảo hiểm PVI - Bảo hiểm phi nhân thọ hàng đầu",
            phone = "1900 54 54 01",
            email = "info@pvi.com.vn",
            website = "https://pvi.com.vn",
            address = "157 Võ Văn Tần, Quận 3, TP.HCM",
            rating = 4.6,
            reviewCount = 6200,
            isVerified = true,
            licenseNumber = "BH-015/2020/BTC",
            coverageTypes = listOf("MEDICAL", "TRIP_CANCELLATION", "BAGGAGE", "ACCIDENT"),
            coverageRegions = listOf("DOMESTIC", "ASIA", "GLOBAL"),
            minPrice = 55000.0,
            maxCoverage = 1_500_000_000.0,
            claimHotline = "1900 54 54 01",
            claimEmail = "claims@pvi.com.vn",
            foundedAt = 788918400000 // 1995-01-01
        ),
        InsuranceProvider(
            name = "Liberty Insurance",
            slug = "liberty",
            logo = "https://libertyinsurance.com.vn/images/logo.png",
            description = "Liberty Insurance Vietnam - Bảo hiểm quốc tế với tiêu chuẩn toàn cầu",
            phone = "1800 588 900",
            email = "cskh@libertyinsurance.com.vn",
            website = "https://libertyinsurance.com.vn",
            address = "15-17 Nguyễn Siêu, Quận 1, TP.HCM",
            rating = 4.5,
            reviewCount = 4500,
            isVerified = true,
            licenseNumber = "BH-025/2020/BTC",
            coverageTypes = listOf("MEDICAL", "TRIP_CANCELLATION", "BAGGAGE", "FLIGHT_DELAY", "ACCIDENT", "COVID"),
            coverageRegions = listOf("DOMESTIC", "ASIA", "GLOBAL", "SCHENGEN"),
            minPrice = 70000.0,
            maxCoverage = 5_000_000_000.0,
            claimHotline = "1800 588 900",
            claimEmail = "claims@libertyinsurance.com.vn",
            foundedAt = 1136073600000 // 2006-01-01
        )
    )
}
