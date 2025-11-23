package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import com.lazytravel.data.models.enums.PostType
import com.lazytravel.data.models.enums.PrivacyType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Post(
    @EncodeDefault val userId: String = "",
    @EncodeDefault val postType: String = PostType.TEXT.name,
    @EncodeDefault val content: String = "",
    @EncodeDefault val locationTagged: String = "",
    @EncodeDefault val tripId: String = "",
    @EncodeDefault val privacy: String = PrivacyType.PUBLIC.name,
    @EncodeDefault val likesCount: Int = 0,
    @EncodeDefault val commentsCount: Int = 0,
    @EncodeDefault val sharesCount: Int = 0,
    @EncodeDefault val viewsCount: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Post)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("userId") {
            required = true
            collectionId = "users"
            cascadeDelete = false
        }
        text("postType") { required = true; max = 50 }
        text("content") { required = true; max = 10000 }
        text("locationTagged") { required = false; max = 200 }
        text("tripId") { required = false; max = 50 }
        text("privacy") { required = true; max = 20 }
        number("likesCount") { required = false; min = 0.0; onlyInt = true }
        number("commentsCount") { required = false; min = 0.0; onlyInt = true }
        number("sharesCount") { required = false; min = 0.0; onlyInt = true }
        number("viewsCount") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<Post> {
        val usersRepo = BaseRepository<User>()
        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()

        if (users.isEmpty()) {
            return emptyList()
        }

        val posts = mutableListOf<Post>()

        // Danh sách các địa điểm du lịch Việt Nam
        val locations = listOf("Đà Lạt", "Phú Quốc", "Hội An", "Nha Trang", "Sapa", "Hạ Long",
            "Đà Nẵng", "Huế", "Vũng Tàu", "Mũi Né", "Quy Nhơn", "Ninh Bình", "Cần Thơ", "Hà Nội",
            "TP.HCM", "Cát Bà", "Côn Đảo", "Lý Sơn", "Phan Thiết", "Tam Đảo")

        val postContents = listOf(
            // TEXT posts
            """{"text":"Mình có mấy tips cho ai định đi Phú Quốc:\n✅ Nên đi tháng 11-3 (thời tiết đẹp)\n✅ Thuê xe máy tự do hơn\n✅ Đặt resort sớm để có giá tốt\n✅ Đừng bỏ qua chợ đêm và vinpearl"}""",
            """{"text":"Vừa đi Sapa về, mọi người hỏi nhiều quá nên mình tổng hợp luôn:\n- Chi phí 2N1Đ khoảng 2-3 triệu/người\n- Nên đi vào tháng 9-11 để thấy lúa chín vàng\n- Thuê xe máy để đi Fansipan và Y Tý\n- Đặt homestay sớm vì cuối tuần hay full"}""",
            """{"text":"Check-in Đà Lạt 3 ngày 2 đêm chỉ với 1.5 triệu:\n🏠 Homestay: 300k/đêm\n🍜 Ăn uống: 500k\n🏍️ Thuê xe: 150k/ngày\n📸 Các điểm free: Hồ Xuân Hương, Crazy House (50k), Thiền viện Trúc Lâm"}""",

            // SINGLE_IMAGE posts
            """{"text":"Crazy House quá đỉnh! Kiến trúc độc đáo 🏰✨","imageUrl":"https://images.unsplash.com/photo-1583417319070-4a69db38a482"}""",
            """{"text":"Bãi biển Nha Trang trong xanh, cát trắng mịn 🏖️☀️","imageUrl":"https://images.unsplash.com/photo-1559628376-f3fe5f782a2e"}""",
            """{"text":"Sapa mùa này đẹp lắm, sương mù bao phủ thung lũng 🌫️⛰️","imageUrl":"https://images.unsplash.com/photo-1528127269322-539801943592"}""",
            """{"text":"Vịnh Hạ Long tuyệt đẹp! Du thuyền 2 ngày 1 đêm quá tuyệt 🚢","imageUrl":"https://images.unsplash.com/photo-1528127269322-539801943592"}""",
            """{"text":"Hoàng Thành Huế uy nghiêm và tráng lệ 🏯","imageUrl":"https://images.unsplash.com/photo-1583417319070-4a69db38a482"}""",
            """{"text":"Đi Vũng Tàu cuối tuần ăn hải sản tươi ngon 🦐🦑","imageUrl":"https://images.unsplash.com/photo-1559628376-f3fe5f782a2e"}""",
            """{"text":"Biển Quy Nhơn trong xanh và yên bình 🌊","imageUrl":"https://images.unsplash.com/photo-1506929562872-bb421503ef21"}""",
            """{"text":"Phố cổ Hà Nội 36 phố phường nhộn nhịp 🏙️","imageUrl":"https://images.unsplash.com/photo-1528127269322-539801943592"}""",
            """{"text":"Cát Bà hoang sơ và đẹp tự nhiên 🏝️","imageUrl":"https://images.unsplash.com/photo-1506929562872-bb421503ef21"}""",

            // ALBUM posts
            """{"text":"Phố cổ Hội An đẹp mê hồn! Đặc biệt là lúc đêm 🏮✨","images":["https://images.unsplash.com/photo-1583417319070-4a69db38a482","https://images.unsplash.com/photo-1528127269322-539801943592","https://images.unsplash.com/photo-1559628376-f3fe5f782a2e","https://images.unsplash.com/photo-1506929562872-bb421503ef21"]}""",
            """{"text":"Đồi cát bay Mũi Né - trải nghiệm trượt cát cực vui! 🏜️","images":["https://images.unsplash.com/photo-1506929562872-bb421503ef21","https://images.unsplash.com/photo-1559628376-f3fe5f782a2e","https://images.unsplash.com/photo-1528127269322-539801943592"]}""",
            """{"text":"Tràng An Ninh Bình - Vịnh Hạ Long trên cạn đẹp như tranh 🛶","images":["https://images.unsplash.com/photo-1528127269322-539801943592","https://images.unsplash.com/photo-1583417319070-4a69db38a482","https://images.unsplash.com/photo-1506929562872-bb421503ef21"]}""",
            """{"text":"Côn Đảo - thiên đường biển đảo yên bình 🐢","images":["https://images.unsplash.com/photo-1559628376-f3fe5f782a2e","https://images.unsplash.com/photo-1506929562872-bb421503ef21"]}""",

            // VIDEO posts - With thumbnail images (Unsplash with proper sizing)
            """{"text":"Cầu Rồng Đà Nẵng phun lửa rồng cực ấn tượng 🐉🔥","videoUrl":"https://www.w3schools.com/html/mov_bbb.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"}""",
            """{"text":"Chợ nổi Cái Răng Cần Thơ sáng sớm náo nhiệt 🚤","videoUrl":"https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1506929562872-bb421503ef21?w=800&h=600&fit=crop"}""",
            """{"text":"TP.HCM sôi động về đêm, đi Bùi Viện vui lắm! 🌃","videoUrl":"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop"}""",
            """{"text":"Phố cổ Hà Nội đẹp lung linh về đêm 🏮","videoUrl":"https://www.w3schools.com/html/mov_bbb.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1559628376-f3fe5f782a2e?w=800&h=600&fit=crop"}""",
            """{"text":"Vịnh Hạ Long tuyệt đẹp khi nhìn từ trên cao 🚁","videoUrl":"https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"}""",
            """{"text":"Đà Lạt mùa hoa mimosa vàng rực rỡ 🌼","videoUrl":"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1506929562872-bb421503ef21?w=800&h=600&fit=crop"}""",
            """{"text":"Sapa sương mù bao phủ ruộng bậc thang 🌫️","videoUrl":"https://www.w3schools.com/html/mov_bbb.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop"}""",
            """{"text":"Phú Quốc hoàng hôn trên biển đẹp như mơ 🌅","videoUrl":"https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1559628376-f3fe5f782a2e?w=800&h=600&fit=crop"}""",
            """{"text":"Hội An phố cổ đêm lung linh đèn lồng 🏮","videoUrl":"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"}""",
            """{"text":"Nha Trang biển xanh ngắm từ cáp treo 🚡","videoUrl":"https://www.w3schools.com/html/mov_bbb.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1506929562872-bb421503ef21?w=800&h=600&fit=crop"}""",
            """{"text":"Mũi Né đồi cát bay vào lúc hoàng hôn 🏜️","videoUrl":"https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop"}""",
            """{"text":"Huế sông Hương thơ mộng buổi chiều 🛶","videoUrl":"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1559628376-f3fe5f782a2e?w=800&h=600&fit=crop"}""",
            """{"text":"Ninh Bình Tràng An non nước hữu tình 🚣","videoUrl":"https://www.w3schools.com/html/mov_bbb.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&h=600&fit=crop"}""",
            """{"text":"Cần Thơ chợ nổi buổi sáng sớm nhộn nhịp 🚤","videoUrl":"https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1506929562872-bb421503ef21?w=800&h=600&fit=crop"}""",
            """{"text":"Côn Đảo biển trong xanh như ngọc bích 🏝️","videoUrl":"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1528127269322-539801943592?w=800&h=600&fit=crop"}""",
            """{"text":"Tam Đảo mát mẻ, thích hợp nghỉ dưỡng cuối tuần ⛰️","videoUrl":"https://www.w3schools.com/html/mov_bbb.mp4","thumbnailUrl":"https://images.unsplash.com/photo-1559628376-f3fe5f782a2e?w=800&h=600&fit=crop"}"""
        )

        for (i in 0..2) {
            val user = users[i % users.size]
            val location = locations[i % locations.size]
            posts.add(
                Post(
                    userId = user.id,
                    postType = PostType.TEXT.name,
                    content = postContents[i],
                    locationTagged = if (i % 2 == 0) location else "",
                    privacy = PrivacyType.PUBLIC.name,
                    likesCount = (10..500).random(),
                    commentsCount = (0..100).random(),
                    sharesCount = (0..50).random(),
                    viewsCount = (50..2000).random()
                )
            )
        }

        // 9 SINGLE_IMAGE posts (index 3-11)
        for (i in 3..11) {
            val user = users[i % users.size]
            val location = locations[i % locations.size]
            posts.add(
                Post(
                    userId = user.id,
                    postType = PostType.SINGLE_IMAGE.name,
                    content = postContents[i],
                    locationTagged = if (i % 2 == 0) location else "",
                    privacy = PrivacyType.PUBLIC.name,
                    likesCount = (10..500).random(),
                    commentsCount = (0..100).random(),
                    sharesCount = (0..50).random(),
                    viewsCount = (50..2000).random()
                )
            )
        }

        // 4 ALBUM posts (index 12-15)
        for (i in 12..15) {
            val user = users[i % users.size]
            val location = locations[i % locations.size]
            posts.add(
                Post(
                    userId = user.id,
                    postType = PostType.ALBUM.name,
                    content = postContents[i],
                    locationTagged = if (i % 2 == 0) location else "",
                    privacy = PrivacyType.PUBLIC.name,
                    likesCount = (10..500).random(),
                    commentsCount = (0..100).random(),
                    sharesCount = (0..50).random(),
                    viewsCount = (50..2000).random()
                )
            )
        }

        // 4 VIDEO posts (index 16-31)
        for (i in 16..31) {
            val user = users[i % users.size]
            val location = locations[i % locations.size]
            posts.add(
                Post(
                    userId = user.id,
                    postType = PostType.VIDEO.name,
                    content = postContents[i],
                    locationTagged = if (i % 2 == 0) location else "",
                    privacy = PrivacyType.PUBLIC.name,
                    likesCount = (10..500).random(),
                    commentsCount = (0..100).random(),
                    sharesCount = (0..50).random(),
                    viewsCount = (50..2000).random()
                )
            )
        }

        return posts
    }
}
