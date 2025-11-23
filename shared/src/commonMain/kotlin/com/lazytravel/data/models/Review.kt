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
data class Review(
    @EncodeDefault val userId: String = "",
    @EncodeDefault val userName: String = "",
    @EncodeDefault val userAvatar: String = "",
    @EncodeDefault val birthday: String = "",
    @EncodeDefault val location: String = "",
    @EncodeDefault val rating: Double = 5.0,
    @EncodeDefault val content: String = "",
    @EncodeDefault val tripDestination: String = "",
    @EncodeDefault val tripDate: String = "",
    @EncodeDefault val tripGroupSize: Int = 0,
    @EncodeDefault val helpful: Int = 0,
    @EncodeDefault val verified: Boolean = false
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Review)
    }

    fun calculateAge(): Int {
        if (birthday.isEmpty()) return 0

        return try {
            val parts = birthday.split("-")
            if (parts.size != 3) return 0

            val birthYear = parts[0].toIntOrNull() ?: return 0
            val birthMonth = parts[1].toIntOrNull() ?: return 0
            val birthDay = parts[2].toIntOrNull() ?: return 0

            // Get current date
            val currentYear = 2025 // You can make this dynamic using kotlinx-datetime
            val currentMonth = 1
            val currentDay = 17

            var age = currentYear - birthYear

            // Adjust if birthday hasn't occurred yet this year
            if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
                age--
            }

            age
        } catch (e: Exception) {
            0
        }
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("userId") {
            required = true
            collectionId = "users"
            cascadeDelete = false
        }
        text("userName") { required = true; max = 100 }
        text("userAvatar") { required = false; max = 500 }
        text("birthday") { required = false; max = 20 }
        text("location") { required = false; max = 100 }
        number("rating") { required = true; min = 1.0; max = 5.0 }
        text("content") { required = true; max = 2000 }
        text("tripDestination") { required = true; max = 200 }
        text("tripDate") { required = false; max = 50 }
        number("tripGroupSize") { required = false; min = 1.0; max = 100.0; onlyInt = true }
        number("helpful") { required = false; min = 0.0; onlyInt = true }
        bool("verified") { required = false }
    }

    override suspend fun getSeedData(): List<Review> {
        val usersRepo = BaseRepository<User>()
        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()

        if (users.isEmpty()) {
            println("⚠️ No users found for seeding reviews")
            return emptyList()
        }

        val reviews = listOf(
            Review(
                userId = users[0].id,
                userName = "Minh Hoàng",
                userAvatar = "https://i.pravatar.cc/150?img=11",
                birthday = "1999-03-15",
                location = "Hà Nội",
                rating = 5.0,
                content = "App này cứu cả nhóm tôi! Trước đây mỗi lần đi du lịch là cãi nhau về tiền bạc, ai trả bao nhiêu, chia thế nào. Giờ mọi thứ tự động, minh bạch 100%. Nhóm 8 người mà không ai phàn nàn gì cả. Tính năng vote điểm đến cũng hay lắm, dân chủ thật sự!",
                tripDestination = "🏖️ Phú Quốc 4N3Đ",
                tripDate = "Tháng 10, 2024",
                tripGroupSize = 8,
                helpful = 234,
                verified = true
            ),
            Review(
                userId = users.getOrNull(1)?.id ?: users[0].id,
                userName = "Thanh Anh",
                userAvatar = "https://i.pravatar.cc/150?img=5",
                birthday = "1994-07-22",
                location = "TP.HCM",
                rating = 5.0,
                content = "Lần đầu đi du lịch gia đình mà không stress! Bố mẹ, anh chị em đều vote được, ai cũng hài lòng. Lịch trình được sắp xếp rất khoa học, phù hợp với cả người lớn tuổi. Tính năng album chung giúp cả nhà lưu lại kỷ niệm đẹp. Rất đáng dùng!",
                tripDestination = "⛰️ Đà Lạt 3N2Đ",
                tripDate = "Tháng 11, 2024",
                tripGroupSize = 6,
                helpful = 189,
                verified = true
            ),
            Review(
                userId = users.getOrNull(2)?.id ?: users[0].id,
                userName = "Quang Đạt",
                userAvatar = "https://i.pravatar.cc/150?img=33",
                birthday = "1997-11-08",
                location = "Đà Nẵng",
                rating = 5.0,
                content = "UI/UX cực kỳ thân thiện, bố cục khoa học. Tính năng chat trong app rất tiện, không cần nhảy qua lại nhiều ứng dụng. Phần quản lý chi phí chi tiết từng đồng, xuất báo cáo rất pro. Team building công ty dùng app này là hợp lý nhất!",
                tripDestination = "🏮 Hội An 2N1Đ",
                tripDate = "Tháng 9, 2024",
                tripGroupSize = 4,
                helpful = 312,
                verified = true
            ),
            Review(
                userId = users.getOrNull(3)?.id ?: users[0].id,
                userName = "Linh Hương",
                userAvatar = "https://i.pravatar.cc/150?img=20",
                birthday = "2001-05-30",
                location = "Cần Thơ",
                rating = 5.0,
                content = "Honeymoon trip hoàn hảo! App giúp mình và chồng lên kế hoạch rất chi tiết mà không tốn nhiều thời gian. Tính năng album couple rất lãng mạn, lưu giữ từng khoảnh khắc đáng nhớ. Cảm ơn Lazy Travel đã làm chuyến đi thêm ý nghĩa! 💕",
                tripDestination = "🌸 Sapa 3N2Đ",
                tripDate = "Tháng 12, 2024",
                tripGroupSize = 2,
                helpful = 267,
                verified = true
            ),
            Review(
                userId = users.getOrNull(4)?.id ?: users[0].id,
                userName = "Hoàng Đức Minh",
                userAvatar = "https://i.pravatar.cc/150?img=52",
                location = "Cần Thơ",
                rating = 4.0,
                content = "App khá tốt cho việc lên kế hoạch du lịch. Tôi thích tính năng theo dõi chi tiêu trong chuyến đi. Tuy nhiên, mong có thêm nhiều địa điểm ở miền Tây hơn nữa để thuận tiện cho việc khám phá vùng quê.",
                tripDestination = "Mekong Delta",
                tripDate = "Tháng 7, 2024",
                helpful = 67,
                verified = true
            ),
            Review(
                userId = users.getOrNull(5)?.id ?: users[0].id,
                userName = "Vũ Thanh Mai",
                userAvatar = "https://i.pravatar.cc/150?img=23",
                location = "Nha Trang",
                rating = 5.0,
                content = "Xuất sắc! Giao diện thân thiện, tính năng đầy đủ. Tôi đặc biệt thích phần AI suggest trip planning - nó giúp tôi tiết kiệm rất nhiều thời gian nghiên cứu. Chuyến đi Hạ Long của tôi đã trở nên dễ dàng hơn rất nhiều!",
                tripDestination = "Hạ Long",
                tripDate = "Tháng 6, 2024",
                helpful = 175,
                verified = true
            ),
            Review(
                userId = users.getOrNull(6)?.id ?: users[0].id,
                userName = "Đỗ Văn Hùng",
                userAvatar = "https://i.pravatar.cc/150?img=68",
                location = "Huế",
                rating = 4.5,
                content = "Một app rất đáng để thử! Tôi đã dùng cho chuyến đi Quy Nhơn và có trải nghiệm tuyệt vời. Tính năng checkin và chia sẻ ảnh rất tiện lợi. Hy vọng sẽ có thêm tích hợp với các dịch vụ booking khách sạn.",
                tripDestination = "Quy Nhơn",
                tripDate = "Tháng 10, 2024",
                helpful = 89,
                verified = true
            ),
            Review(
                userId = users.getOrNull(7)?.id ?: users[0].id,
                userName = "Bùi Thị Lan",
                userAvatar = "https://i.pravatar.cc/150?img=31",
                location = "Vinh",
                rating = 5.0,
                content = "App này thật sự hữu ích cho những người yêu du lịch! Tôi đã tìm được nhiều điểm đến mới và kết bạn với nhiều travel buddy. Giao diện đẹp, tính năng đa dạng, support team nhiệt tình. Highly recommended!",
                tripDestination = "Ninh Bình",
                tripDate = "Tháng 9, 2024",
                helpful = 142,
                verified = true
            ),
            Review(
                userId = users.getOrNull(8)?.id ?: users[0].id,
                userName = "Ngô Quang Hải",
                userAvatar = "https://i.pravatar.cc/150?img=12",
                location = "Thanh Hóa",
                rating = 4.5,
                content = "Tôi rất thích tính năng vote cho địa điểm trong nhóm bạn. Giúp việc quyết định nơi đi chơi trở nên dân chủ và vui vẻ hơn. Chuyến đi Mũi Né với hội bạn thân đã rất thành công nhờ app này!",
                tripDestination = "Mũi Né",
                tripDate = "Tháng 8, 2024",
                helpful = 78,
                verified = true
            ),
            Review(
                userId = users.getOrNull(9)?.id ?: users[0].id,
                userName = "Đinh Hồng Nhung",
                userAvatar = "https://i.pravatar.cc/150?img=44",
                location = "Bắc Ninh",
                rating = 5.0,
                content = "Perfect cho backpackers! Tôi đã dùng app này trong suốt hành trình xuyên Việt của mình. Tính năng lưu offline map, tracking chi tiêu và kết nối với local travelers rất tuyệt. Cảm ơn đã tạo ra một công cụ tuyệt vời thế này!",
                tripDestination = "Xuyên Việt",
                tripDate = "Tháng 11, 2024",
                helpful = 245,
                verified = true
            )
        )

        return reviews
    }

    companion object {
        fun getSeedData(): List<Review> {
            return listOf(
                Review(
                    userId = "",
                    userName = "Minh Hoàng",
                    userAvatar = "https://i.pravatar.cc/150?img=11",
                    birthday = "1999-03-15",
                    location = "Hà Nội",
                    rating = 5.0,
                    content = "App này cứu cả nhóm tôi! Trước đây mỗi lần đi du lịch là cãi nhau về tiền bạc, ai trả bao nhiêu, chia thế nào. Giờ mọi thứ tự động, minh bạch 100%. Nhóm 8 người mà không ai phàn nàn gì cả. Tính năng vote điểm đến cũng hay lắm, dân chủ thật sự!",
                    tripDestination = "🏖️ Phú Quốc 4N3Đ",
                    tripDate = "Tháng 10, 2024",
                    tripGroupSize = 8,
                    helpful = 234,
                    verified = true
                ),
                Review(
                    userId = "",
                    userName = "Thanh Anh",
                    userAvatar = "https://i.pravatar.cc/150?img=5",
                    birthday = "1994-07-22",
                    location = "TP.HCM",
                    rating = 5.0,
                    content = "Lần đầu đi du lịch gia đình mà không stress! Bố mẹ, anh chị em đều vote được, ai cũng hài lòng. Lịch trình được sắp xếp rất khoa học, phù hợp với cả người lớn tuổi. Tính năng album chung giúp cả nhà lưu lại kỷ niệm đẹp. Rất đáng dùng!",
                    tripDestination = "⛰️ Đà Lạt 3N2Đ",
                    tripDate = "Tháng 11, 2024",
                    tripGroupSize = 6,
                    helpful = 189,
                    verified = true
                ),
                Review(
                    userId = "",
                    userName = "Quang Đạt",
                    userAvatar = "https://i.pravatar.cc/150?img=33",
                    birthday = "1997-11-08",
                    location = "Đà Nẵng",
                    rating = 5.0,
                    content = "UI/UX cực kỳ thân thiện, bố cục khoa học. Tính năng chat trong app rất tiện, không cần nhảy qua lại nhiều ứng dụng. Phần quản lý chi phí chi tiết từng đồng, xuất báo cáo rất pro. Team building công ty dùng app này là hợp lý nhất!",
                    tripDestination = "🏮 Hội An 2N1Đ",
                    tripDate = "Tháng 9, 2024",
                    tripGroupSize = 4,
                    helpful = 312,
                    verified = true
                ),
                Review(
                    userId = "",
                    userName = "Linh Hương",
                    userAvatar = "https://i.pravatar.cc/150?img=20",
                    birthday = "2001-05-30",
                    location = "Cần Thơ",
                    rating = 5.0,
                    content = "Honeymoon trip hoàn hảo! App giúp mình và chồng lên kế hoạch rất chi tiết mà không tốn nhiều thời gian. Tính năng album couple rất lãng mạn, lưu giữ từng khoảnh khắc đáng nhớ. Cảm ơn Lazy Travel đã làm chuyến đi thêm ý nghĩa! 💕",
                    tripDestination = "🌸 Sapa 3N2Đ",
                    tripDate = "Tháng 12, 2024",
                    tripGroupSize = 2,
                    helpful = 267,
                    verified = true
                )
            )
        }
    }
}

