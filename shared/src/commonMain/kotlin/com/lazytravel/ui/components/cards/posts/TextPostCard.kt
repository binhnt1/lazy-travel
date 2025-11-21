package com.lazytravel.ui.components.cards.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextPostCard(
    authorName: String,
    authorAvatar: String,
    timeAgo: String,
    location: String? = null,
    content: String,
    likesCount: Int,
    commentsCount: Int,
    viewsCount: Int,
    sharesCount: Int = 0,
    isLiked: Boolean = false,
    isBookmarked: Boolean = false,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Use PostHeader component
        PostHeader(
            authorName = authorName,
            authorAvatar = authorAvatar,
            timeAgo = timeAgo,
            location = location
        )

        // Post content
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = content,
                fontSize = 13.sp,
                color = Color(0xFF212121),
                lineHeight = 18.sp
            )
        }

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
}

