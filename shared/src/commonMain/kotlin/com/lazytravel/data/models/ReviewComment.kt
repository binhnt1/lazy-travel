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
data class ReviewComment(
    @EncodeDefault val reviewId: String = "",
    @EncodeDefault val userId: String = "",
    @EncodeDefault val content: String = "",
    @EncodeDefault val parentCommentId: String? = null,
    @EncodeDefault val likesCount: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as ReviewComment)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("reviewId") {
            required = true
            collectionId = "reviews"
            cascadeDelete = true
        }
        relation("userId") {
            required = true
            collectionId = "users"
            cascadeDelete = false
        }
        text("content") { required = true; max = 2000 }
        text("parentCommentId") { required = false; max = 50 }
        number("likesCount") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<ReviewComment> {
        val usersRepo = BaseRepository<User>()
        val reviewsRepo = BaseRepository<Review>()

        val reviews = reviewsRepo.getRecords<Review>().getOrNull() ?: emptyList()
        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()

        if (reviews.isEmpty() || users.isEmpty()) {
            println("⚠️ No reviews or users found for seeding comments")
            return emptyList()
        }

        val comments = mutableListOf<ReviewComment>()
        val commentTexts = listOf(
            "Cảm ơn bạn đã chia sẻ! Rất hữu ích",
            "Mình cũng đang định đi nơi này, thanks for the info!",
            "Bạn đi vào tháng nào vậy?",
            "Có gì cần lưu ý không bạn?",
            "Giá vé bao nhiêu vậy bạn?",
            "Địa điểm này có gần trung tâm không?",
            "Đi từ sân bay bao lâu?",
            "Có chỗ ăn uống gần đó không?",
            "Review rất chi tiết và hữu ích!",
            "Cảm ơn bạn đã chia sẻ kinh nghiệm!",
            "Mình vừa đi về, đồng ý với bạn!",
            "Ảnh đẹp quá! Bạn chụp bằng máy gì?",
            "Nơi này có đông người không?",
            "Thời tiết thế nào vậy bạn?",
            "Có nên đặt tour không?"
        )

        reviews.forEach { review ->
            val numComments = (0..5).random()

            repeat(numComments) {
                val user = users.random()
                val comment = ReviewComment(
                    reviewId = review.id,
                    userId = user.id,
                    content = commentTexts.random(),
                    likesCount = (0..20).random()
                )

                comments.add(comment)
            }
        }

        println("✅ Generated ${comments.size} review comments")
        return comments
    }
}

