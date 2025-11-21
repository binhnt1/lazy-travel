package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class Poll(
    val postId: String = "",
    val question: String = "",
    val allowMultipleVotes: Boolean = false,
    val endsAt: String = "",
    val totalVotes: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Poll)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("postId") {
            required = true
            collectionId = "posts"
            cascadeDelete = true
        }
        text("question") { required = true; max = 500 }
        bool("allowMultipleVotes") { required = false }
        text("endsAt") { required = false; max = 50 }
        number("totalVotes") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<Poll> = listOf(
        Poll(
            postId = "post6",
            question = "Tháng 12 này nên đi đâu?",
            allowMultipleVotes = false,
            endsAt = "2025-12-01T00:00:00Z",
            totalVotes = 235
        ),
        Poll(
            postId = "poll_post_2",
            question = "Bạn thích loại hình du lịch nào nhất?",
            allowMultipleVotes = true,
            endsAt = "2025-12-15T00:00:00Z",
            totalVotes = 156
        ),
        Poll(
            postId = "poll_post_3",
            question = "Ngân sách cho 1 chuyến du lịch 4N3Đ của bạn là bao nhiêu?",
            allowMultipleVotes = false,
            endsAt = "",
            totalVotes = 342
        )
    )
}

