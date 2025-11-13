package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.Post
import com.lazytravel.domain.repository.PostRepository

/**
 * Use Case - Lấy danh sách posts (Social Feed)
 */
class GetPostsUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(): Result<List<Post>> {
        return try {
            val posts = repository.getPosts()
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
