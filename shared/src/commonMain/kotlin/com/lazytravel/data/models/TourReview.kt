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
data class TourReview(
    @EncodeDefault val tourId: String = "",              // Relation to Tour (tour being reviewed)
    @EncodeDefault val userId: String = "",              // Relation to User (reviewer)
    @EncodeDefault val rating: Double = 0.0,             // Overall rating (1-5)
    @EncodeDefault val guideRating: Double = 0.0,        // Tour guide rating (1-5)
    @EncodeDefault val experienceRating: Double = 0.0,   // Tour experience (1-5)
    @EncodeDefault val valueRating: Double = 0.0,        // Value for money (1-5)
    @EncodeDefault val title: String = "",               // Review title
    @EncodeDefault val content: String = "",             // Review content
    @EncodeDefault val photos: List<String> = emptyList(), // Review photos URLs
    @EncodeDefault val likes: Int = 0,                   // Number of likes
    @EncodeDefault val isVerified: Boolean = false,      // Verified review (booked the tour)
    @EncodeDefault val helpful: Int = 0                  // Number of helpful votes
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as TourReview)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("tourId") {
            required = true
            collectionId = Tour().collectionName()
            cascadeDelete = true
        }
        relation("userId") {
            required = true
            collectionId = User().collectionName()
            cascadeDelete = false
        }
        number("rating") { required = true; min = 1.0; max = 5.0 }
        number("guideRating") { required = false; min = 1.0; max = 5.0 }
        number("experienceRating") { required = false; min = 1.0; max = 5.0 }
        number("valueRating") { required = false; min = 1.0; max = 5.0 }
        text("title") { required = false; max = 200 }
        text("content") { required = true; max = 2000 }
        json("photos") { required = false }
        number("likes") { required = false; min = 0.0; onlyInt = true }
        bool("isVerified") { required = false }
        number("helpful") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<TourReview> {
        // Get tours and users for relations
        val toursRepo = BaseRepository<Tour>()
        val tours = toursRepo.getRecords<Tour>().getOrNull() ?: emptyList()

        val usersRepo = BaseRepository<User>()
        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()

        if (tours.isEmpty() || users.isEmpty()) {
            return emptyList()
        }

        val reviews = mutableListOf<TourReview>()

        // Sample review titles and contents
        val reviewTitles = listOf(
            "Tour tuyệt vời!",
            "Đáng đồng tiền bát gạo",
            "Hướng dẫn viên nhiệt tình",
            "Trải nghiệm khó quên",
            "Tour chuyên nghiệp",
            "Rất hài lòng!",
            "Sẽ book lại lần nữa",
            "Phù hợp gia đình",
            "Tổ chức chu đáo",
            "Highly recommended!"
        )

        val reviewContents = listOf(
            "Tour được tổ chức rất tốt, hướng dẫn viên nhiệt tình và chuyên nghiệp. Lịch trình hợp lý, các điểm tham quan đều đẹp. Chi phí hợp lý, không phát sinh thêm. Rất recommend!",
            "Mình rất thích tour này! Hướng dẫn viên giỏi, giải thích chi tiết về văn hóa và lịch sử. Đồ ăn ngon, phương tiện di chuyển thoải mái. Sẽ book tour khác của công ty!",
            "Đây là lần đầu mình đi tour và trải nghiệm rất tốt. Mọi thứ được sắp xếp chu đáo. Khách sạn đẹp, ăn uống ngon. Hướng dẫn viên vui vẻ và nhiệt tình.",
            "Tour vượt mong đợi! Cảnh đẹp hơn hình ảnh, lịch trình không gấp gáp. Có thời gian chụp ảnh và tự do khám phá. Giá cả hợp lý so với chất lượng.",
            "Tổ chức rất chuyên nghiệp, đúng giờ. Hướng dẫn viên am hiểu về địa phương, giới thiệu nhiều điều thú vị. Phương tiện sạch sẽ, tài xế lái xe an toàn.",
            "Tour rất vui, được làm quen nhiều bạn mới. Các hoạt động đa dạng, phù hợp mọi lứa tuổi. Chi phí minh bạch, không có phụ thu đột xuất.",
            "Trải nghiệm tuyệt vời! Mọi thứ đều hoàn hảo từ khách sạn, đồ ăn đến các điểm tham quan. Hướng dẫn viên rất tận tâm. Chắc chắn sẽ giới thiệu cho bạn bè!",
            "Rất phù hợp cho gia đình. Các hoạt động an toàn, phù hợp cả người lớn và trẻ em. Hướng dẫn viên quan tâm đến từng thành viên. Sẽ đi lại!",
            "Tour được plan kỹ càng, mọi detail đều được chăm chút. Khách sạn tốt, vị trí thuận tiện. Đồ ăn đa dạng và ngon. Giá cả hợp lý.",
            "Great tour! Professional guide with good English. Well-organized itinerary. Accommodation and food were excellent. Good value for money. Will definitely recommend!"
        )

        val photoUrls = listOf(
            "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1555400038-63f5ba517a47?w=800&h=600&fit=crop"
        )

        // Create 3-5 reviews for each tour
        tours.take(15).forEach { tour ->
            val reviewCount = (3..5).random()

            repeat(reviewCount) { index ->
                val user = users[(tours.indexOf(tour) + index) % users.size]
                val rating = (4.0..5.0).random()

                reviews.add(TourReview(
                    tourId = tour.id,
                    userId = user.id,
                    rating = rating,
                    guideRating = (rating - 0.3..rating).random(),
                    experienceRating = (rating - 0.2..rating).random(),
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
