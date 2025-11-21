package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String = "",
    val email: String = "",
    val fullName: String = "",
    val avatar: String = "",
    val bio: String = "",
    val verified: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val tripsCount: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as User)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("username") { required = true; max = 50 }
        text("email") { required = true; max = 100 }
        text("fullName") { required = true; max = 100 }
        text("avatar") { required = false; max = 500 }
        text("bio") { required = false; max = 500 }
        bool("verified") { required = false }
        number("followersCount") { required = false; min = 0.0; onlyInt = true }
        number("followingCount") { required = false; min = 0.0; onlyInt = true }
        number("tripsCount") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<User> = listOf(
        User(
            username = "tuanhung",
            email = "tuanhung@example.com",
            fullName = "Tuấn Hùng",
            avatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=400&fit=crop",
            bio = "Yêu thích khám phá các điểm đến mới",
            verified = true,
            followersCount = 2340,
            followingCount = 567,
            tripsCount = 23
        ),
        User(
            username = "hoangnam",
            email = "hoangnam@example.com",
            fullName = "Hoàng Nam",
            avatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&h=400&fit=crop",
            bio = "Travel blogger | 📸 Photography",
            verified = true,
            followersCount = 5678,
            followingCount = 890,
            tripsCount = 45
        ),
        User(
            username = "maiphuong",
            email = "maiphuong@example.com",
            fullName = "Mai Phương",
            avatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&h=400&fit=crop",
            bio = "Nơi nào cũng đẹp nếu ta biết tận hưởng ✨",
            verified = true,
            followersCount = 3456,
            followingCount = 678,
            tripsCount = 34
        ),
        User(
            username = "anhkhoa",
            email = "anhkhoa@example.com",
            fullName = "Anh Khoa",
            avatar = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=400&h=400&fit=crop",
            bio = "Adventure seeker 🏔️",
            verified = false,
            followersCount = 1234,
            followingCount = 456,
            tripsCount = 18
        ),
        User(
            username = "thuhang",
            email = "thuhang@example.com",
            fullName = "Thu Hằng",
            avatar = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400&h=400&fit=crop",
            bio = "Food & Travel 🍜✈️",
            verified = true,
            followersCount = 4567,
            followingCount = 789,
            tripsCount = 56
        )
    )
}

