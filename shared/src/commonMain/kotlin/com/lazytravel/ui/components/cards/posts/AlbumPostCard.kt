package com.lazytravel.ui.components.cards.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.ui.components.organisms.ImageViewerDialog
import com.lazytravel.ui.theme.AppColors

@Composable
fun AlbumPostCard(
    authorName: String,
    authorAvatar: String,
    timeAgo: String,
    location: String? = null,
    content: String,
    images: List<String>,
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
    var selectedImageIndex by remember { mutableStateOf(0) }

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

        // Album grid (2x2 for first 4 images)
        when {
            images.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🖼️ Album",
                        fontSize = 16.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
            images.size == 1 -> {
                AsyncImage(
                    model = images[0],
                    contentDescription = "Album image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clickable {
                            selectedImageIndex = 0
                            showImageViewer = true
                        },
                    contentScale = ContentScale.Crop,
                    onLoading = {
                    },
                    onSuccess = {
                    },
                    onError = {
                    }
                )
            }
            else -> {
                // Grid layout for multiple images
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        images.take(2).forEachIndexed { index, imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Album image",
                                modifier = Modifier
                                    .weight(1f)
                                    .height(150.dp)
                                    .clickable {
                                        selectedImageIndex = index
                                        showImageViewer = true
                                    },
                                contentScale = ContentScale.Crop,
                                onLoading = {
                                },
                                onSuccess = {
                                },
                                onError = {
                                }
                            )
                        }
                    }
                    if (images.size > 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            images.drop(2).take(2).forEachIndexed { idx, imageUrl ->
                                val actualIndex = idx + 2
                                Box(modifier = Modifier.weight(1f)) {
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = "Album image",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .clickable {
                                                selectedImageIndex = actualIndex
                                                showImageViewer = true
                                            },
                                        contentScale = ContentScale.Crop,
                                        onLoading = {
                                        },
                                        onSuccess = {
                                        },
                                        onError = {
                                        }
                                    )
                                    // Show "+N more" overlay on last image if there are more
                                    if (images.size > 4 && actualIndex == 3) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.6f))
                                                .clickable {
                                                    selectedImageIndex = actualIndex
                                                    showImageViewer = true
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "+${images.size - 4}",
                                                fontSize = 32.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
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

    // Image Viewer Dialog
    if (showImageViewer) {
        ImageViewerDialog(
            images = images,
            initialPage = selectedImageIndex,
            onDismiss = { showImageViewer = false }
        )
    }
}
