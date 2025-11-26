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
fun BuddyNormalCard(
    buddy: Buddy,
    onClick: (String) -> Unit = {}
) {
    var showImageViewer by remember { mutableStateOf(false) }
    var initialImagePage by remember { mutableStateOf(0) }
    // Calculate filled slots (occupied = maxParticipants - availableSlots)
    val occupiedSlots = buddy.maxParticipants - buddy.availableSlots

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .clickable { onClick(buddy.id) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Trip Header - Host info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with gradient background or initials
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFF44336), Color(0xFFE91E63))
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (buddy.expandedUser?.avatar.isNullOrEmpty()) {
                        // Show initials if no avatar
                        Text(
                            text = buddy.expandedUser?.fullName?.take(2)?.uppercase() ?: "??",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    } else {
                        AsyncImage(
                            model = buddy.expandedUser?.avatar ?: "",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Host info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = buddy.expandedUser?.fullName ?: "Unknown",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF212121)
                        )
                        // Enhanced verified badge - circular like BuddyHotCard and BuddyLuxuryCard
                        if (buddy.verified) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp) // Same size as BuddyHotCard and BuddyLuxuryCard
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
                        Text(text = "⭐ ${buddy.rating}", fontSize = 13.sp, color = Color(0xFF666666))
                        Text(text = "•", fontSize = 13.sp, color = Color(0xFF666666))
                        Text(text = "${buddy.reviewsCount} trips", fontSize = 13.sp, color = Color(0xFF666666))
                    }
                }

                // Status badge
                Box(
                    modifier = Modifier
                        .background(
                            color = if (buddy.status == "URGENT") Color(0xFFFFEBEE) else Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (buddy.status == "URGENT") "Gấp!" else "Hot 🔥",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (buddy.status == "URGENT") Color(0xFFF44336) else Color(0xFFFF9800)
                    )
                }
            }

            // Image carousel section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
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
                        contentDescription = buddy.tripTitle,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                showImageViewer = true
                                initialImagePage = 0
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Trip Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Enhanced Destination Section with Creative Design
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFF0F8FF), // Light blue
                                    Color(0xFFE6F3FF)  // Slightly darker blue
                                )
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column {
                        // Trip title with emoji badge
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Enhanced emoji badge
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFFF6B35),
                                                Color(0xFFFF8C42)
                                            )
                                        ),
                                        RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = buddy.emoji.ifEmpty { "🏖️" },
                                    fontSize = 24.sp
                                )
                            }
                            
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                // Trip title with single line
                                Text(
                                    text = buddy.tripTitle,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A237E),
                                    maxLines = 1, // Changed to 1 line
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 22.sp
                                )
                                
                                Spacer(modifier = Modifier.height(6.dp))
                                
                                // Location with view count in same row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Location with icon
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .background(
                                                    Color(0xFF4CAF50).copy(alpha = 0.2f),
                                                    RoundedCornerShape(10.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "📍",
                                                fontSize = 12.sp
                                            )
                                        }
                                        Text(
                                            text = buddy.expandedPlace?.name ?: buddy.expandedCity?.name ?: buddy.destination,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF2E7D32)
                                        )
                                    }
                                    
                                    // View count indicator (moved here)
                                    if (buddy.viewCount > 0) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .background(Color(0xFF4CAF50), CircleShape)
                                            )
                                            Text(
                                                text = "${buddy.viewCount} người đang xem",
                                                fontSize = 12.sp,
                                                color = Color(0xFF4CAF50),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Info chips (2x2 grid layout)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ColoredInfoChip(
                            text = "📅 ${formatDateFromTimestamp(buddy.startDate)}",
                            backgroundColor = Color(0xFFE3F2FD),
                            textColor = Color(0xFF1976D2),
                            modifier = Modifier.weight(1f)
                        )
                        ColoredInfoChip(
                            text = "⏱️ ${buddy.tripDuration}",
                            backgroundColor = Color(0xFFF3E5F5),
                            textColor = Color(0xFF7B1FA2),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ColoredInfoChip(
                            text = "💰 ${buddy.budgetMin.toInt()}-${buddy.budgetMax.toInt()} triệu",
                            backgroundColor = Color(0xFFFFF3E0),
                            textColor = Color(0xFFE65100),
                            modifier = Modifier.weight(1f)
                        )
                        ColoredInfoChip(
                            text = "🎂 ${buddy.ageRange} tuổi",
                            backgroundColor = Color(0xFFE8F5E9),
                            textColor = Color(0xFF388E3C),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Description
                Text(
                    text = buddy.description,
                    fontSize = 13.sp,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                // Tags
                if (buddy.interests.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        buddy.interests.take(3).forEach { interest ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = interest,
                                    fontSize = 12.sp,
                                    color = Color(0xFF666666)
                                )
                            }
                        }
                    }
                }

                // Requirements (light gray background box)
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

                // Members preview
                if (buddy.expandedParticipants.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        // Overlapping avatars
                        buddy.expandedParticipants.take(3).forEachIndexed { index, user ->
                            val colors = listOf(
                                Color(0xFFF44336), Color(0xFF9C27B0), Color(0xFFE91E63),
                                Color(0xFFFF9800), Color(0xFF00BCD4), Color(0xFF4CAF50)
                            )
                            Box(
                                modifier = Modifier
                                    .offset(x = (index * (-8)).dp)
                                    .size(28.dp)
                                    .border(2.dp, Color.White, RoundedCornerShape(6.dp))
                                    .background(colors[index % colors.size], RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (user.avatar.isNullOrEmpty()) {
                                    // Show initials
                                    Text(
                                        text = user.fullName.take(2).uppercase(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                } else {
                                    AsyncImage(
                                        model = user.avatar,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(6.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "${buddy.expandedParticipants.size} thành viên đã tham gia",
                            fontSize = 13.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }

            // Trip Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAFAFA))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Slots visual
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(buddy.maxParticipants) { index ->
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
                                    .size(8.dp)
                                    .background(dotColor, CircleShape)
                            )
                        }
                    }
                    Text(
                        text = "Còn ${buddy.availableSlots}/${buddy.maxParticipants} chỗ",
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )
                }

                // Actions
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Increased spacing further
                ) {
                    // Icon button - Bookmark - even larger size
                    Box(
                        modifier = Modifier
                            .size(36.dp) // Increased from 32dp to 36dp
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp)) // Increased border radius
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🔖",
                            fontSize = 20.sp, // Increased from 18sp to 20sp
                            color = Color(0xFF757575)
                        )
                    }

                    // Icon button - Chat - even larger size
                    Box(
                        modifier = Modifier
                            .size(36.dp) // Increased from 32dp to 36dp
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp)) // Increased border radius
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "💬",
                            fontSize = 20.sp, // Increased from 18sp to 20sp
                            color = Color(0xFF757575)
                        )
                    }

                    // Detail button with icon - even larger size
                    Button(
                        onClick = { onClick(buddy.id) },
                        modifier = Modifier.height(36.dp), // Increased from 32dp to 36dp
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B35)
                        ),
                        shape = RoundedCornerShape(8.dp), // Increased border radius
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp) // Increased horizontal padding
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Chi tiết",
                                fontSize = 14.sp, // Increased from 13sp to 14sp
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "View details",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp) // Increased from 16dp to 18dp
                            )
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

@Composable
private fun ColoredInfoChip(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}