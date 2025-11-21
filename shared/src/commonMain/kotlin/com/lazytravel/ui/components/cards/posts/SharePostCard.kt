package com.lazytravel.ui.components.cards.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.ui.components.organisms.ImageViewerDialog
import com.lazytravel.ui.theme.AppColors

@Composable
fun SharePostCard(
    // Sharer info
    sharerName: String,
    sharerAvatar: String,
    shareTimeAgo: String,
    shareComment: String = "",
    shareType: String = "SHARE",

    // Original post info
    originalAuthorName: String,
    originalAuthorAvatar: String,
    originalTimeAgo: String,
    originalLocation: String? = null,
    originalContent: String,
    originalPostType: String,
    originalImageUrl: String? = null,
    originalImages: List<String>? = null,
    originalVideoUrl: String? = null,
    originalThumbnailUrl: String? = null,

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
        // Sharer header
        PostHeader(
            authorName = sharerName,
            authorAvatar = sharerAvatar,
            timeAgo = shareTimeAgo,
            location = null
        )

        // Share comment if exists
        if (shareComment.isNotEmpty()) {
            Text(
                text = shareComment,
                fontSize = 14.sp,
                color = AppColors.TextPrimary,
                lineHeight = 21.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Original post container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFFE4E6EB),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp)
        ) {
            // Original post header (smaller size for embedded post)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(6.dp))
                ) {
                    AsyncImage(
                        model = originalAuthorAvatar,
                        contentDescription = "Avatar of $originalAuthorName",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Author info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = originalAuthorName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (originalLocation != null) {
                            Text(
                                text = "📍 $originalLocation • ",
                                fontSize = 10.sp,
                                color = Color(0xFF999999)
                            )
                        }
                        Text(
                            text = originalTimeAgo,
                            fontSize = 10.sp,
                            color = Color(0xFF999999)
                        )
                    }
                }
            }

            // Original content
            if (originalContent.isNotEmpty()) {
                Text(
                    text = originalContent,
                    fontSize = 13.sp,
                    color = AppColors.TextPrimary,
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Original media based on type
            when (originalPostType) {
                "SINGLE_IMAGE" -> {
                    if (originalImageUrl != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        AsyncImage(
                            model = originalImageUrl,
                            contentDescription = "Original post image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clickable { showImageViewer = true },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                "ALBUM" -> {
                    if (!originalImages.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        when (originalImages.size) {
                            1 -> {
                                AsyncImage(
                                    model = originalImages[0],
                                    contentDescription = "Album image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clickable { showImageViewer = true },
                                    contentScale = ContentScale.Crop
                                )
                            }
                            else -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    originalImages.take(2).forEach { imageUrl ->
                                        AsyncImage(
                                            model = imageUrl,
                                            contentDescription = "Album image",
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(150.dp)
                                                .clickable { showImageViewer = true },
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                                if (originalImages.size > 2) {
                                    Text(
                                        text = "+${originalImages.size - 2} ảnh khác",
                                        fontSize = 12.sp,
                                        color = Color(0xFF666666),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                "VIDEO" -> {
                    if (originalThumbnailUrl != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            AsyncImage(
                                model = originalThumbnailUrl,
                                contentDescription = "Video thumbnail",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // Play icon overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = "▶️",
                                    fontSize = 40.sp,
                                    color = Color.White,
                                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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

    // Image viewer dialog
    if (showImageViewer && !originalImages.isNullOrEmpty()) {
        ImageViewerDialog(
            images = originalImages,
            initialPage = 0,
            onDismiss = { showImageViewer = false }
        )
    } else if (showImageViewer && originalImageUrl != null) {
        ImageViewerDialog(
            images = listOf(originalImageUrl),
            initialPage = 0,
            onDismiss = { showImageViewer = false }
        )
    }
}

