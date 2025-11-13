package com.lazytravel.data.remote.schema

/**
 * Blog & Travel Tips Schemas
 *
 * For travel guides, tips, and articles
 */

/**
 * Blog Categories - Organize blog posts
 */
val blogCategoriesSchema = collectionSchema {
    name = "blog_categories"
    type = CollectionType.BASE

    fields {
        text("name") {
            required = true
            min = 2
            max = 100
        }

        text("icon") {
            required = true
            max = 10  // Emoji
        }

        text("color") {
            required = true
            max = 7  // Hex color #RRGGBB
        }

        bool("is_active") {
            required = true
        }
    }

    indexes {
        uniqueIndex("name")
        index("is_active")
    }

    listRule = "is_active = true"
    viewRule = null
    createRule = ""  // Admin only
    updateRule = ""  // Admin only
    deleteRule = ""  // Admin only
}

/**
 * Blog Posts - Travel articles and tips
 */
val blogPostsSchema = collectionSchema {
    name = "blog_posts"
    type = CollectionType.BASE

    fields {
        relation("author") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }

        relation("category") {
            collectionId = "blog_categories"
            maxSelect = 1
            required = true
        }

        text("title") {
            required = true
            min = 5
            max = 200
        }

        text("excerpt") {
            required = true
            max = 500
        }

        editor("content") {
            required = true
        }

        file("cover_image") {
            maxSelect = 1
            maxSize = 5242880
            mimeTypes = listOf(
                "image/jpeg",
                "image/png",
                "image/webp"
            )
        }

        number("read_time_minutes") {
            required = true
            min = 1.0
            onlyInt = true
        }

        number("views_count") {
            required = true
            min = 0.0
            onlyInt = true
        }

        number("likes_count") {
            required = true
            min = 0.0
            onlyInt = true
        }

        bool("is_published") {
            required = true
        }

        date("published_at") {
            required = false
        }

        // Tags (JSON array: ["Nha Trang", "Budget Travel", "Photography"])
        json("tags") {
            required = false
        }
    }

    indexes {
        index("author")
        index("category")
        index("is_published")
        index("published_at")
        index("views_count")
        compositeIndex("is_published", "published_at")
        compositeIndex("category", "is_published")
    }

    listRule = "is_published = true"
    viewRule = "is_published = true || author.id = @request.auth.id"
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id = author.id"
    deleteRule = "@request.auth.id = author.id"
}
