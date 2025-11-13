package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.Post
import com.lazytravel.domain.repository.PostRepository

/**
 * Use Case - Tạo post mới
 */
class CreatePostUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(post: Post): Result<Post> {
        return try {
            if (post.content.isBlank()) {
                return Result.failure(Exception("Nội dung không được rỗng"))
            }

            val createdPost = repository.createPost(post)
            Result.success(createdPost)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
