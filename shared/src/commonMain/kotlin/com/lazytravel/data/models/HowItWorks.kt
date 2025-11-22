package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class HowItWork(
    val order: Int = 0,
    val icon: String = "",
    val title: String = "",
    val description: String = "",
    val badge: String = "",
    val accentColor: String = "#667EEA"
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as HowItWork)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        number("order") { required = true; min = 1.0; onlyInt = true }
        text("icon") { required = true; max = 10 }
        text("title") { required = true; max = 150 }
        text("description") { required = true; max = 300 }
        text("badge") { required = true; max = 40 }
        text("accentColor") { required = true; max = 10 }
    }

    override suspend fun getSeedData(): List<HowItWork> = listOf(
        HowItWork(
            order = 1,
            icon = "🎯",
            title = "Tạo chuyến đi",
            description = "Thêm bạn bè, đặt ngân sách và bắt đầu",
            badge = "30 giây",
            accentColor = "#667EEA"
        ),
        HowItWork(
            order = 2,
            icon = "🗳️",
            title = "Vote điểm đến",
            description = "Mọi người bỏ phiếu, AI chọn tự động",
            badge = "Dân chủ",
            accentColor = "#4ECDC4"
        ),
        HowItWork(
            order = 3,
            icon = "📅",
            title = "Lên lịch trình",
            description = "Timeline chi tiết, chia chi phí tự động",
            badge = "Tự động",
            accentColor = "#FF6B35"
        ),
        HowItWork(
            order = 4,
            icon = "📸",
            title = "Tận hưởng & Chia sẻ",
            description = "Đăng ảnh, nhận huy hiệu thành tích",
            badge = "Vui vẻ!",
            accentColor = "#F093FB"
        )
    )
}
