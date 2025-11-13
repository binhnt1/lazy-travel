package com.lazytravel.domain.repository

import com.lazytravel.domain.model.Post
import com.lazytravel.domain.model.PostComment
import com.lazytravel.domain.model.PostLike

/**
 * Post Repository Interface
 */
interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun getPostById(id: String): Post
    suspend fun getUserPosts(userId: String): List<Post>
    suspend fun createPost(post: Post): Post
    suspend fun updatePost(post: Post): Post
    suspend fun deletePost(id: String)

    // Likes
    suspend fun likePost(postId: String, userId: String): PostLike
    suspend fun unlikePost(postId: String, userId: String)

    // Comments
    suspend fun getPostComments(postId: String): List<PostComment>
    suspend fun addComment(comment: PostComment): PostComment
    suspend fun deleteComment(commentId: String)
}
