package com.lazytravel.domain.repository

import com.lazytravel.domain.model.BlogPost
import com.lazytravel.domain.model.BlogCategory

/**
 * Blog Repository Interface
 */
interface BlogRepository {
    suspend fun getBlogPosts(): List<BlogPost>
    suspend fun getPublishedPosts(): List<BlogPost>
    suspend fun getPostById(id: String): BlogPost
    suspend fun getPostsByCategory(categoryId: String): List<BlogPost>
    suspend fun searchPosts(query: String): List<BlogPost>

    // Categories
    suspend fun getCategories(): List<BlogCategory>
}
