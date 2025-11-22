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
    val photoUrl: String = "",
    val checkInsCount: Int = 0
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
        text("photoUrl") { required = false; max = 500 }
        number("checkInsCount") { required = false; onlyInt = true }
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
                photoUrl = "https://images.unsplash.com/photo-1555880679-c2d074c4f026?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1555880679-c2d074c4f026?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1555880679-c2d074c4f026?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1569163139394-de4798aa62b6?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1578986849445-9d0e4c7c4a1e?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=800&h=600&fit=crop",
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
                photoUrl = "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=800&h=600&fit=crop",
                checkInsCount = 11000
            )
        )
    }
}

