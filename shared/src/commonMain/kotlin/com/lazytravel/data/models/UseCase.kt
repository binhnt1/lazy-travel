package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class UseCase(
    val icon: String = "",
    val title: String = "",
    val description: String = "",
    val gradient: String = "",
    val features: List<String> = listOf(),
    val order: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return Json.encodeToString(item as UseCase)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("icon") { required = true; max = 10 }
        text("title") { required = true; max = 100 }
        text("description") { required = true; max = 300 }
        text("gradient") { required = true; max = 100 }
        json("features") { required = true }
        number("order") { required = true; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<UseCase> = listOf(
        UseCase(
            icon = "👥",
            title = "Nhóm bạn thân",
            description = "Vote điểm đến dân chủ, không ai phải chịu thiệt. Chia tiền minh bạch, giữ tình bạn lâu dài.",
            gradient = "linear-gradient(135deg, #667EEA, #764BA2)",
            features = listOf("Vote công bằng", "Chia bill tự động", "Album nhóm"),
            order = 1
        ),
        UseCase(
            icon = "👨‍👩‍👧‍👦",
            title = "Gia đình",
            description = "Lên kế hoạch phù hợp với mọi lứa tuổi. Quản lý ngân sách rõ ràng, lịch trình linh hoạt cho cả nhà.",
            gradient = "linear-gradient(135deg, #F093FB, #F5576C)",
            features = listOf("An toàn cho trẻ", "Quản lý chi phí", "Lưu kỷ niệm"),
            order = 2
        ),
        UseCase(
            icon = "💼",
            title = "Team Building Công ty",
            description = "Tổ chức team building chuyên nghiệp. Tracking ngân sách, phân công công việc, báo cáo chi tiết.",
            gradient = "linear-gradient(135deg, #4FACFE, #00F2FE)",
            features = listOf("Quản lý đội nhóm", "Báo cáo chi phí", "Lên lịch pro"),
            order = 3
        ),
        UseCase(
            icon = "💑",
            title = "Cặp đôi",
            description = "Lên kế hoạch honeymoon hoặc kỷ niệm lãng mạn. Lưu giữ từng khoảnh khắc đáng nhớ của hai người.",
            gradient = "linear-gradient(135deg, #FA709A, #FEE140)",
            features = listOf("Riêng tư", "Lãng mạn", "Album couple"),
            order = 4
        )
    )
}