package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class Stat(
    val number: String = "",
    val label: String = "",
    val sublabel: String = "",
    val order: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Stat)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("number") { required = true; max = 20 }
        text("label") { required = true; max = 100 }
        text("sublabel") { required = false; max = 200 }
        number("order") { required = true; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<Stat> = listOf(
        Stat(
            number = "50K+",
            label = "Người dùng",
            sublabel = "Đang sử dụng Lazy Travel",
            order = 1
        ),
        Stat(
            number = "120K+",
            label = "Chuyến đi",
            sublabel = "Đã được lên kế hoạch",
            order = 2
        ),
        Stat(
            number = "4.8⭐",
            label = "Đánh giá",
            sublabel = "Từ 12K+ reviews",
            order = 3
        ),
        Stat(
            number = "98%",
            label = "Hài lòng",
            sublabel = "Sẽ giới thiệu cho bạn bè",
            order = 4
        )
    )
}
