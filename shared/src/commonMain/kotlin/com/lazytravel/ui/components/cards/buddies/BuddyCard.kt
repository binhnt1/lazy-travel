package com.lazytravel.ui.components.cards.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.models.Buddy
import com.lazytravel.helpers.formatDateFromTimestamp
import com.lazytravel.ui.theme.AppColors
import kotlin.collections.chunked
import kotlin.collections.forEach


@Composable
fun BuddyCard(
    buddy: Buddy,
    modifier: Modifier = Modifier,
    onJoinClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Banner with emoji
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        color = when (buddy.emoji) {
                            "🏖️" -> Color(0xFF667EEA).copy(alpha = 0.15f)
                            "⛰️" -> Color(0xFFF093FB).copy(alpha = 0.15f)
                            "🛕" -> Color(0xFFFF9800).copy(alpha = 0.15f)
                            else -> Color(0xFF667EEA).copy(alpha = 0.15f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buddy.emoji,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 48.sp
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Buddy header with avatar and info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Avatar circle - use expandedUser avatar if available
                    val user = buddy.expandedUser
                    if (user != null && user.avatar.isNotEmpty()) {
                        // TODO: Load avatar image using AsyncImage or Coil
                        // For now, show initials as fallback
                        val initials = user.fullName.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("").uppercase()
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(AppColors.Primary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.Primary
                            )
                        }
                    } else {
                        // Fallback to initials from userId
                        val initials = buddy.userId.take(2).uppercase()
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(AppColors.Primary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.Primary
                            )
                        }
                    }

                    // Buddy info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user?.fullName ?: "Unknown",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (buddy.verified) {
                                Text(
                                    text = "✓",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                            Text(
                                text = if (buddy.verified) "Đã xác minh" else "Chưa xác minh",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (buddy.verified) Color(0xFF4CAF50) else Color(0xFF999999)
                            )
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF999999)
                            )
                            Text(
                                text = "⭐ ${(buddy.rating * 10).toInt() / 10.0} (${buddy.reviewsCount})",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF999999)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Trip info
                Column {
                    // Trip title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = buddy.emoji,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = buddy.tripTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Trip details grid
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8F9FA), RoundedCornerShape(6.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TripDetailItem(icon = "📅", text = formatDateFromTimestamp(buddy.startDate), modifier = Modifier.weight(1f))
                            TripDetailItem(icon = "💰", text = "${buddy.budgetMin.toInt()}-${buddy.budgetMax.toInt()}tr", modifier = Modifier.weight(1f))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TripDetailItem(icon = "👥", text = "Tổng ${buddy.totalCapacity} người", modifier = Modifier.weight(1f))
                            TripDetailItem(icon = "🎯", text = "Còn ${buddy.availableSlots} chỗ", modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                Text(
                    text = buddy.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 18.sp
                    ),
                    color = Color(0xFF424242),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tags
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    buddy.tags.chunked(2).forEach { tagRow ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            tagRow.forEach { tag ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp)),
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFFAFAFA)
                                ) {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF666666),
                                        modifier = Modifier.padding(6.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Status and button
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Status badge
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        color = when (buddy.status) {
                            "URGENT" -> Color(0xFFFFEBEE)
                            else -> Color(0xFFE8F5E9)
                        }
                    ) {
                        Text(
                            text = when (buddy.status) {
                                "URGENT" -> "🔥 Sắp đầy - Chỉ còn ${buddy.availableSlots} chỗ!"
                                else -> "⏰ Còn ${buddy.availableSlots} chỗ trống"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = when (buddy.status) {
                                "URGENT" -> Color(0xFFC62828)
                                else -> Color(0xFF2E7D32)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                    }

                    // Join button
                    Button(
                        onClick = onJoinClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Primary
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Tham gia ngay",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TripDetailItem(
    icon: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF666666),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
