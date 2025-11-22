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
data class ReviewLike(
    @EncodeDefault val reviewId: String = "",
    @EncodeDefault val userId: String = "",
    @EncodeDefault val helpful: Boolean = false
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as ReviewLike)
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
        bool("helpful") { required = false }
    }

    override suspend fun getSeedData(): List<ReviewLike> {
        val usersRepo = BaseRepository<User>()
        val reviewsRepo = BaseRepository<Review>()

        val reviews = reviewsRepo.getRecords<Review>().getOrNull() ?: emptyList()
        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()

        if (reviews.isEmpty() || users.isEmpty()) {
            println("⚠️ No reviews or users found for seeding likes")
            return emptyList()
        }

        val likes = mutableListOf<ReviewLike>()
        val createdPairs = mutableSetOf<String>()

        reviews.forEach { review ->
            val numLikes = (0..10).random()

            repeat(numLikes) {
                val user = users.random()
                val pairKey = "${review.id}-${user.id}"

                if (!createdPairs.contains(pairKey)) {
                    val like = ReviewLike(
                        reviewId = review.id,
                        userId = user.id,
                        helpful = (0..100).random() < 50
                    )

                    likes.add(like)
                    createdPairs.add(pairKey)
                }
            }
        }

        println("✅ Generated ${likes.size} review likes")
        return likes
    }
}

