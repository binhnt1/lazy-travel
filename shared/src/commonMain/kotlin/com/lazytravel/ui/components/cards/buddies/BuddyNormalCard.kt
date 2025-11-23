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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.data.models.Buddy
import com.lazytravel.helpers.formatDateFromTimestamp

@Composable
fun BuddyNormalCard(
    buddy: Buddy,
    onClick: (String) -> Unit = {}
) {
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
                            fontSize = 16.sp,
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
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF212121)
                        )
                        if (buddy.verified) {
                            Text(
                                text = "✓",
                                fontSize = 12.sp,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⭐ ${buddy.rating}", fontSize = 12.sp, color = Color(0xFF666666))
                        Text(text = "•", fontSize = 12.sp, color = Color(0xFF666666))
                        Text(text = "${buddy.reviewsCount} trips", fontSize = 12.sp, color = Color(0xFF666666))
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
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (buddy.status == "URGENT") Color(0xFFF44336) else Color(0xFFFF9800)
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
                // Activity indicator (if viewCount > 0)
                if (buddy.viewCount > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
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

                // Destination
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = buddy.emoji.ifEmpty { "🏖️" }, fontSize = 20.sp)
                    Column {
                        Text(
                            text = buddy.destination,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                        Text(
                            text = buddy.expandedCity?.name ?: "Việt Nam",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
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
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    lineHeight = 18.sp,
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
                                    fontSize = 11.sp,
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
                            .padding(10.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Yêu cầu bạn đồng hành:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF666666)
                            )
                            buddy.requirements.forEach { req ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(text = "✓", fontSize = 11.sp, color = Color(0xFF666666))
                                    Text(text = req, fontSize = 11.sp, color = Color(0xFF444444))
                                }
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
                                        fontSize = 10.sp,
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
                            fontSize = 12.sp,
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
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        // Filled dots = occupied slots (maxParticipants - availableSlots)
                                        // Empty dots = available slots
                                        if (index < occupiedSlots) {
                                            Color(0xFFFF6B35) // Orange for filled
                                        } else {
                                            Color(0xFFE0E0E0) // Gray for empty
                                        },
                                        CircleShape
                                    )
                            )
                        }
                    }
                    Text(
                        text = "Còn ${buddy.availableSlots}/${buddy.maxParticipants} chỗ",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }

                // Actions
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
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}