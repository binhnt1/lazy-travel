package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable
import kotlin.collections.emptyList

@Serializable
data class City(
    val name: String = "",
    val countryId: String = "", // Relation to Country
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String = "",
    val population: Int = 0,
    val isCapital: Boolean = false,
    val isPopular: Boolean = false,
    val description: String = "",
    val imageUrl: String = "",
) : BaseModel() {

    @kotlinx.serialization.Transient
    var expandedCountry: Country? = null

    override fun serializeToJson(item: BaseModel): String = json.encodeToString(serializer(), item as City)
    fun populateExpandedData() {
        val expandData = expand ?: return
        expandData["countryId"]?.let { countryJson ->
            try {
                val country = json.decodeFromJsonElement(Country.serializer(), countryJson)
                expandedCountry = country
            } catch (_: Exception) {
            }
        }
    }

    override suspend fun getSeedData(): List<City> {
        // Get all countries first to link cities
        val countryRepo = BaseRepository<Country>()
        val countries = countryRepo.getRecords<Country>().getOrNull() ?: emptyList()

        val vietnamId = countries.find { it.name == "Vietnam" }?.id ?: ""
        val thailandId = countries.find { it.name == "Thailand" }?.id ?: ""
        val japanId = countries.find { it.name == "Japan" }?.id ?: ""

        return listOf(
            // Vietnam Cities
            City(
                name = "Hanoi",
                countryId = vietnamId,
                latitude = 21.0285,
                longitude = 105.8542,
                timezone = "Asia/Ho_Chi_Minh",
                population = 8053663,
                isCapital = true,
                isPopular = true,
                description = "Thủ đô của Việt Nam, nơi lưu giữ nhiều di sản văn hóa lịch sử",
                imageUrl = "https://images.unsplash.com/photo-1555880679-c2d074c4f026?w=800&h=600&fit=crop"
            ),
            City(
                name = "Ho Chi Minh City",
                countryId = vietnamId,
                latitude = 10.8231,
                longitude = 106.6297,
                timezone = "Asia/Ho_Chi_Minh",
                population = 9077158,
                isCapital = false,
                isPopular = true,
                description = "Thành phố năng động nhất Việt Nam với nhiều điểm tham quan",
                imageUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"
            ),
            City(
                name = "Da Nang",
                countryId = vietnamId,
                latitude = 16.0544,
                longitude = 108.2022,
                timezone = "Asia/Ho_Chi_Minh",
                population = 1230000,
                isCapital = false,
                isPopular = true,
                description = "Thành phố biển đẹp với nhiều bãi tắm và điểm du lịch",
                imageUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop"
            ),
            City(
                name = "Hoi An",
                countryId = vietnamId,
                latitude = 15.8794,
                longitude = 108.3350,
                timezone = "Asia/Ho_Chi_Minh",
                population = 152160,
                isCapital = false,
                isPopular = true,
                description = "Phố cổ di sản thế giới với kiến trúc độc đáo",
                imageUrl = "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop"
            ),
            City(
                name = "Nha Trang",
                countryId = vietnamId,
                latitude = 12.2388,
                longitude = 109.1967,
                timezone = "Asia/Ho_Chi_Minh",
                population = 500000,
                isCapital = false,
                isPopular = true,
                description = "Thành phố biển với nhiều hoạt động thể thao nước",
                imageUrl = "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop"
            ),
            City(
                name = "Da Lat",
                countryId = vietnamId,
                latitude = 11.9404,
                longitude = 108.4583,
                timezone = "Asia/Ho_Chi_Minh",
                population = 406105,
                isCapital = false,
                isPopular = true,
                description = "Thành phố ngàn hoa với khí hậu mát mẻ quanh năm",
                imageUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"
            ),
            City(
                name = "Hue",
                countryId = vietnamId,
                latitude = 16.4637,
                longitude = 107.5909,
                timezone = "Asia/Ho_Chi_Minh",
                population = 455230,
                isCapital = false,
                isPopular = true,
                description = "Cố đô Việt Nam với nhiều di tích lịch sử",
                imageUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop"
            ),
            City(
                name = "Phu Quoc",
                countryId = vietnamId,
                latitude = 10.2899,
                longitude = 103.9840,
                timezone = "Asia/Ho_Chi_Minh",
                population = 179480,
                isCapital = false,
                isPopular = true,
                description = "Đảo ngọc với bãi biển đẹp và thiên nhiên hoang sơ",
                imageUrl = "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop"
            ),

            // Thailand Cities
            City(
                name = "Bangkok",
                countryId = thailandId,
                latitude = 13.7563,
                longitude = 100.5018,
                timezone = "Asia/Bangkok",
                population = 10899698,
                isCapital = true,
                isPopular = true,
                description = "Thủ đô năng động của Thái Lan",
                imageUrl = "https://images.unsplash.com/photo-1508009603885-50cf7c579365?w=800&h=600&fit=crop"
            ),
            City(
                name = "Chiang Mai",
                countryId = thailandId,
                latitude = 18.7883,
                longitude = 98.9853,
                timezone = "Asia/Bangkok",
                population = 174235,
                isCapital = false,
                isPopular = true,
                description = "Thành phố cổ kính miền Bắc Thái Lan",
                imageUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop"
            ),
            City(
                name = "Phuket",
                countryId = thailandId,
                latitude = 7.8804,
                longitude = 98.3923,
                timezone = "Asia/Bangkok",
                population = 416582,
                isCapital = false,
                isPopular = true,
                description = "Đảo thiên đường du lịch của Thái Lan",
                imageUrl = "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop"
            ),

            // Japan Cities
            City(
                name = "Tokyo",
                countryId = japanId,
                latitude = 35.6762,
                longitude = 139.6503,
                timezone = "Asia/Tokyo",
                population = 13960000,
                isCapital = true,
                isPopular = true,
                description = "Thủ đô hiện đại của Nhật Bản",
                imageUrl = "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=800&h=600&fit=crop"
            ),
            City(
                name = "Kyoto",
                countryId = japanId,
                latitude = 35.0116,
                longitude = 135.7681,
                timezone = "Asia/Tokyo",
                population = 1475183,
                isCapital = false,
                isPopular = true,
                description = "Cố đô Nhật Bản với nhiều đền chùa",
                imageUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=800&h=600&fit=crop"
            ),
            City(
                name = "Osaka",
                countryId = japanId,
                latitude = 34.6937,
                longitude = 135.5023,
                timezone = "Asia/Tokyo",
                population = 2752412,
                isCapital = false,
                isPopular = true,
                description = "Thành phố ẩm thực của Nhật Bản",
                imageUrl = "https://images.unsplash.com/photo-1590559899731-a382839e5549?w=800&h=600&fit=crop"
            )
        )
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("name") { required = true; max = 100 }
        relation("countryId") {
            required = true
            collectionId = Country().collectionName()
            cascadeDelete = false
            maxSelect = 1
        }
        number("latitude") { required = true }
        number("longitude") { required = true }
        text("timezone") { required = false; max = 50 }
        number("population") { required = false; onlyInt = true }
        bool("isCapital") { required = false }
        bool("isPopular") { required = false }
        text("description") { required = false; max = 1000 }
        text("imageUrl") { required = false; max = 500 }
    }
}

