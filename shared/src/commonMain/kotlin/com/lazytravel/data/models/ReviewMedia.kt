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
data class ReviewMedia(
    @EncodeDefault val reviewId: String = "",
    @EncodeDefault val mediaUrl: String = "",
    @EncodeDefault val mediaType: String = "IMAGE",
    @EncodeDefault val thumbnailUrl: String? = null,
    @EncodeDefault val orderIndex: Int = 0,
    @EncodeDefault val width: Int? = null,
    @EncodeDefault val height: Int? = null,
    @EncodeDefault val duration: Int? = null
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as ReviewMedia)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("reviewId") {
            required = true
            collectionId = "reviews"
            cascadeDelete = true
        }
        text("mediaUrl") { required = true; max = 500 }
        text("mediaType") { required = true; max = 10 }
        text("thumbnailUrl") { required = false; max = 500 }
        number("orderIndex") { required = true; min = 0.0; onlyInt = true }
        number("width") { required = false; min = 0.0; onlyInt = true }
        number("height") { required = false; min = 0.0; onlyInt = true }
        number("duration") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<ReviewMedia> {
        val reviewsRepo = BaseRepository<Review>()
        val reviews = reviewsRepo.getRecords<Review>().getOrNull() ?: emptyList()

        if (reviews.isEmpty()) {
            println("⚠️ No reviews found for seeding media")
            return emptyList()
        }

        val mediaList = mutableListOf<ReviewMedia>()
        val imageUrls = listOf(
            "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1426604966848-d7adac402bff?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1433086966358-54859d0ed716?w=800&h=600&fit=crop"
        )

        reviews.forEach { review ->
            val numMedia = (0..4).random()

            repeat(numMedia) { index ->
                val media = ReviewMedia(
                    reviewId = review.id,
                    mediaUrl = imageUrls.random(),
                    mediaType = "IMAGE",
                    thumbnailUrl = imageUrls.random(),
                    orderIndex = index,
                    width = listOf(800, 1024, 1200).random(),
                    height = listOf(600, 768, 900).random()
                )
                mediaList.add(media)
            }
        }

        println("✅ Generated ${mediaList.size} review media items")
        return mediaList
    }
}

