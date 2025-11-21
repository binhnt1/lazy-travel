package com.lazytravel.ui.components.cards.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.data.models.Buddy
import com.lazytravel.helpers.formatDateFromTimestamp

@Composable
fun BuddyHotCard(
    buddy: Buddy,
    onClick: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(290.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFFF6B35), RoundedCornerShape(8.dp))
            .clickable { onClick(buddy.id) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                AsyncImage(
                    model = buddy.coverImage,
                    contentDescription = buddy.destination,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )

                // HOT badge (top-left)
                if (buddy.badgeText.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                            .background(Color(0xFFFF6B35), RoundedCornerShape(4.dp))
                            .shadow(2.dp, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = buddy.badgeText.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Body content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Host row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    AsyncImage(
                        model = buddy.expandedUser?.avatar ?: "",
                        contentDescription = buddy.expandedUser?.fullName ?: "User",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(50))
                            .border(2.dp, Color(0xFFFFF3EE), RoundedCornerShape(50)),
                        contentScale = ContentScale.Crop
                    )

                    // Host info column
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        // Host name with verified badge
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = buddy.expandedUser?.fullName ?: "Unknown",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF212121)
                            )

                            // Verified badge
                            if (buddy.verified) {
                                Text(
                                    text = "✓",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }

                        // Rating row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⭐",
                                fontSize = 10.sp
                            )
                            Text(
                                text = "${(buddy.rating * 10).toInt() / 10.0}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFFFA726)
                            )
                            Text(
                                text = "•",
                                fontSize = 10.sp,
                                color = Color(0xFF9E9E9E)
                            )
                            Text(
                                text = "${buddy.reviewsCount} đánh giá",
                                fontSize = 10.sp,
                                color = Color(0xFF757575)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Reviews count badge
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF3E0), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${buddy.reviewsCount} reviews",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFFF6B35)
                        )
                    }
                }

                // Trip title block
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = (buddy.expandedPlace?.name ?: buddy.destination).uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )

                    // Region
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "📍",
                            fontSize = 11.sp
                        )
                        Text(
                            text = buddy.expandedCity?.name ?: buddy.destination,
                            fontSize = 11.sp,
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
                                        fontSize = 9.sp,
                                        color = Color(0xFFE64A19),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp
                                    )
                                    Text(
                                        text = formatDateFromTimestamp(buddy.startDate),
                                        fontSize = 11.sp,
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
                                        fontSize = 9.sp,
                                        color = Color(0xFFE64A19),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp
                                    )
                                    Text(
                                        text = buddy.tripDuration,
                                        fontSize = 11.sp,
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
                                        fontSize = 9.sp,
                                        color = Color(0xFFE64A19),
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 12.sp
                                    )
                                    Text(
                                        text = "${buddy.budgetMin.toInt()}-${buddy.budgetMax.toInt()}tr",
                                        fontSize = 11.sp,
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
                                        fontSize = 9.sp,
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
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFBF360C),
                                        lineHeight = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Trip tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    buddy.tags.take(2).forEach { tag ->
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFF3EE), RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFFFFCCBC), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                color = Color(0xFFFF6B35)
                            )
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
                    // Slots text
                    Text(
                        text = "Còn ${buddy.availableSlots}/${buddy.totalCapacity} chỗ",
                        fontSize = 11.sp,
                        color = Color(0xFF757575)
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
