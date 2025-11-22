package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class Feature(
    val icon: String = "",
    val title: String = "",
    val order: Int = 0,
    val description: String = ""
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Feature)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("icon") { required = true; max = 10 }
        text("title") { required = true; max = 100 }
        text("description") { required = true; max = 200 }
        number("order") { required = true; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<Feature> = listOf(
        Feature("🗳️", "Vote Điểm Đến", 1, "Mọi người bỏ phiếu, hệ thống tự chọn nơi phù hợp nhất"),
        Feature("💰", "Chia Chi Phí", 2, "Tính toán tự động, thanh toán công bằng"),
        Feature("📅", "Lịch Trình Chi Tiết", 3, "Timeline rõ ràng cho từng ngày du lịch"),
        Feature("📸", "Album Chung", 4, "Lưu và chia sẻ ảnh cùng nhóm bạn"),
        Feature("💬", "Chat Nhóm", 5, "Thảo luận mọi quyết định trong app"),
        Feature("🏆", "Huy Hiệu", 6, "Nhận thành tích khi hoàn thành chuyến đi")
    )
}