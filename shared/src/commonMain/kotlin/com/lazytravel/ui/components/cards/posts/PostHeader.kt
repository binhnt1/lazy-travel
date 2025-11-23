package com.lazytravel.ui.components.cards.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun PostHeader(
    authorName: String,
    authorAvatar: String,
    timeAgo: String,
    location: String?,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(if (compact) 8.dp else 12.dp),
        horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 10.dp)
    ) {
        // Avatar image using Coil
        Box(
            modifier = Modifier
                .size(if (compact) 36.dp else 44.dp)
                .clip(RoundedCornerShape(if (compact) 6.dp else 8.dp))
        ) {
            AsyncImage(
                model = authorAvatar,
                contentDescription = "Avatar of $authorName",
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE0E0E0)),
                contentScale = ContentScale.Crop,
                onLoading = {
                },
                onSuccess = {
                },
                onError = {
                }
            )
        }

        // Author info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = authorName,
                fontSize = if (compact) 12.sp else 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (location != null) {
                    Text(
                        text = "📍 $location • ",
                        fontSize = if (compact) 10.sp else 11.sp,
                        color = Color(0xFF999999)
                    )
                }
                Text(
                    text = timeAgo,
                    fontSize = if (compact) 10.sp else 11.sp,
                    color = Color(0xFF999999)
                )
            }
        }
    }
}

