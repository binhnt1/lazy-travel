package com.lazytravel.ui.components.cards.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.data.models.Buddy
import com.lazytravel.helpers.formatDateFromTimestamp

@Composable
fun BuddyLuxuryCard(
    buddy: Buddy,
    onClick: (String) -> Unit = {}
) {
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
                AsyncImage(
                    model = buddy.coverImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

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
                                    fontSize = 9.sp,
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
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            if (buddy.verified) {
                                Text(
                                    text = "✓",
                                    fontSize = 10.sp,
                                    color = Color(0xFF4FACFE)
                                )
                            }
                        }
                        Text(
                            text = "${buddy.rating} ⭐ • ${buddy.reviewsCount} đánh giá",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Light,
                            color = Color(0xFFA0B0D0)
                        )
                    }
                }

                // Trip title
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 0.dp)
                ) {
                    Text(
                        text = buddy.destination,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        letterSpacing = 0.5.sp,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📍",
                            fontSize = 10.sp
                        )
                        Text(
                            text = (buddy.expandedPlace?.name ?: buddy.destination).uppercase(),
                            fontSize = 10.sp,
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
                                        fontSize = 9.sp,
                                        color = Color(0xFFA0B0D0),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = formatDateFromTimestamp(buddy.startDate),
                                        fontSize = 12.sp,
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
                                        fontSize = 9.sp,
                                        color = Color(0xFFA0B0D0),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = buddy.tripDuration,
                                        fontSize = 12.sp,
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
                                        fontSize = 10.sp,
                                        color = Color(0xFFA0B0D0),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "${buddy.budgetMin.toInt()}-${buddy.budgetMax.toInt()}tr",
                                        fontSize = 12.sp,
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
                                        fontSize = 9.sp,
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
                                        fontSize = 12.sp,
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
                                    fontSize = 10.sp,
                                    color = Color(0xFFD4AF37)
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
                        Text(
                            text = "Còn ${buddy.availableSlots}/${buddy.totalCapacity} chỗ",
                            fontSize = 11.sp,
                            color = Color(0xFFA0B0D0)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Icon button - Bookmark
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                                    .background(Color.White, RoundedCornerShape(4.dp))
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🔖",
                                    fontSize = 14.sp,
                                    color = Color(0xFF757575)
                                )
                            }

                            // Icon button - Chat
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                                    .background(Color.White, RoundedCornerShape(4.dp))
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "💬",
                                    fontSize = 14.sp,
                                    color = Color(0xFF757575)
                                )
                            }

                            // Detail button
                            Button(
                                onClick = { onClick(buddy.id) },
                                modifier = Modifier.height(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF6B35)
                                ),
                                shape = RoundedCornerShape(4.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                            ) {
                                Text(
                                    text = "Chi tiết",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
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

