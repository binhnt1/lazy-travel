package com.lazytravel.domain.model

import kotlinx.serialization.Serializable

/**
 * Post - Bài đăng trên social feed
 */
@Serializable
data class Post(
    val id: String = "",
    val userId: String,
    val content: String,
    val locationTagged: String? = null,
    val tripId: String? = null,
    val privacy: PostPrivacy,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val viewsCount: Int = 0,
    val media: List<PostMedia> = emptyList(),
    val created: String? = null,
    val updated: String? = null
)

enum class PostPrivacy {
    PUBLIC,
    FRIENDS,
    PRIVATE
}

/**
 * Post Media - Ảnh/video trong bài đăng
 */
@Serializable
data class PostMedia(
    val id: String = "",
    val postId: String,
    val media: String, // URL
    val mediaType: MediaType,
    val orderIndex: Int,
    val width: Int? = null,
    val height: Int? = null,
    val created: String? = null
)

enum class MediaType {
    IMAGE,
    VIDEO
}

/**
 * Post Like - Lượt thích bài đăng
 */
@Serializable
data class PostLike(
    val id: String = "",
    val postId: String,
    val userId: String,
    val created: String? = null
)

/**
 * Post Comment - Bình luận bài đăng
 */
@Serializable
data class PostComment(
    val id: String = "",
    val postId: String,
    val userId: String,
    val content: String,
    val parentCommentId: String? = null,
    val likesCount: Int = 0,
    val created: String? = null,
    val updated: String? = null
)
