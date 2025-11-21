package com.lazytravel.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PostWithDetails(
    val id: String,
    val user: UserBasic,
    val postType: String,
    val content: String,
    val locationTagged: String?,
    val tripId: String?,
    val privacy: String,
    val stats: PostStats,
    val media: List<PostMedia> = emptyList(),
    val createdAt: String,
    val updatedAt: String,
    val isLikedByCurrentUser: Boolean = false
)

@Serializable
data class UserBasic(
    val id: String,
    val username: String,
    val fullName: String,
    val avatar: String,
    val verified: Boolean
)

@Serializable
data class PostStats(
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val views: Int = 0
)

@Serializable
sealed class PostContentData {
    @Serializable
    data class TextContent(
        val text: String
    ) : PostContentData()

    @Serializable
    data class SingleImageContent(
        val text: String,
        val imageUrl: String
    ) : PostContentData()

    @Serializable
    data class AlbumContent(
        val text: String,
        val images: List<String>,
        val totalCount: Int
    ) : PostContentData()

    @Serializable
    data class VideoContent(
        val text: String,
        val videoUrl: String,
        val thumbnailUrl: String,
        val duration: Int
    ) : PostContentData()

    @Serializable
    data class CheckInContent(
        val text: String,
        val placeName: String,
        val placeId: String,
        val latitude: Double,
        val longitude: Double,
        val imageUrl: String?
    ) : PostContentData()

    @Serializable
    data class PollContent(
        val question: String,
        val options: List<PollOptionData>,
        val endsAt: String?
    ) : PostContentData()

    @Serializable
    data class ShareContent(
        val originalPostId: String,
        val comment: String?
    ) : PostContentData()
}

@Serializable
data class PollOptionData(
    val id: String,
    val text: String,
    val votes: Int
)

