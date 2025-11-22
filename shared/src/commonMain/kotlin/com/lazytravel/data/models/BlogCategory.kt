package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class BlogCategory(
    @EncodeDefault val name: String = "",
    @EncodeDefault val slug: String = "",
    @EncodeDefault val description: String = "",
    @EncodeDefault val color: String = "",
    @EncodeDefault val emoji: String = "",
    @EncodeDefault val postCount: Int = 0,
    @EncodeDefault val order: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as BlogCategory)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("name") { required = true; max = 50 }
        text("slug") { required = true; max = 50 }
        text("description") { required = false; max = 200 }
        text("color") { required = false; max = 20 }
        text("emoji") { required = false; max = 10 }
        number("postCount") { required = false; min = 0.0; onlyInt = true }
        number("order") { required = false; min = 0.0; max = 100.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<BlogCategory> {
        return listOf(
            BlogCategory(
                name = "HƯỚNG DẪN",
                slug = "huong-dan",
                description = "Cẩm nang và hướng dẫn chi tiết cho chuyến du lịch",
                color = "#4ECDC4",
                emoji = "🗺️",
                postCount = 0,
                order = 1
            ),
            BlogCategory(
                name = "TIẾT KIỆM",
                slug = "tiet-kiem",
                description = "Mẹo tiết kiệm cho chuyến đi du lịch",
                color = "#FA709A",
                emoji = "💰",
                postCount = 0,
                order = 2
            ),
            BlogCategory(
                name = "NHIẾP ẢNH",
                slug = "nhiep-anh",
                description = "Góc chụp đẹp và tips nhiếp ảnh du lịch",
                color = "#667EEA",
                emoji = "📸",
                postCount = 0,
                order = 3
            ),
            BlogCategory(
                name = "ẨM THỰC",
                slug = "am-thuc",
                description = "Khám phá ẩm thực địa phương",
                color = "#FF9800",
                emoji = "🍜",
                postCount = 0,
                order = 4
            ),
            BlogCategory(
                name = "PHIÊU LƯU",
                slug = "phieu-luu",
                description = "Trải nghiệm mạo hiểm và khám phá",
                color = "#11998E",
                emoji = "⛰️",
                postCount = 0,
                order = 5
            ),
            BlogCategory(
                name = "VĂN HÓA",
                slug = "van-hoa",
                description = "Tìm hiểu văn hóa bản địa",
                color = "#E91E63",
                emoji = "🏮",
                postCount = 0,
                order = 6
            )
        )
    }

    companion object {
        fun getSeedData(): List<BlogCategory> {
            return listOf(
                BlogCategory(
                    name = "HƯỚNG DẪN",
                    slug = "huong-dan",
                    description = "Cẩm nang và hướng dẫn chi tiết cho chuyến du lịch",
                    color = "#4ECDC4",
                    emoji = "🗺️",
                    postCount = 0,
                    order = 1
                ),
                BlogCategory(
                    name = "TIẾT KIỆM",
                    slug = "tiet-kiem",
                    description = "Mẹo tiết kiệm cho chuyến đi du lịch",
                    color = "#FA709A",
                    emoji = "💰",
                    postCount = 0,
                    order = 2
                ),
                BlogCategory(
                    name = "NHIẾP ẢNH",
                    slug = "nhiep-anh",
                    description = "Góc chụp đẹp và tips nhiếp ảnh du lịch",
                    color = "#667EEA",
                    emoji = "📸",
                    postCount = 0,
                    order = 3
                ),
                BlogCategory(
                    name = "ẨM THỰC",
                    slug = "am-thuc",
                    description = "Khám phá ẩm thực địa phương",
                    color = "#FF9800",
                    emoji = "🍜",
                    postCount = 0,
                    order = 4
                ),
                BlogCategory(
                    name = "PHIÊU LƯU",
                    slug = "phieu-luu",
                    description = "Trải nghiệm mạo hiểm và khám phá",
                    color = "#11998E",
                    emoji = "⛰️",
                    postCount = 0,
                    order = 5
                ),
                BlogCategory(
                    name = "VĂN HÓA",
                    slug = "van-hoa",
                    description = "Tìm hiểu văn hóa bản địa",
                    color = "#E91E63",
                    emoji = "🏮",
                    postCount = 0,
                    order = 6
                )
            )
        }
    }
}
