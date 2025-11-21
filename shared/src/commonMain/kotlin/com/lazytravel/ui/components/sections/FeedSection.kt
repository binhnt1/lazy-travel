package com.lazytravel.ui.components.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.Post
import com.lazytravel.data.models.PostShare
import com.lazytravel.data.models.User
import com.lazytravel.data.models.enums.PostType
import com.lazytravel.helpers.calculateTimeAgo
import com.lazytravel.helpers.extractAlbumImages
import com.lazytravel.helpers.extractImageUrl
import com.lazytravel.helpers.extractTextContent
import com.lazytravel.helpers.extractThumbnailUrl
import com.lazytravel.helpers.extractVideoUrl
import com.lazytravel.ui.components.cards.posts.AlbumPostCard
import com.lazytravel.ui.components.cards.posts.ImagePostCard
import com.lazytravel.ui.components.cards.posts.SharePostCard
import com.lazytravel.ui.components.cards.posts.TextPostCard
import com.lazytravel.ui.components.cards.posts.VideoPostCard
import com.lazytravel.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun FeedSection(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
    isLoggedIn: Boolean = false
) {
    val scope = rememberCoroutineScope()
    val postsRepo = remember { BaseRepository<Post>() }
    val usersRepo = remember { BaseRepository<User>() }
    val postSharesRepo = remember { BaseRepository<PostShare>() }
    var isLoading by remember { mutableStateOf(true) }
    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var users by remember { mutableStateOf<Map<String, User>>(emptyMap()) }
    var postShares by remember { mutableStateOf<Map<String, PostShare>>(emptyMap()) }
    var originalPosts by remember { mutableStateOf<Map<String, Post>>(emptyMap()) }

    // Interaction handlers
    val handleLike: () -> Unit = {
        if (isLoggedIn) {
            // TODO: Implement like logic when user is logged in
            println("Like clicked")
        } else {
            onLoginClick()
        }
    }

    val handleComment: () -> Unit = {
        if (isLoggedIn) {
            // TODO: Implement comment logic when user is logged in
            println("Comment clicked")
        } else {
            onLoginClick()
        }
    }

    val handleShare: () -> Unit = {
        if (isLoggedIn) {
            // TODO: Implement share logic when user is logged in
            println("Share clicked")
        } else {
            onLoginClick()
        }
    }

    val handleBookmark: () -> Unit = {
        if (isLoggedIn) {
            // TODO: Implement bookmark logic when user is logged in
            println("Bookmark clicked")
        } else {
            onLoginClick()
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            // Fetch posts
            postsRepo.getRecords<Post>().fold(
                onSuccess = { fetchedPosts ->
                    // Get random 20 posts
                    posts = fetchedPosts
                        .shuffled()
                        .take(20)

                    // Fetch users for these posts
                    usersRepo.getRecords<User>().fold(
                        onSuccess = { fetchedUsers ->
                            users = fetchedUsers.associateBy { it.id }

                            // Fetch PostShare data for SHARE posts
                            val sharePosts = posts.filter { it.postType == PostType.SHARE.name }
                            if (sharePosts.isNotEmpty()) {
                                postSharesRepo.getRecords<PostShare>().fold(
                                    onSuccess = { fetchedShares ->
                                        postShares = fetchedShares.associateBy { it.postId }

                                        // Fetch original posts
                                        val originalPostIds = fetchedShares.map { it.originalPostId }.distinct()
                                        val originals = mutableMapOf<String, Post>()
                                        originalPostIds.forEach { postId ->
                                            postsRepo.getRecord<Post>(postId).fold(
                                                onSuccess = { post ->
                                                    originals[postId] = post
                                                },
                                                onFailure = { }
                                            )
                                        }
                                        originalPosts = originals
                                    },
                                    onFailure = { }
                                )
                            }

                            isLoading = false
                        },
                        onFailure = { _ ->
                            isLoading = false
                        }
                    )
                },
                onFailure = { _ ->
                    isLoading = false
                }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F2F5)), // Facebook-style background
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Header section with padding
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = localizedString("newsfeed_tag"),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B35),
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )

            Text(
                text = localizedString("feed_section_title"),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            Text(
                text = localizedString("feed_section_subtitle"),
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 21.sp
            )
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = localizedString("loading_posts"),
                    color = Color.Gray
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                posts.forEach { post ->
                    val user = users[post.userId]
                    val dateAgo = post.updatedAt ?: post.createdAt
                    if (user != null) {
                        when (post.postType) {
                            PostType.TEXT.name -> {
                                TextPostCard(
                                    authorName = user.fullName,
                                    authorAvatar = user.avatar,
                                    timeAgo = calculateTimeAgo(dateAgo),
                                    location = post.locationTagged.takeIf { it.isNotEmpty() },
                                    content = extractTextContent(post.content),
                                    likesCount = post.likesCount,
                                    commentsCount = post.commentsCount,
                                    viewsCount = post.viewsCount,
                                    sharesCount = post.sharesCount,
                                    onLikeClick = handleLike,
                                    onCommentClick = handleComment,
                                    onShareClick = handleShare,
                                    onBookmarkClick = handleBookmark
                                )
                            }
                            PostType.SINGLE_IMAGE.name -> {
                                val imageUrl = extractImageUrl(post.content)
                                if (imageUrl != null) {
                                    ImagePostCard(
                                        authorName = user.fullName,
                                        authorAvatar = user.avatar,
                                        timeAgo = calculateTimeAgo(dateAgo),
                                        location = post.locationTagged.takeIf { it.isNotEmpty() },
                                        content = extractTextContent(post.content),
                                        imageUrl = imageUrl,
                                        likesCount = post.likesCount,
                                        commentsCount = post.commentsCount,
                                        viewsCount = post.viewsCount,
                                        sharesCount = post.sharesCount,
                                        onLikeClick = handleLike,
                                        onCommentClick = handleComment,
                                        onShareClick = handleShare,
                                        onBookmarkClick = handleBookmark
                                    )
                                }
                            }
                            PostType.ALBUM.name -> {
                                val images = extractAlbumImages(post.content)
                                if (images.isNotEmpty()) {
                                    AlbumPostCard(
                                        authorName = user.fullName,
                                        authorAvatar = user.avatar,
                                        timeAgo = calculateTimeAgo(dateAgo),
                                        location = post.locationTagged.takeIf { it.isNotEmpty() },
                                        content = extractTextContent(post.content),
                                        images = images,
                                        likesCount = post.likesCount,
                                        commentsCount = post.commentsCount,
                                        viewsCount = post.viewsCount,
                                        sharesCount = post.sharesCount,
                                        onLikeClick = handleLike,
                                        onCommentClick = handleComment,
                                        onShareClick = handleShare,
                                        onBookmarkClick = handleBookmark
                                    )
                                }
                            }
                            PostType.VIDEO.name -> {
                                val videoUrl = extractVideoUrl(post.content)
                                val thumbnailUrl = extractThumbnailUrl(post.content)
                                if (videoUrl != null) {
                                    VideoPostCard(
                                        authorName = user.fullName,
                                        authorAvatar = user.avatar,
                                        timeAgo = calculateTimeAgo(dateAgo),
                                        location = post.locationTagged.takeIf { it.isNotEmpty() },
                                        content = extractTextContent(post.content),
                                        videoUrl = videoUrl,
                                        thumbnailUrl = thumbnailUrl ?: videoUrl, // Use thumbnail if available, fallback to video URL
                                        duration = (30..300).random(), // Random duration 30s-5min
                                        likesCount = post.likesCount,
                                        commentsCount = post.commentsCount,
                                        viewsCount = post.viewsCount,
                                        sharesCount = post.sharesCount,
                                        onLikeClick = handleLike,
                                        onCommentClick = handleComment,
                                        onShareClick = handleShare,
                                        onBookmarkClick = handleBookmark
                                    )
                                }
                            }
                            PostType.SHARE.name -> {
                                val postShareData = postShares[post.id]
                                if (postShareData != null) {
                                    val originalPost = originalPosts[postShareData.originalPostId]
                                    val originalUser = originalPost?.let { users[it.userId] }

                                    if (originalPost != null && originalUser != null) {
                                        SharePostCard(
                                            sharerName = user.fullName,
                                            sharerAvatar = user.avatar,
                                            shareTimeAgo = calculateTimeAgo(dateAgo),
                                            shareComment = postShareData.shareComment,
                                            shareType = postShareData.shareType,

                                            originalAuthorName = originalUser.fullName,
                                            originalAuthorAvatar = originalUser.avatar,
                                            originalTimeAgo = calculateTimeAgo(originalPost.updatedAt ?: originalPost.createdAt),
                                            originalLocation = originalPost.locationTagged.takeIf { it.isNotEmpty() },
                                            originalContent = extractTextContent(originalPost.content),
                                            originalPostType = originalPost.postType,
                                            originalImageUrl = if (originalPost.postType == PostType.SINGLE_IMAGE.name)
                                                extractImageUrl(originalPost.content) else null,
                                            originalImages = if (originalPost.postType == PostType.ALBUM.name)
                                                extractAlbumImages(originalPost.content) else null,
                                            originalVideoUrl = if (originalPost.postType == PostType.VIDEO.name)
                                                extractVideoUrl(originalPost.content) else null,
                                            originalThumbnailUrl = if (originalPost.postType == PostType.VIDEO.name)
                                                extractThumbnailUrl(originalPost.content) else null,

                                            likesCount = post.likesCount,
                                            commentsCount = post.commentsCount,
                                            viewsCount = post.viewsCount,
                                            sharesCount = post.sharesCount,
                                            onLikeClick = handleLike,
                                            onCommentClick = handleComment,
                                            onShareClick = handleShare,
                                            onBookmarkClick = handleBookmark
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Login button in white card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B35)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = localizedString("login_to_view_more"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


