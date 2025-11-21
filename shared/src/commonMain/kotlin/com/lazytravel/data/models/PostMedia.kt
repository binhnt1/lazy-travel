package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class PostMedia(
    val postId: String = "",
    val mediaUrl: String = "",
    val mediaType: String = "IMAGE",
    val thumbnailUrl: String = "",
    val orderIndex: Int = 0,
    val width: Int = 0,
    val height: Int = 0,
    val duration: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as PostMedia)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("postId") {
            required = true
            collectionId = "posts"
            cascadeDelete = true
        }
        text("mediaUrl") { required = true; max = 500 }
        text("mediaType") { required = true; max = 20 }
        text("thumbnailUrl") { required = false; max = 500 }
        number("orderIndex") { required = true; min = 0.0; onlyInt = true }
        number("width") { required = false; min = 0.0; onlyInt = true }
        number("height") { required = false; min = 0.0; onlyInt = true }
        number("duration") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<PostMedia> {
        val postsRepo = BaseRepository<Post>()
        val posts = postsRepo.getRecords<Post>().getOrNull() ?: emptyList()

        if (posts.isEmpty()) {
            return emptyList()
        }

        val medias = mutableListOf<PostMedia>()

        val imageUrls = listOf(
            "https://example.com/travel1.jpg",
            "https://example.com/travel2.jpg",
            "https://example.com/travel3.jpg",
            "https://example.com/travel4.jpg",
            "https://example.com/travel5.jpg",
            "https://example.com/travel6.jpg",
            "https://example.com/travel7.jpg",
            "https://example.com/travel8.jpg"
        )

        val videoUrls = listOf(
            "https://example.com/video1.mp4",
            "https://example.com/video2.mp4",
            "https://example.com/video3.mp4",
            "https://example.com/video4.mp4"
        )

        // Tạo media cho các posts ALBUM và VIDEO
        for (post in posts) {
            when (post.postType) {
                "ALBUM" -> {
                    // Mỗi album có 3-8 ảnh
                    val numImages = (3..8).random()
                    repeat(numImages) { index ->
                        medias.add(
                            PostMedia(
                                postId = post.id,
                                mediaUrl = imageUrls.random(),
                                mediaType = "IMAGE",
                                orderIndex = index,
                                width = listOf(1080, 1920, 1440).random(),
                                height = listOf(1350, 1080, 1920).random()
                            )
                        )
                    }
                }
                "SINGLE_IMAGE" -> {
                    // Chỉ 1 ảnh
                    medias.add(
                        PostMedia(
                            postId = post.id,
                            mediaUrl = imageUrls.random(),
                            mediaType = "IMAGE",
                            orderIndex = 0,
                            width = listOf(1080, 1920, 1440).random(),
                            height = listOf(1350, 1080, 1920).random()
                        )
                    )
                }
                "VIDEO" -> {
                    // 1 video với thumbnail
                    medias.add(
                        PostMedia(
                            postId = post.id,
                            mediaUrl = videoUrls.random(),
                            mediaType = "VIDEO",
                            thumbnailUrl = imageUrls.random(),
                            orderIndex = 0,
                            width = 1920,
                            height = 1080,
                            duration = (15..300).random() // 15s - 5 phút
                        )
                    )
                }
                "CHECK_IN" -> {
                    // Check-in có 1 ảnh
                    medias.add(
                        PostMedia(
                            postId = post.id,
                            mediaUrl = imageUrls.random(),
                            mediaType = "IMAGE",
                            orderIndex = 0,
                            width = 1080,
                            height = 1350
                        )
                    )
                }
            }
        }

        return medias
    }
}

