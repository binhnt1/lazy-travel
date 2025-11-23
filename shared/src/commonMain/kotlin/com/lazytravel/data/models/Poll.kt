package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Poll Model - Generic voting system
 * Có thể dùng cho: Post (social feed), Trip (destination voting), Buddy (schedule/activity voting)
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Poll(
    // Polymorphic relations (only ONE should be filled)
    @EncodeDefault val postId: String = "",              // For social feed posts
    @EncodeDefault val tripId: String = "",              // For Trip destination voting
    @EncodeDefault val buddyId: String = "",             // For Buddy activity/schedule voting

    // Poll info
    @EncodeDefault val question: String = "",
    @EncodeDefault val allowMultipleVotes: Boolean = false,
    @EncodeDefault val endsAt: String = "",
    @EncodeDefault val totalVotes: Int = 0,

    // Metadata
    @EncodeDefault val pollType: String = "POST"         // "POST", "TRIP", "BUDDY"
) : BaseModel() {

    /**
     * Validate that only one relation is filled
     */
    fun isValid(): Boolean {
        val relations = listOf(postId, tripId, buddyId).filter { it.isNotEmpty() }
        return relations.size == 1
    }

    /**
     * Get entity type and ID
     */
    fun getEntityInfo(): Pair<String, String>? {
        return when {
            postId.isNotEmpty() -> "POST" to postId
            tripId.isNotEmpty() -> "TRIP" to tripId
            buddyId.isNotEmpty() -> "BUDDY" to buddyId
            else -> null
        }
    }

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Poll)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        // Polymorphic relations (only one should be filled)
        relation("postId") {
            required = false
            collectionId = "posts"
            cascadeDelete = true
        }
        relation("tripId") {
            required = false
            collectionId = Trip().collectionName()
            cascadeDelete = true
        }
        relation("buddyId") {
            required = false
            collectionId = Buddy().collectionName()
            cascadeDelete = true
        }

        // Poll info
        text("question") { required = true; max = 500 }
        bool("allowMultipleVotes") { required = false }
        text("endsAt") { required = false; max = 50 }
        number("totalVotes") { required = false; min = 0.0; onlyInt = true }

        // Metadata
        text("pollType") { required = true; max = 20 }
    }

    override suspend fun getSeedData(): List<Poll> = listOf(
        Poll(
            postId = "post6",
            question = "Tháng 12 này nên đi đâu?",
            allowMultipleVotes = false,
            endsAt = "2025-12-01T00:00:00Z",
            totalVotes = 235,
            pollType = "POST"
        ),
        Poll(
            postId = "poll_post_2",
            question = "Bạn thích loại hình du lịch nào nhất?",
            allowMultipleVotes = true,
            endsAt = "2025-12-15T00:00:00Z",
            totalVotes = 156,
            pollType = "POST"
        ),
        Poll(
            postId = "poll_post_3",
            question = "Ngân sách cho 1 chuyến du lịch 4N3Đ của bạn là bao nhiêu?",
            allowMultipleVotes = false,
            endsAt = "",
            totalVotes = 342,
            pollType = "POST"
        )
    )
}

