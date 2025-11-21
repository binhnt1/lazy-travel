package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class TripStory(
    val postId: String = "",
    val tripId: String = "",
    val title: String = "",
    val coverImage: String = "",
    val totalSlides: Int = 0,
    val viewsCount: Int = 0,
    val likesCount: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as TripStory)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("postId") {
            required = true
            collectionId = "posts"
            cascadeDelete = true
        }
        text("tripId") { required = false; max = 50 }
        text("title") { required = true; max = 200 }
        text("coverImage") { required = true; max = 500 }
        number("totalSlides") { required = true; min = 1.0; onlyInt = true }
        number("viewsCount") { required = false; min = 0.0; onlyInt = true }
        number("likesCount") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<TripStory> = emptyList()


//    override suspend fun getSeedData(): List<TripStory> = listOf(
//        TripStory(
//            postId = "story_post_1",
//            tripId = "trip123",
//            title = "4 ngày 3 đêm ở Đà Lạt",
//            coverImage = "https://example.com/dalat_story_cover.jpg",
//            totalSlides = 12,
//            viewsCount = 3456,
//            likesCount = 234
//        ),
//        TripStory(
//            postId = "story_post_2",
//            tripId = "trip456",
//            title = "Khám phá Phú Quốc cùng hội bạn thân",
//            coverImage = "https://example.com/phuquoc_story_cover.jpg",
//            totalSlides = 15,
//            viewsCount = 5678,
//            likesCount = 456
//        ),
//        TripStory(
//            postId = "story_post_3",
//            tripId = "trip789",
//            title = "Hành trình chinh phục Sapa",
//            coverImage = "https://example.com/sapa_story_cover.jpg",
//            totalSlides = 10,
//            viewsCount = 2345,
//            likesCount = 178
//        )
//    )
}

@Serializable
data class StorySlide(
    val storyId: String = "",
    val slideType: String = "",
    val content: String = "",
    val mediaUrl: String = "",
    val caption: String = "",
    val orderIndex: Int = 0,
    val duration: Int = 5
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as StorySlide)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("storyId") {
            required = true
            collectionId = "tripstories"
            cascadeDelete = true
        }
        text("slideType") { required = true; max = 20 }
        text("content") { required = false; max = 1000 }
        text("mediaUrl") { required = false; max = 500 }
        text("caption") { required = false; max = 500 }
        number("orderIndex") { required = true; min = 0.0; onlyInt = true }
        number("duration") { required = false; min = 1.0; max = 30.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<StorySlide> = emptyList()
    // TODO: Requires real story IDs
    /*
    override suspend fun getSeedData(): List<StorySlide> = listOf(
        StorySlide(
            storyId = "story1",
            slideType = "IMAGE",
            mediaUrl = "https://example.com/dalat_day1.jpg",
            caption = "Ngày 1: Đến Đà Lạt vào buổi chiều, checkin khách sạn",
            orderIndex = 0,
            duration = 5
        ),
        StorySlide(
            storyId = "story1",
            slideType = "IMAGE",
            mediaUrl = "https://example.com/dalat_day2_morning.jpg",
            caption = "Ngày 2 sáng: Crazy House - kiến trúc độc đáo 🏰",
            orderIndex = 1,
            duration = 5
        ),
        StorySlide(
            storyId = "story1",
            slideType = "VIDEO",
            mediaUrl = "https://example.com/dalat_xq.mp4",
            caption = "Chiều đi làng XQ - thêu tay tuyệt đẹp",
            orderIndex = 2,
            duration = 10
        ),
        StorySlide(
            storyId = "story1",
            slideType = "LOCATION",
            content = """{"placeName":"Cafe Cối Xay Gió","latitude":11.9404,"longitude":108.4583}""",
            mediaUrl = "https://example.com/cafe.jpg",
            caption = "Ngày 3: Check-in cafe view đẹp ☕",
            orderIndex = 3,
            duration = 5
        ),
        StorySlide(
            storyId = "story2",
            slideType = "IMAGE",
            mediaUrl = "https://example.com/phuquoc_beach.jpg",
            caption = "Bãi Sao - nước biển trong xanh 🏖️",
            orderIndex = 0,
            duration = 5
        ),
        StorySlide(
            storyId = "story2",
            slideType = "VIDEO",
            mediaUrl = "https://example.com/phuquoc_diving.mp4",
            caption = "Lặn ngắm san hô tại Hòn Thơm 🐠",
            orderIndex = 1,
            duration = 15
        )
    )
    */
}

