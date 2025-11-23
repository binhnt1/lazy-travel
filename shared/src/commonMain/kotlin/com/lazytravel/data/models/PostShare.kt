package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import com.lazytravel.data.models.enums.PostType
import com.lazytravel.data.models.enums.PrivacyType
import kotlinx.serialization.Serializable

@Serializable
data class PostShare(
    val postId: String = "",
    val originalPostId: String = "",
    val shareComment: String = "",
    val shareType: String = ""
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as PostShare)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("postId") {
            required = true
            collectionId = "posts"
            cascadeDelete = true
        }
        relation("originalPostId") {
            required = true
            collectionId = "posts"
            cascadeDelete = false
        }
        text("shareComment") { required = false; max = 1000 }
        text("shareType") { required = true; max = 20 }
    }

    override suspend fun getSeedData(): List<PostShare> {
        val usersRepo = BaseRepository<User>()
        val postsRepo = BaseRepository<Post>()

        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()
        val allPosts = postsRepo.getRecords<Post>().getOrNull() ?: emptyList()

        if (users.isEmpty() || allPosts.isEmpty()) {
            println("⚠️ PostShare seed: No users or posts found")
            return emptyList()
        }
        val posts = allPosts.filter { it.postType != PostType.SHARE.name }
        if (posts.isEmpty()) {
            println("⚠️ PostShare seed: No non-share posts found")
            return emptyList()
        }

        val shareComments = listOf(
            "Tips hay quá! Mọi người nên đọc 👍",
            "Đẹp không chê vào đâu được! 😍",
            "Ai muốn đi cùng mình không?",
            "Save lại để tham khảo!",
            "Quá đỉnh! Must visit 🔥",
            "Chia sẻ cho mọi người tham khảo nhé!",
            "Chill phết! Ai free đi cùng không? 🎒",
            "Địa điểm này nên thêm vào bucket list!",
            "Xem mà muốn bay luôn! ✈️",
            "",  // Empty comment for REPOST
            "Thông tin hữu ích! Share cho bạn bè cùng xem",
            "Đúng là địa điểm hot! 🔥",
            "Plan đi ngay thôi! Ai đi cùng?",
            "View đẹp xuất sắc! 🌄",
            "Giá cả hợp lý, nên đi! 💰"
        )

        val shareTypes = listOf("SHARE", "REPOST", "QUOTE")
        val shares = mutableListOf<PostShare>()

        val popularPosts = posts.sortedByDescending { it.likesCount }.take(10)
        for (originalPost in popularPosts) {
            repeat(1) {
                val user = users.random()
                val shareType = shareTypes.random()
                val shareComment = if (shareType == "REPOST") "" else shareComments.random()
                if (user.id.isEmpty()) {
                    return@repeat
                }

                val sharePost = Post(
                    userId = user.id,
                    postType = PostType.SHARE.name,
                    content = shareComment.ifEmpty { "Shared a post" },
                    locationTagged = originalPost.locationTagged,
                    tripId = originalPost.tripId,
                    privacy = PrivacyType.PUBLIC.name,
                    likesCount = 0,
                    commentsCount = 0,
                    sharesCount = 0,
                    viewsCount = 0
                )

                try {
                    val result = postsRepo.createRecord(sharePost)
                    if (result.isSuccess) {
                        val createdPost = result.getOrNull()
                        if (createdPost != null) {
                            shares.add(
                                PostShare(
                                    postId = createdPost.id,
                                    originalPostId = originalPost.id,
                                    shareComment = shareComment,
                                    shareType = shareType
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return shares
    }
}

