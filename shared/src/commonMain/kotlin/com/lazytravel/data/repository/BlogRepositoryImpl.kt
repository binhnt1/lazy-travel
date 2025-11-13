package com.lazytravel.data.repository

import com.lazytravel.data.remote.PocketBaseApi
import com.lazytravel.data.remote.PocketBaseConfig
import com.lazytravel.domain.model.BlogPost
import com.lazytravel.domain.model.BlogCategory
import com.lazytravel.domain.repository.BlogRepository
import kotlinx.serialization.json.Json

/**
 * Blog Repository Implementation
 */
class BlogRepositoryImpl : BlogRepository {

    private val postsCollection = PocketBaseConfig.Collections.BLOG_POSTS
    private val categoriesCollection = PocketBaseConfig.Collections.BLOG_CATEGORIES

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getBlogPosts(): List<BlogPost> {
        val result = PocketBaseApi.getRecords(
            collection = postsCollection,
            page = 1,
            perPage = 100,
            sort = "-published_at"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(BlogPost.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse blog post: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching blog posts: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getPublishedPosts(): List<BlogPost> {
        val filter = "is_published = true"

        val result = PocketBaseApi.getRecords(
            collection = postsCollection,
            filter = filter,
            sort = "-published_at"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(BlogPost.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse blog post: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching published posts: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun getPostById(id: String): BlogPost {
        val result = PocketBaseApi.getRecord(postsCollection, id)

        return result.fold(
            onSuccess = { responseText ->
                json.decodeFromString(BlogPost.serializer(), responseText)
            },
            onFailure = { error ->
                throw Exception("Error fetching blog post $id: ${error.message}")
            }
        )
    }

    override suspend fun getPostsByCategory(categoryId: String): List<BlogPost> {
        val filter = "category_id = '$categoryId' && is_published = true"

        val result = PocketBaseApi.getRecords(
            collection = postsCollection,
            filter = filter,
            sort = "-published_at"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(BlogPost.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse blog post: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching posts by category: ${error.message}")
                emptyList()
            }
        )
    }

    override suspend fun searchPosts(query: String): List<BlogPost> {
        val filter = "is_published = true && (title ~ '$query' || excerpt ~ '$query' || content ~ '$query')"

        val result = PocketBaseApi.getRecords(
            collection = postsCollection,
            filter = filter,
            sort = "-published_at"
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(BlogPost.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse blog post: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error searching blog posts: ${error.message}")
                emptyList()
            }
        )
    }

    // Categories
    override suspend fun getCategories(): List<BlogCategory> {
        val filter = "is_active = true"

        val result = PocketBaseApi.getRecords(
            collection = categoriesCollection,
            filter = filter
        )

        return result.fold(
            onSuccess = { response ->
                response.items.mapNotNull { jsonElement ->
                    try {
                        json.decodeFromJsonElement(BlogCategory.serializer(), jsonElement)
                    } catch (e: Exception) {
                        println("⚠️ Failed to parse blog category: ${e.message}")
                        null
                    }
                }
            },
            onFailure = { error ->
                println("❌ Error fetching blog categories: ${error.message}")
                emptyList()
            }
        )
    }
}
