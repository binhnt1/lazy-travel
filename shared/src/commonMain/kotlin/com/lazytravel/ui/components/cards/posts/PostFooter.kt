package com.lazytravel.ui.components.cards.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PostFooter(
    likesCount: Int,
    commentsCount: Int,
    sharesCount: Int = 0,
    viewsCount: Int = 0,
    isLiked: Boolean = false,
    isBookmarked: Boolean = false,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Stats row (likes, comments, shares count)
        if (likesCount > 0 || commentsCount > 0 || sharesCount > 0 || viewsCount > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Likes, Comments
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (likesCount > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color(0xFFFF6B35),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = formatCount(likesCount),
                                fontSize = 13.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }

                    if (commentsCount > 0) {
                        Text(
                            text = "${formatCount(commentsCount)} bình luận",
                            fontSize = 13.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }

                // Right side: Shares, Views
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (sharesCount > 0) {
                        Text(
                            text = "${formatCount(sharesCount)} chia sẻ",
                            fontSize = 13.sp,
                            color = Color(0xFF666666)
                        )
                    }

                    if (viewsCount > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color(0xFF666666),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = formatCount(viewsCount),
                                fontSize = 13.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            }
        }

        // Divider
        HorizontalDivider(
            color = Color(0xFFE0E0E0),
            thickness = 0.5.dp
        )

        // Action buttons row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Like button
            ActionButton(
                icon = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                label = "Thích",
                color = if (isLiked) Color(0xFFFF6B35) else Color(0xFF666666),
                onClick = onLikeClick,
                modifier = Modifier.weight(1f)
            )

            // Comment button
            ActionButton(
                icon = Icons.Outlined.ChatBubbleOutline,
                label = "Bình luận",
                color = Color(0xFF666666),
                onClick = onCommentClick,
                modifier = Modifier.weight(1f)
            )

            // Share button
            ActionButton(
                icon = Icons.Outlined.Share,
                label = "Chia sẻ",
                color = Color(0xFF666666),
                onClick = onShareClick,
                modifier = Modifier.weight(1f)
            )

            // Bookmark button
            ActionButton(
                icon = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                label = "Lưu",
                color = if (isBookmarked) Color(0xFFFF6B35) else Color(0xFF666666),
                onClick = onBookmarkClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

private fun formatCount(count: Int): String {
    return when {
        count < 1000 -> count.toString()
        count < 1000000 -> "${(count / 100) / 10.0}K"
        else -> "${(count / 100000) / 10.0}M"
    }
}

