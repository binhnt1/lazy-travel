package com.lazytravel.ui.components.cards.buddies

import androidx.compose.animation.core.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
fun BuddyHotCard(
    buddy: Buddy,
    onClick: (String) -> Unit = {}
) {
    var showImageViewer by remember { mutableStateOf(false) }
    var initialImagePage by remember { mutableStateOf(0) }
    
    // Animation for the hot badge
    val infiniteTransition = rememberInfiniteTransition(label = "hotBadge")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Box(
        modifier = Modifier
            .width(290.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8F5), // White at top
                        Color(0xFFFFF8F5)  // Very light orange at bottom
                    )
                ),
                RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFFF6B35), // Simple hot orange border
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick(buddy.id) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Enhanced image section - increased height
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp) // Increased from 150dp
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
                                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                .clickable {
                                    showImageViewer = true
                                    initialImagePage = page
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    // Enhanced page indicators
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
                                    .border(
                                        width = 0.5.dp,
                                        color = Color(0xFFFF6B35).copy(alpha = 0.3f),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                    
                    // Enhanced image counter (bottom left)
                    Text(
                        text = "${pagerState.currentPage + 1}/${buddy.allImages.size}",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(12.dp)) // Increased opacity
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                } else {
                    // Single image display
                    AsyncImage(
                        model = displayImages.firstOrNull() ?: "",
                        contentDescription = buddy.tripTitle,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            .clickable {
                                showImageViewer = true
                                initialImagePage = 0
                            },
                        contentScale = ContentScale.Crop
                    )
                }

                // Enhanced HOT badge with animation (top-left) - fixed verified badge
                if (buddy.badgeText.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFF6B35), // Hot orange
                                        Color(0xFFFF8C42), // Lighter orange
                                        Color(0xFFFFA726)  // Even lighter orange
                                    )
                                ),
                                RoundedCornerShape(6.dp)
                            )
                            .scale(pulseScale)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "🔥",
                                fontSize = 14.sp
                            )
                            Text(
                                text = buddy.badgeText.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
                
                // Animated flame indicator (top-right)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .scale(pulseScale)
                ) {
                    Text(
                        text = "🔥",
                        fontSize = 22.sp
                    )
                }
            }

            // Enhanced body content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Enhanced host row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Enhanced avatar - larger size
                    Box(
                        modifier = Modifier
                            .size(40.dp) // Increased from 36dp
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFF6B35),
                                        Color(0xFFFF8C42)
                                    )
                                ),
                                RoundedCornerShape(50)
                            )
                            .padding(2.dp)
                    ) {
                        AsyncImage(
                            model = buddy.expandedUser?.avatar ?: "",
                            contentDescription = buddy.expandedUser?.fullName ?: "User",
                            modifier = Modifier
                                .size(36.dp)// Increased from 32dp
                                .clip(RoundedCornerShape(50))
                                .border(2.dp, Color.White, RoundedCornerShape(50)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Host info column
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Host name with enhanced verified badge
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = buddy.expandedUser?.fullName ?: "Unknown",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121)
                            )

                            // Enhanced verified badge - circular
                            if (buddy.verified) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp) // Increased size for better visibility
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

                        // Enhanced rating row with trips count like BuddyNormalCard
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⭐ ${buddy.rating}",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                text = "•",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                text = "${buddy.reviewsCount} trips",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Enhanced reviews count badge - show only 1 review like BuddyNormalCard
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFFF3E0),
                                        Color(0xFFFFE0B2)
                                    )
                                ),
                                RoundedCornerShape(4.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color(0xFFFF6B35).copy(alpha = 0.3f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${buddy.reviewsCount} review", // Changed to singular
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B35)
                        )
                    }
                }

                // Trip title block - reduced spacing and single line
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp), // Reduced spacing
                    verticalArrangement = Arrangement.spacedBy(1.dp) // Reduced spacing
                ) {
                    Text(
                        text = buddy.tripTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        maxLines = 1, // Changed to 1 line
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.sp
                    )

                    // Region
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "📍",
                            fontSize = 13.sp
                        )
                        Text(
                            text = buddy.expandedPlace?.name ?: buddy.expandedCity?.name ?: buddy.destination,
                            fontSize = 12.sp,
                            color = Color(0xFF757575)
                        )
                    }
                }

                // Grid info (2x2)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFFFCCBC), RoundedCornerShape(6.dp))
                        .clip(RoundedCornerShape(6.dp))
                ) {
                    Column {
                        // Row 1
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFF3E0))
                        ) {
                            // Khởi hành
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "KHỞI HÀNH",
                                        fontSize = 10.sp,
                                        color = Color(0xFFE64A19),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp
                                    )
                                    Text(
                                        text = formatDateFromTimestamp(buddy.startDate),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFBF360C),
                                        lineHeight = 12.sp
                                    )
                                }
                            }

                            // Divider
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color(0xFFFFCCBC))
                            )

                            // Thời gian
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "THỜI GIAN",
                                        fontSize = 10.sp,
                                        color = Color(0xFFE64A19),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp
                                    )
                                    Text(
                                        text = buddy.tripDuration,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFBF360C),
                                        lineHeight = 12.sp
                                    )
                                }
                            }
                        }

                        // Divider horizontal
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFFFCCBC))
                        )

                        // Row 2
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFF3E0))
                        ) {
                            // Ngân sách
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "NGÂN SÁCH",
                                        fontSize = 10.sp,
                                        color = Color(0xFFE64A19),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp
                                    )
                                    Text(
                                        text = "${buddy.budgetMin.toInt()}-${buddy.budgetMax.toInt()}tr",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFBF360C),
                                        lineHeight = 12.sp
                                    )
                                }
                            }

                            // Divider
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color(0xFFFFCCBC))
                            )

                            // Trạng thái
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "TRẠNG THÁI",
                                        fontSize = 10.sp,
                                        color = Color(0xFFE64A19),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp
                                    )
                                    Text(
                                        text = when(buddy.status) {
                                            "URGENT" -> "Sắp chốt"
                                            "AVAILABLE" -> "Còn chỗ"
                                            "FULL" -> "Đã đủ"
                                            else -> "Còn chỗ"
                                        },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFBF360C),
                                        lineHeight = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Enhanced trip tags - more prominent
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp) // Increased spacing
                ) {
                    buddy.tags.take(2).forEach { tag ->
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color(0xFFFF6B35).copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp) // Increased padding
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp, // Increased font size
                                fontWeight = FontWeight.SemiBold, // Added font weight
                                color = Color(0xFFFF6B35)
                            )
                        }
                    }
                }

                // Add requirements section like BuddyNormalCard
                if (buddy.requirements.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "Yêu cầu bạn đồng hành:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF666666)
                            )
                            buddy.requirements.forEach { req ->
                                Text(
                                    text = "✓ $req",
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = Color(0xFF444444),
                                )
                            }
                        }
                    }
                }

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE0E0E0))
                        .padding(vertical = 8.dp)
                )

                // Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Enhanced slots visual with dots like BuddyNormalCard
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        // Calculate filled slots for hot card
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
                                        .size(8.dp) // Increased size to match BuddyNormalCard
                                        .background(dotColor, CircleShape)
                                )
                            }
                        }
                        Text(
                            text = "Còn ${buddy.availableSlots}/${buddy.totalCapacity} chỗ",
                            fontSize = 12.sp,
                            color = Color(0xFF757575)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp) // Increased spacing
                    ) {
                        // Icon button - Bookmark - increased size
                        Box(
                            modifier = Modifier
                                .size(32.dp) // Increased from 28dp
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp)) // Increased border radius
                                .background(Color.White, RoundedCornerShape(6.dp))
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🔖",
                                fontSize = 18.sp, // Increased font size
                                color = Color(0xFF757575)
                            )
                        }

                        // Icon button - Chat - increased size
                        Box(
                            modifier = Modifier
                                .size(32.dp) // Increased from 28dp
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp)) // Increased border radius
                                .background(Color.White, RoundedCornerShape(6.dp))
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "💬",
                                fontSize = 18.sp, // Increased font size
                                color = Color(0xFF757575)
                            )
                        }

                        // Detail button with icon - increased size
                        Button(
                            onClick = { onClick(buddy.id) },
                            modifier = Modifier.height(32.dp), // Increased from 28dp
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
                                    fontSize = 13.sp, // Increased font size
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "View details",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp) // Increased icon size
                                )
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
