package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class VideoPost(
    val postId: String = "",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val duration: Int = 0,
    val width: Int = 0,
    val height: Int = 0,
    val quality: String = "",
    val fileSize: Long = 0,
    val viewsCount: Int = 0,
    val watchTimeSeconds: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as VideoPost)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("postId") {
            required = true
            collectionId = "posts"
            cascadeDelete = true
        }
        text("videoUrl") { required = true; max = 500 }
        text("thumbnailUrl") { required = true; max = 500 }
        number("duration") { required = true; min = 0.0; onlyInt = true }
        number("width") { required = false; min = 0.0; onlyInt = true }
        number("height") { required = false; min = 0.0; onlyInt = true }
        text("quality") { required = false; max = 10 }
        number("fileSize") { required = false; min = 0.0; onlyInt = true }
        number("viewsCount") { required = false; min = 0.0; onlyInt = true }
        number("watchTimeSeconds") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<VideoPost> = emptyList()
    // TODO: Requires real post IDs
    /*
    override suspend fun getSeedData(): List<VideoPost> = listOf(
        VideoPost(
            postId = "post5",
            videoUrl = "https://example.com/diving_phuquoc.mp4",
            thumbnailUrl = "https://example.com/diving_thumb.jpg",
            duration = 45,
            width = 1920,
            height = 1080,
            quality = "1080P",
            fileSize = 125000000,
            viewsCount = 1234,
            watchTimeSeconds = 35000
        ),
        VideoPost(
            postId = "video_post_2",
            videoUrl = "https://example.com/dalat_tour.mp4",
            thumbnailUrl = "https://example.com/dalat_tour_thumb.jpg",
            duration = 120,
            width = 1920,
            height = 1080,
            quality = "1080P",
            fileSize = 320000000,
            viewsCount = 2345,
            watchTimeSeconds = 180000
        ),
        VideoPost(
            postId = "video_post_3",
            videoUrl = "https://example.com/food_tour_hanoi.mp4",
            thumbnailUrl = "https://example.com/food_tour_thumb.jpg",
            duration = 180,
            width = 1280,
            height = 720,
            quality = "720P",
            fileSize = 215000000,
            viewsCount = 3456,
            watchTimeSeconds = 450000
        )
    )
    */
}

