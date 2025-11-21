package com.lazytravel.ui.components.cards.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.ui.components.organisms.ImageViewerDialog
import com.lazytravel.ui.theme.AppColors

@Composable
fun ImagePostCard(
    authorName: String,
    authorAvatar: String,
    timeAgo: String,
    location: String? = null,
    content: String,
    imageUrl: String,
    likesCount: Int = 0,
    commentsCount: Int = 0,
    viewsCount: Int = 0,
    sharesCount: Int = 0,
    isLiked: Boolean = false,
    isBookmarked: Boolean = false,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showImageViewer by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Header
        PostHeader(
            authorName = authorName,
            authorAvatar = authorAvatar,
            timeAgo = timeAgo,
            location = location
        )

        // Content text
        if (content.isNotEmpty()) {
            Text(
                text = content,
                fontSize = 14.sp,
                color = AppColors.TextPrimary,
                lineHeight = 21.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Image using Coil
        AsyncImage(
            model = imageUrl,
            contentDescription = "Post image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable { showImageViewer = true },
            contentScale = ContentScale.Crop,
            onLoading = {
            },
            onSuccess = {
            },
            onError = {
            }
        )

        // Footer with actions
        PostFooter(
            likesCount = likesCount,
            commentsCount = commentsCount,
            sharesCount = sharesCount,
            viewsCount = viewsCount,
            isLiked = isLiked,
            isBookmarked = isBookmarked,
            onLikeClick = onLikeClick,
            onCommentClick = onCommentClick,
            onShareClick = onShareClick,
            onBookmarkClick = onBookmarkClick
        )
    }

    // Image Viewer Dialog
    if (showImageViewer) {
        ImageViewerDialog(
            images = listOf(imageUrl),
            initialPage = 0,
            onDismiss = { }
        )
    }
}
