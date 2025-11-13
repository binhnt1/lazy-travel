package com.lazytravel.domain.usecase

import com.lazytravel.domain.model.BlogPost
import com.lazytravel.domain.repository.BlogRepository

/**
 * Use Case - Lấy danh sách blog posts
 */
class GetBlogPostsUseCase(
    private val repository: BlogRepository
) {
    suspend operator fun invoke(): Result<List<BlogPost>> {
        return try {
            val posts = repository.getPublishedPosts()
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
