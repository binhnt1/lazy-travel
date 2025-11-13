package com.lazytravel.data.remote.schema

/**
 * Social Feed Schemas
 *
 * For user posts, likes, comments - the social network aspect
 */

/**
 * Posts - User posts on social feed
 */
val postsSchema = collectionSchema {
    name = "posts"
    type = CollectionType.BASE

    fields {
        relation("user") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }

        editor("content") {
            required = true
        }

        text("location_tagged") {
            required = false
            max = 200
        }

        relation("trip") {
            collectionId = "trips"
            maxSelect = 1
            required = false
        }

        select("privacy") {
            values = listOf("public", "friends", "private")
            maxSelect = 1
            required = true
        }

        number("likes_count") {
            required = true
            min = 0.0
            onlyInt = true
        }

        number("comments_count") {
            required = true
            min = 0.0
            onlyInt = true
        }

        number("views_count") {
            required = true
            min = 0.0
            onlyInt = true
        }
    }

    indexes {
        index("user")
        index("privacy")
        index("created")
        compositeIndex("privacy", "created")
    }

    listRule = "privacy = 'public' || (privacy = 'friends' && @collection.follows.following.id ?= user.id && @collection.follows.follower.id ?= @request.auth.id) || user.id = @request.auth.id"
    viewRule = "privacy = 'public' || user.id = @request.auth.id"
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id = user.id"
    deleteRule = "@request.auth.id = user.id"
}

/**
 * Post Media - Images/videos attached to posts
 */
val postMediaSchema = collectionSchema {
    name = "post_media"
    type = CollectionType.BASE

    fields {
        relation("post") {
            collectionId = "posts"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        file("media") {
            maxSelect = 1
            maxSize = 10485760  // 10MB
            mimeTypes = listOf(
                "image/jpeg",
                "image/png",
                "image/webp",
                "video/mp4"
            )
            thumbs = listOf(
                "100x100",
                "300x300",
                "600x600"
            )
        }

        select("media_type") {
            values = listOf("image", "video")
            maxSelect = 1
            required = true
        }

        number("order_index") {
            required = true
            onlyInt = true
        }

        number("width") {
            required = false
            onlyInt = true
        }

        number("height") {
            required = false
            onlyInt = true
        }
    }

    indexes {
        index("post")
        compositeIndex("post", "order_index")
    }

    listRule = "@collection.posts.id ?= post.id && (@collection.posts.privacy ?= 'public' || @collection.posts.user.id ?= @request.auth.id)"
    viewRule = "@collection.posts.id ?= post.id && (@collection.posts.privacy ?= 'public' || @collection.posts.user.id ?= @request.auth.id)"
    createRule = "@request.auth.id != '' && @request.auth.id = post.user.id"
    updateRule = "@request.auth.id = post.user.id"
    deleteRule = "@request.auth.id = post.user.id"
}

/**
 * Post Likes - Who liked which posts
 */
val postLikesSchema = collectionSchema {
    name = "post_likes"
    type = CollectionType.BASE

    fields {
        relation("post") {
            collectionId = "posts"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        relation("user") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }
    }

    indexes {
        compositeIndex("post", "user")  // Unique like per user per post
        index("created")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = ""  // No updates
    deleteRule = "@request.auth.id = user.id"
}

/**
 * Post Comments - Comments on posts
 */
val postCommentsSchema = collectionSchema {
    name = "post_comments"
    type = CollectionType.BASE

    fields {
        relation("post") {
            collectionId = "posts"
            maxSelect = 1
            required = true
            cascadeDelete = true
        }

        relation("user") {
            collectionId = "users"
            maxSelect = 1
            required = true
        }

        text("content") {
            required = true
            min = 1
            max = 500
        }

        relation("parent_comment") {
            collectionId = "post_comments"
            maxSelect = 1
            required = false
            cascadeDelete = true
        }

        number("likes_count") {
            required = true
            min = 0.0
            onlyInt = true
        }
    }

    indexes {
        index("post")
        index("parent_comment")
        index("created")
        compositeIndex("post", "created")
    }

    listRule = null
    viewRule = null
    createRule = "@request.auth.id != ''"
    updateRule = "@request.auth.id = user.id"
    deleteRule = "@request.auth.id = user.id || @request.auth.id = post.user.id"
}
