package com.lazytravel.data.repository

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.domain.model.Post
import com.lazytravel.domain.model.PostComment
import com.lazytravel.domain.model.PostLike
import com.lazytravel.domain.repository.PostRepository
import kotlinx.serialization.json.Json

/**
 * Post Repository Implementation - Social Feed
 */
class PostRepositoryImpl : PostRepository {

    private val postsCollection = PocketBaseConfig.Collections.POSTS
    private val likesCollection = PocketBaseConfig.Collections.POST_LIKES
    private val commentsCollection = PocketBaseConfig.Collections.POST_COMMENTS

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getPosts(): List<Post> {
        // Get public posts, sorted by newest
        val filter = "privacy = 'public'"

        val result = PocketBaseApi.getRecords(
            collection = postsCollection,
            page = 1,
            perPage = 50,
            sort = "-created",
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Post.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse post: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching posts: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getPostById(id: String): Post {
        val result = PocketBaseApi.getRecord(postsCollection, id)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(Post.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error fetching post $id: ${error.message}")
            }
        )
    }

    override suspend fun getUserPosts(userId: String): List<Post> {
        val filter = "user_id = '$userId'"

        val result = PocketBaseApi.getRecords(
            collection = postsCollection,
            filter = filter,
            sort = "-created"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(Post.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse post: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching user posts: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun createPost(post: Post): Post {
        val result = PocketBaseApi.createRecord(postsCollection, post)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(Post.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error creating post: ${error.message}")
            }
        )
    }

    override suspend fun updatePost(post: Post): Post {
        val result = PocketBaseApi.updateRecord(postsCollection, post.id, post)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(Post.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error updating post: ${error.message}")
            }
        )
    }

    override suspend fun deletePost(id: String) {
        val result = PocketBaseApi.deleteRecord(postsCollection, id)

        result.fold(
            onSuccess = { /* Success */ },
            onFailure = { error ->
                throw Exception("Error deleting post: ${error.message}")
            }
        )
    }

    // Likes
    override suspend fun likePost(postId: String, userId: String): PostLike {
        val like = PostLike(
            postId = postId,
            userId = userId
        )

        val result = PocketBaseApi.createRecord(likesCollection, like)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(PostLike.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error liking post: ${error.message}")
            }
        )
    }

    override suspend fun unlikePost(postId: String, userId: String) {
        // Find the like record first
        val filter = "post_id = '$postId' && user_id = '$userId'"

        val result = PocketBaseApi.getRecords(
            collection = likesCollection,
            filter = filter
        )

        result.fold(
            onSuccess = { response ->
                response.items.firstOrNull()?.let { jsonElement ->
                    val like = json.decodeFromJsonElement(PostLike.serializer(), jsonElement)
                    PocketBaseApi.deleteRecord(likesCollection, like.id)
                }
            },
            onFailure = { error ->
                println("❌ Error unliking post: ${error.message}")
            }
        )
    }

    // Comments
    override suspend fun getPostComments(postId: String): List<PostComment> {
        val filter = "post_id = '$postId' && parent_comment_id = ''"

        val result = PocketBaseApi.getRecords(
            collection = commentsCollection,
            filter = filter,
            sort = "-created"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(PostComment.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse comment: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching comments: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun addComment(comment: PostComment): PostComment {
        val result = PocketBaseApi.createRecord(commentsCollection, comment)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(PostComment.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error adding comment: ${error.message}")
            }
        )
    }

    override suspend fun deleteComment(commentId: String) {
        val result = PocketBaseApi.deleteRecord(commentsCollection, commentId)

        result.fold(
            onSuccess = { /* Success */ },
            onFailure = { error ->
                throw Exception("Error deleting comment: ${error.message}")
            }
        )
    }
}
