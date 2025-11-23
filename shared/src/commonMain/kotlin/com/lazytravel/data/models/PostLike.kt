package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class PostLike(
    val postId: String = "",
    val userId: String = "",
    val reactionType: String = "LIKE"
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as PostLike)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("postId") {
            required = true
            collectionId = "posts"
            cascadeDelete = true
        }
        relation("userId") {
            required = true
            collectionId = "users"
            cascadeDelete = false
        }
        text("reactionType") { required = true; max = 20 }
    }

    override suspend fun getSeedData(): List<PostLike> {
        val usersRepo = BaseRepository<User>()
        val postsRepo = BaseRepository<Post>()

        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()
        val posts = postsRepo.getRecords<Post>().getOrNull() ?: emptyList()

        if (users.isEmpty() || posts.isEmpty()) {
            return emptyList()
        }

        val reactions = listOf("LIKE", "LOVE", "HAHA", "WOW", "SAD", "ANGRY")
        val likes = mutableListOf<PostLike>()

        // Mỗi post sẽ có từ 5-20 likes ngẫu nhiên
        for (post in posts.take(50)) { // Lấy 50 posts đầu tiên
            val numLikes = (5..20).random()
            val usedUsers = mutableSetOf<String>()

            repeat(numLikes) {
                val user = users.random()
                if (!usedUsers.contains(user.id)) {
                    usedUsers.add(user.id)
                    likes.add(
                        PostLike(
                            postId = post.id,
                            userId = user.id,
                            reactionType = reactions.random()
                        )
                    )
                }
            }
        }

        return likes
    }
}

