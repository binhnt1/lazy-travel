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
data class BuddyReview(
    @EncodeDefault val buddyId: String = "",              // Relation to Buddy (trip being reviewed)
    @EncodeDefault val userId: String = "",               // Relation to User (reviewer)
    @EncodeDefault val rating: Double = 0.0,              // Overall rating (1-5)
    @EncodeDefault val organizationRating: Double = 0.0,  // Organization & planning (1-5)
    @EncodeDefault val experienceRating: Double = 0.0,    // Trip experience (1-5)
    @EncodeDefault val valueRating: Double = 0.0,         // Value for money (1-5)
    @EncodeDefault val title: String = "",                // Review title
    @EncodeDefault val content: String = "",              // Review content
    @EncodeDefault val photos: List<String> = emptyList(), // Review photos URLs
    @EncodeDefault val likes: Int = 0,                    // Number of likes
    @EncodeDefault val isVerified: Boolean = false,       // Verified review (joined the trip)
    @EncodeDefault val helpful: Int = 0                   // Number of helpful votes
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as BuddyReview)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("buddyId") {
            required = true
            collectionId = Buddy().collectionName()
            cascadeDelete = true
        }
        relation("userId") {
            required = true
            collectionId = User().collectionName()
            cascadeDelete = false
        }
        number("rating") { required = true; min = 1.0; max = 5.0 }
        number("organizationRating") { required = false; min = 1.0; max = 5.0 }
        number("experienceRating") { required = false; min = 1.0; max = 5.0 }
        number("valueRating") { required = false; min = 1.0; max = 5.0 }
        text("title") { required = false; max = 200 }
        text("content") { required = true; max = 2000 }
        json("photos") { required = false }
        number("likes") { required = false; min = 0.0; onlyInt = true }
        bool("isVerified") { required = false }
        number("helpful") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<BuddyReview> {
        // Get buddies and users for relations
        val buddiesRepo = BaseRepository<Buddy>()
        val buddies = buddiesRepo.getRecords<Buddy>().getOrNull() ?: emptyList()

        val usersRepo = BaseRepository<User>()
        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()

        if (buddies.isEmpty() || users.isEmpty()) {
            return emptyList()
        }

        val reviews = mutableListOf<BuddyReview>()

        // Sample review titles and contents
        val reviewTitles = listOf(
            "Chuyến đi tuyệt vời!",
            "Trải nghiệm đáng nhớ",
            "Rất hài lòng với chuyến đi",
            "Chuyến đi ý nghĩa",
            "Tổ chức chuyên nghiệp",
            "Đáng tiền bỏ ra!",
            "Sẽ quay lại lần nữa",
            "Hoàn hảo cho kỳ nghỉ",
            "Tuyệt vời cho nhóm bạn",
            "Phù hợp gia đình"
        )

        val reviewContents = listOf(
            "Chuyến đi được tổ chức rất tốt, host nhiệt tình và chu đáo. Lịch trình hợp lý, không gấp gáp. Các địa điểm tham quan đều đẹp và thú vị. Chi phí hợp lý với những gì được trải nghiệm.",
            "Mình rất thích chuyến đi này! Các thành viên thân thiện, host có kinh nghiệm. Đồ ăn ngon, chỗ ở sạch sẽ. Có nhiều hoạt động thú vị và thời gian nghỉ ngơi hợp lý.",
            "Đây là lần đầu mình đi cùng buddy và trải nghiệm rất tốt. Host tổ chức chuyên nghiệp, quan tâm đến nhu cầu của mọi người. Sẽ recommend cho bạn bè!",
            "Chuyến đi vượt ngoài mong đợi! Cảnh đẹp, thời tiết tốt, các bạn đi cùng vui vẻ. Chi phí hợp lý, không phát sinh thêm nhiều. Đáng để thử một lần!",
            "Tổ chức rất có tâm, chi tiết từng ngày được plan kỹ. Host nhiệt tình giải đáp mọi thắc mắc. Chỗ ở và phương tiện di chuyển đều tốt.",
            "Chuyến đi rất vui, được làm quen nhiều bạn mới. Các hoạt động đa dạng, không nhàm chán. Giá cả phải chăng so với chất lượng nhận được.",
            "Trải nghiệm tuyệt vời! Host có kinh nghiệm và sắp xếp mọi thứ rất tốt. Lịch trình linh hoạt, không bị bắt buộc. Sẽ tham gia chuyến khác của host!",
            "Phù hợp cho người muốn nghỉ dưỡng và thư giãn. Không gian yên tĩnh, cảnh đẹp. Host nhiệt tình và chu đáo với từng thành viên.",
            "Chuyến đi vui vẻ với nhóm bạn. Các hoạt động team building thú vị. Chi phí minh bạch, không có chi phí phát sinh đột xuất.",
            "Rất phù hợp cho gia đình. Các hoạt động an toàn, phù hợp mọi lứa tuổi. Host rất quan tâm đến trẻ em. Sẽ đi lại lần nữa!"
        )

        val photoUrls = listOf(
            "https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1609137144813-7d9921338f24?w=800&h=600&fit=crop"
        )

        // Create 3-5 reviews for each buddy (if completed)
        buddies.take(15).forEach { buddy ->
            val reviewCount = (3..5).random()

            repeat(reviewCount) { index ->
                val user = users[(buddies.indexOf(buddy) + index) % users.size]
                val rating = (4.0..5.0).random()

                reviews.add(BuddyReview(
                    buddyId = buddy.id,
                    userId = user.id,
                    rating = rating,
                    organizationRating = (rating - 0.5..rating).random(),
                    experienceRating = (rating - 0.3..rating).random(),
                    valueRating = (rating - 0.5..rating + 0.2).random().coerceIn(1.0, 5.0),
                    title = reviewTitles[index % reviewTitles.size],
                    content = reviewContents[index % reviewContents.size],
                    photos = if (index % 2 == 0) photoUrls.take((1..3).random()) else emptyList(),
                    likes = (0..50).random(),
                    isVerified = index % 3 == 0, // 1/3 verified reviews
                    helpful = (0..30).random()
                ))
            }
        }

        return reviews
    }

    private fun ClosedRange<Double>.random(): Double {
        return start + kotlin.random.Random.nextDouble() * (endInclusive - start)
    }
}

