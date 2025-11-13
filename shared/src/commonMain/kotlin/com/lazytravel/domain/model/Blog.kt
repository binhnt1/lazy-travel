package com.lazytravel.domain.model

import kotlinx.serialization.Serializable

/**
 * Blog Category - Danh mục blog
 */
@Serializable
data class BlogCategory(
    val id: String = "",
    val name: String,
    val icon: String,
    val color: String,
    val isActive: Boolean = true,
    val created: String? = null
)

/**
 * Blog Post - Bài viết blog
 */
@Serializable
data class BlogPost(
    val id: String = "",
    val authorId: String,
    val categoryId: String,
    val title: String,
    val excerpt: String,
    val content: String,
    val coverImage: String? = null,
    val readTimeMinutes: Int,
    val viewsCount: Int = 0,
    val likesCount: Int = 0,
    val isPublished: Boolean = false,
    val publishedAt: String? = null,
    val tags: List<String> = emptyList(),
    val created: String? = null,
    val updated: String? = null
)
