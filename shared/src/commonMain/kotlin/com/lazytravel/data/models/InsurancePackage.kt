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
data class InsurancePackage(
    // Relation
    @EncodeDefault val insuranceProviderId: String = "",  // → InsuranceProvider

    // Package info
    @EncodeDefault val name: String = "",           // "Cơ bản", "Tiêu chuẩn", "Cao cấp"
    @EncodeDefault val slug: String = "",           // "co-ban", "tieu-chuan", "cao-cap"
    @EncodeDefault val description: String = "",

    // Coverage & Pricing
    @EncodeDefault val coverage: Double = 0.0,      // 1,000,000,000đ (1 tỷ)
    @EncodeDefault val pricePerDay: Double = 0.0,   // 50,000đ/ngày

    // Benefits
    @EncodeDefault val benefits: List<String>? = null,  // ["Y tế khẩn cấp 24/7", "Bồi thường hành lý 100%"]

    // Badges: PHỔ BIẾN, LUXURY, HOT, NEW
    @EncodeDefault val badges: List<String>? = null,

    // Metadata
    @EncodeDefault val displayOrder: Int = 0,       // 1, 2, 3 (để sắp xếp)
) : BaseModel() {

    // Expanded relation
    @kotlinx.serialization.Transient
    var expandedInsuranceProvider: InsuranceProvider? = null

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as InsurancePackage)
    }

    fun populateExpandedData() {
        val expandData = expand ?: return

        expandData["insuranceProviderId"]?.let { providerJson ->
            try {
                expandedInsuranceProvider = json.decodeFromJsonElement(InsuranceProvider.serializer(), providerJson)
            } catch (_: Exception) {}
        }
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("insuranceProviderId") {
            required = true
            collectionId = InsuranceProvider().collectionName()
            cascadeDelete = false
            maxSelect = 1
        }
        text("name") { required = true; max = 100 }
        text("slug") { required = true; max = 100 }
        text("description") { required = false; max = 1000 }
        number("coverage") { required = true; min = 0.0 }
        number("pricePerDay") { required = true; min = 0.0 }
        json("benefits") { required = false }
        json("badges") { required = false }
        number("displayOrder") { required = false; onlyInt = true }
    }

    override suspend fun getSeedData(): List<InsurancePackage> {
        // Get insurance providers first
        val providerRepo = BaseRepository<InsuranceProvider>()
        val providers = providerRepo.getRecords<InsuranceProvider>().getOrNull() ?: emptyList()
        val providerMap = providers.associateBy { it.slug }

        val baoVietId = providerMap["bao-viet"]?.id ?: ""
        val pviId = providerMap["pvi"]?.id ?: ""

        return listOf(
            // Bảo Việt Packages
            InsurancePackage(
                insuranceProviderId = baoVietId,
                name = "Cơ bản",
                slug = "co-ban",
                description = "Gói bảo hiểm cơ bản cho chuyến đi ngắn ngày trong nước",
                coverage = 1_000_000_000.0,  // 1 tỷ VND
                pricePerDay = 50_000.0,
                benefits = listOf(
                    "Y tế khẩn cấp 24/7",
                    "Bồi thường hành lý thất lạc",
                    "Hỗ trợ chi phí y tế cơ bản",
                    "Trợ giúp pháp lý"
                ),
                displayOrder = 1
            ),
            InsurancePackage(
                insuranceProviderId = baoVietId,
                name = "Tiêu chuẩn",
                slug = "tieu-chuan",
                description = "Gói bảo hiểm phổ biến nhất, phù hợp cho du lịch quốc tế",
                coverage = 2_000_000_000.0,  // 2 tỷ VND
                pricePerDay = 80_000.0,
                benefits = listOf(
                    "Y tế khẩn cấp 24/7 toàn cầu",
                    "Bồi thường hành lý 100%",
                    "Hủy/Hoãn chuyến đi",
                    "Bảo hiểm tai nạn",
                    "Hỗ trợ pháp lý quốc tế"
                ),
                badges = listOf(BADGE_POPULAR),
                displayOrder = 2
            ),
            InsurancePackage(
                insuranceProviderId = baoVietId,
                name = "Cao cấp",
                slug = "cao-cap",
                description = "Gói bảo hiểm toàn diện cho chuyến đi xa và dài ngày",
                coverage = 5_000_000_000.0,  // 5 tỷ VND
                pricePerDay = 150_000.0,
                benefits = listOf(
                    "Y tế khẩn cấp VIP 24/7",
                    "Bồi thường hành lý cao cấp",
                    "Hủy/Hoãn/Delay chuyến bay",
                    "Bảo hiểm tai nạn toàn diện",
                    "Hỗ trợ pháp lý quốc tế",
                    "Dịch vụ y tế tận nơi",
                    "Đưa đón khẩn cấp"
                ),
                badges = listOf(BADGE_LUXURY),
                displayOrder = 3
            ),

            // PVI Packages
            InsurancePackage(
                insuranceProviderId = pviId,
                name = "Du lịch An toàn",
                slug = "du-lich-an-toan",
                description = "Gói bảo hiểm tiết kiệm cho du lịch nội địa",
                coverage = 800_000_000.0,
                pricePerDay = 40_000.0,
                benefits = listOf(
                    "Y tế khẩn cấp trong nước",
                    "Bồi thường hành lý",
                    "Hỗ trợ y tế cơ bản"
                ),
                displayOrder = 1
            ),
            InsurancePackage(
                insuranceProviderId = pviId,
                name = "Du lịch Quốc tế",
                slug = "du-lich-quoc-te",
                description = "Bảo vệ toàn diện cho chuyến đi quốc tế",
                coverage = 3_000_000_000.0,
                pricePerDay = 100_000.0,
                benefits = listOf(
                    "Y tế khẩn cấp toàn cầu",
                    "Bồi thường hành lý 100%",
                    "Hủy/Hoãn chuyến",
                    "Bảo hiểm tai nạn",
                    "Trợ giúp đa ngôn ngữ"
                ),
                badges = listOf(BADGE_HOT),
                displayOrder = 2
            ),
            InsurancePackage(
                insuranceProviderId = pviId,
                name = "Du lịch Premium",
                slug = "du-lich-premium",
                description = "Gói bảo hiểm cao cấp nhất với quyền lợi tối đa",
                coverage = 10_000_000_000.0,  // 10 tỷ VND
                pricePerDay = 250_000.0,
                benefits = listOf(
                    "Y tế khẩn cấp VIP không giới hạn",
                    "Bồi thường hành lý cao cấp",
                    "Hủy/Hoãn/Delay đầy đủ",
                    "Bảo hiểm tai nạn VIP",
                    "Concierge service 24/7",
                    "Y tế tận nơi ưu tiên",
                    "Đưa đón y tế quốc tế"
                ),
                badges = listOf(BADGE_LUXURY, BADGE_NEW),
                displayOrder = 3
            )
        )
    }

    companion object {
        // Badge constants
        const val BADGE_POPULAR = "PHỔ BIẾN"
        const val BADGE_LUXURY = "LUXURY"
        const val BADGE_HOT = "HOT"
        const val BADGE_NEW = "NEW"
    }
}
