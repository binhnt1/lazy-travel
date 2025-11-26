package com.lazytravel.ui.components.cards.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.data.models.Buddy
import com.lazytravel.helpers.formatDateFromTimestamp
import com.lazytravel.ui.components.organisms.ImageViewerDialog

@Composable
fun BuddyLuxuryCard(
    buddy: Buddy,
    onClick: (String) -> Unit = {}
) {
    var showImageViewer by remember { mutableStateOf(false) }
    var initialImagePage by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .width(290.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF08104D),
                        Color(0xFF050A30)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFD4AF37), // Gold border
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick(buddy.id) }
    ) {
        Column {
            // Image section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF1F2D6B))
            ) {
                // Get images for card display (limited to 6)
                val displayImages = buddy.cardImages
                
                // Only use pager if there are multiple images
                if (displayImages.size > 1) {
                    val pagerState = rememberPagerState(pageCount = { displayImages.size })
                    
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = displayImages[page],
                            contentDescription = "${buddy.destination} - Image ${page + 1}",
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    showImageViewer = true
                                    initialImagePage = page
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    // Page indicators
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(displayImages.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pagerState.currentPage == index) Color.White
                                        else Color.White.copy(alpha = 0.5f)
                                    )
                            )
                        }
                    }
                    
                    // Image counter (bottom left) - show total images count
                    Text(
                        text = "${pagerState.currentPage + 1}/${buddy.allImages.size}",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                } else {
                    // Single image display
                    AsyncImage(
                        model = displayImages.firstOrNull() ?: "",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                showImageViewer = true
                                initialImagePage = 0
                            },
                        contentScale = ContentScale.Crop
                    )
                }

                // Badges
                if (buddy.tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.TopStart),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        buddy.tags.take(1).forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color(0xFF050A30).copy(alpha = 0.9f),
                                        RoundedCornerShape(2.dp)
                                    )
                                    .border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(2.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = tag.trim().uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFD4AF37),
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }
            }

            // Body section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Host info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .border(1.dp, Color(0xFFD4AF37), CircleShape)
                            .padding(2.dp)
                            .clip(CircleShape)
                    ) {
                        AsyncImage(
                            model = buddy.expandedUser?.avatar ?: "",
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = buddy.expandedUser?.fullName ?: "Unknown",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            // Enhanced verified badge - circular like BuddyHotCard
                            if (buddy.verified) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp) // Same size as BuddyHotCard
                                        .background(
                                            color = Color(0xFF2E7D32), // Darker green for more prominence
                                            shape = CircleShape
                                        )
                                        .border(1.5.dp, Color.White, CircleShape) // Thicker white border
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Verified",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⭐ ${buddy.rating}",
                                fontSize = 11.sp,
                                color = Color(0xFFA0B0D0)
                            )
                            Text(
                                text = "•",
                                fontSize = 11.sp,
                                color = Color(0xFFA0B0D0)
                            )
                            Text(
                                text = "${buddy.reviewsCount} trips",
                                fontSize = 11.sp,
                                color = Color(0xFFA0B0D0)
                            )
                        }
                    }
                }

                // Trip title
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 0.dp)
                ) {
                    Text(
                        text = buddy.tripTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        letterSpacing = 0.5.sp,
                        lineHeight = 26.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        minLines = 2 // Ensure consistent height
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📍",
                            fontSize = 12.sp
                        )
                        Text(
                            text = buddy.expandedPlace?.name ?: buddy.expandedCity?.name ?: buddy.destination,
                            fontSize = 11.sp,
                            color = Color(0xFF4FACFE),
                            letterSpacing = 1.sp
                        )
                    }

                    // Border line
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFF1F2D6B))
                    )
                }

                // Info grid
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1F2D6B), RoundedCornerShape(4.dp))
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Khởi hành
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color(0xFF08104D))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "KHỞI HÀNH",
                                        fontSize = 10.sp,
                                        color = Color(0xFFA0B0D0),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = formatDateFromTimestamp(buddy.startDate),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White,
                                        lineHeight = 14.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color(0xFF1F2D6B))
                            )

                            // Thời gian
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color(0xFF08104D))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "THỜI GIAN",
                                        fontSize = 10.sp,
                                        color = Color(0xFFA0B0D0),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = buddy.tripDuration,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFF1F2D6B))
                        )

                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Chi phí
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color(0xFF08104D))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Ngân sách",
                                        fontSize = 11.sp,
                                        color = Color(0xFFA0B0D0),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "${buddy.budgetMin.toInt()}-${buddy.budgetMax.toInt()}tr",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF2D06B),
                                        lineHeight = 14.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color(0xFF1F2D6B))
                            )

                            // Số chỗ
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color(0xFF08104D))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "TRẠNG THÁI",
                                        fontSize = 10.sp,
                                        color = Color(0xFFA0B0D0),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = when(buddy.status) {
                                            "URGENT" -> "Sắp chốt"
                                            "AVAILABLE" -> "Còn chỗ"
                                            "FULL" -> "Đã đủ"
                                            else -> "Còn chỗ"
                                        },
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Tags
                if (buddy.tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        buddy.tags.take(2).forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .border(1.dp, Color(0xFF1F2D6B), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = tag.trim(),
                                    fontSize = 11.sp,
                                    color = Color(0xFFD4AF37)
                                )
                            }
                        }
                    }
                }

                // Add requirements section like BuddyHotCard
                if (buddy.requirements.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1F2D6B).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "Yêu cầu bạn đồng hành:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFA0B0D0)
                            )
                            buddy.requirements.forEach { req ->
                                Text(
                                    text = "✓ $req",
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }

                // Footer
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFF1F2D6B))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Slots visual with dots
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Calculate filled slots for luxury card
                            val occupiedSlots = buddy.totalCapacity - buddy.availableSlots
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                repeat(buddy.totalCapacity) { index ->
                                    val isFilled = index < occupiedSlots
                                    val dotColor = when {
                                        buddy.availableSlots == 0 -> Color(0xFFE0E0E0) // All gray when full (0/x)
                                        buddy.availableSlots == 1 && !isFilled -> Color(0xFFFF6B35) // Highlight 1 dot for 1/x
                                        buddy.availableSlots == 2 && !isFilled -> Color(0xFFFF6B35) // Highlight 2 dots for 2/x
                                        isFilled -> Color(0xFFFF6B35) // Orange for filled
                                        else -> Color(0xFFE0E0E0) // Gray for empty
                                    }
                                    
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(dotColor, CircleShape)
                                    )
                                }
                            }
                            Text(
                                text = "Còn ${buddy.availableSlots}/${buddy.totalCapacity} chỗ",
                                fontSize = 12.sp,
                                color = Color(0xFFA0B0D0)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp) // Increased spacing like BuddyHotCard
                        ) {
                            // Icon button - Bookmark - increased size
                            Box(
                                modifier = Modifier
                                    .size(32.dp) // Increased from 28dp like BuddyHotCard
                                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp)) // Increased border radius
                                    .background(Color.White, RoundedCornerShape(6.dp))
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🔖",
                                    fontSize = 18.sp, // Increased font size like BuddyHotCard
                                    color = Color(0xFF757575)
                                )
                            }

                            // Icon button - Chat - increased size
                            Box(
                                modifier = Modifier
                                    .size(32.dp) // Increased from 28dp like BuddyHotCard
                                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp)) // Increased border radius
                                    .background(Color.White, RoundedCornerShape(6.dp))
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "💬",
                                    fontSize = 18.sp, // Increased font size like BuddyHotCard
                                    color = Color(0xFF757575)
                                )
                            }

                            // Detail button with icon - increased size
                            Button(
                                onClick = { onClick(buddy.id) },
                                modifier = Modifier.height(32.dp), // Increased from 28dp like BuddyHotCard
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF6B35)
                                ),
                                shape = RoundedCornerShape(6.dp), // Increased border radius
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp) // Increased horizontal padding
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Chi tiết",
                                        fontSize = 13.sp, // Increased font size like BuddyHotCard
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "View details",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp) // Increased icon size like BuddyHotCard
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Image viewer dialog
    if (showImageViewer) {
        ImageViewerDialog(
            images = buddy.allImages,
            initialPage = initialImagePage,
            onDismiss = { showImageViewer = false }
        )
    }
}
