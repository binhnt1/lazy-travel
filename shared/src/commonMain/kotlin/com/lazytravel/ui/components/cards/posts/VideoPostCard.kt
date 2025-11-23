package com.lazytravel.ui.components.cards.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
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
import com.lazytravel.ui.components.atoms.VideoPlayer
import com.lazytravel.ui.theme.AppColors

@Composable
fun VideoPostCard(
    authorName: String,
    authorAvatar: String,
    timeAgo: String,
    location: String? = null,
    content: String,
    videoUrl: String,
    thumbnailUrl: String,
    duration: Int, // in seconds
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

        // Real Video Player using expect/actual pattern
        var isPlaying by remember { mutableStateOf(false) }
        var isMuted by remember { mutableStateOf(false) }
        var isFullscreen by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isFullscreen) 600.dp else 300.dp)
        ) {
            if (isPlaying) {
                // Show actual video player
                VideoPlayer(
                    url = videoUrl,
                    modifier = Modifier.fillMaxSize(),
                    autoPlay = true,
                    showControls = true,
                    isMuted = isMuted,
                    isFullscreen = isFullscreen,
                    onMuteToggle = { isMuted = !isMuted },
                    onFullscreenToggle = { isFullscreen = !isFullscreen }
                )

                // Close button overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .clickable { isPlaying = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✕",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }
                }
            } else {
                // Show thumbnail with play button
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { isPlaying = true }
                ) {
                    // Video thumbnail from DB using Coil
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = "Video thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onLoading = {
                        },
                        onSuccess = {
                        },
                        onError = {
                        }
                    )

                    // Subtle dark overlay for better button visibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.15f))
                    )

                    // Play button overlay - Material Icon
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Play button
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.95f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play video",
                                tint = Color.Black,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }

                    // Duration badge at bottom right
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Text(
                            text = formatDuration(duration),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    color = Color.Black.copy(alpha = 0.8f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        )
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
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    val secondsStr = if (remainingSeconds < 10) "0$remainingSeconds" else "$remainingSeconds"
    return "$minutes:$secondsStr"
}

