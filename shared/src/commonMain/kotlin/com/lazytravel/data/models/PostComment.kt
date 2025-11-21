package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class PostComment(
    val postId: String = "",
    val userId: String = "",
    val content: String = "",
    val parentCommentId: String = "",
    val likesCount: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as PostComment)
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
        text("content") { required = true; max = 2000 }
        text("parentCommentId") {
            required = false
            max = 50
        }
        number("likesCount") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<PostComment> {
        val usersRepo = BaseRepository<User>()
        val postsRepo = BaseRepository<Post>()

        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()
        val posts = postsRepo.getRecords<Post>().getOrNull() ?: emptyList()

        if (users.isEmpty() || posts.isEmpty()) {
            return emptyList()
        }

        val commentTexts = listOf(
            "Đẹp quá! Mình cũng muốn đi lắm 😍",
            "Thanks for sharing! Very helpful 🙏",
            "Ảnh đẹp quá! Chụp bằng máy gì vậy bạn?",
            "Có link book tour không bạn?",
            "Mình cũng vừa đi tuần trước, đúng là đẹp lắm!",
            "Nên đi mùa nào thì đẹp nhất vậy bạn?",
            "Chi phí khoảng bao nhiêu vậy bạn?",
            "Cảm ơn bạn đã chia sẻ! Rất hữu ích 👍",
            "Đi bao nhiêu ngày thì vừa bạn?",
            "Có nên thuê xe máy không hay đi tour?",
            "Chỗ này đông người không bạn?",
            "Mình đang plan đi tháng sau, note lại đây!",
            "Quán ăn ngon không bạn?",
            "Hotel ở đâu vậy bạn? Giá bao nhiêu?",
            "Tuyệt vời! Save lại để đi sau 📌",
            "Đi cùng gia đình có phù hợp không bạn?",
            "Cảnh đẹp thật! Nhưng xa quá 😅",
            "Vé vào cửa bao nhiêu vậy bạn?",
            "Có gì lưu ý không bạn?",
            "Wow! Thêm vào bucket list ngay! 🎯"
        )

        val comments = mutableListOf<PostComment>()
        val createdComments = mutableMapOf<String, MutableList<PostComment>>()

        // Mỗi post sẽ có từ 3-15 comments
        for (post in posts.take(40)) { // Lấy 40 posts đầu tiên
            val numComments = (3..15).random()
            val postComments = mutableListOf<PostComment>()

            repeat(numComments) {
                val user = users.random()
                val comment = PostComment(
                    postId = post.id,
                    userId = user.id,
                    content = commentTexts.random(),
                    likesCount = (0..50).random()
                )
                comments.add(comment)
                postComments.add(comment)
            }

            createdComments[post.id] = postComments
        }

        // Tạo reply comments (20% số comments sẽ có reply)
        val replyTexts = listOf(
            "Cảm ơn bạn nhé! 😊",
            "Mình sẽ note lại! Thanks!",
            "Ủa mình cũng vậy! Haha 😄",
            "Inbox mình nhé, mình share chi tiết!",
            "Đúng rồi! Mình cũng nghĩ vậy!",
            "Không đâu bạn, chill lắm!",
            "Ừa đúng rồi bạn!",
            "Mình dùng iPhone thôi bạn 📱",
            "Khoảng 5-7 triệu cho 3 ngày bạn nhé",
            "Tháng 11-3 đi đẹp nhất bạn!"
        )

        val commentsForReply = comments.filter { it.likesCount > 10 }
        for (comment in commentsForReply.take(commentsForReply.size / 5)) {
            val user = users.random()
            comments.add(
                PostComment(
                    postId = comment.postId,
                    userId = user.id,
                    content = replyTexts.random(),
                    parentCommentId = comment.id,
                    likesCount = (0..20).random()
                )
            )
        }

        return comments
    }
}

