package com.lazytravel.ui.components.cards.tours

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.data.models.Tour
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.ui.utils.parseHexColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TourLuxuryCard(
    tour: Tour,
    onClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val goldColor = Color(0xFFD4AF37)
    val darkGold = Color(0xFF5D4E0E)

    Card(
        onClick = onClick,
        modifier = modifier.width(300.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, goldColor.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFFEF5),
                            Color(0xFFFFF9E6)
                        )
                    )
                )
        ) {
            // Tour image with overlays
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                // Background image
                val bgImage = tour.images?.firstOrNull() ?: ""
                if (bgImage.isNotEmpty()) {
                    AsyncImage(
                        model = bgImage,
                        contentDescription = tour.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = parseHexColor(tour.thumbnailColor) ?: AppColors.Primary
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tour.emoji,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 48.sp
                            )
                        )
                    }
                }

                // LUXURY badge - top left
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(goldColor, Color(0xFFF4E4A6))
                                )
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👑",
                            fontSize = 10.sp
                        )
                        Text(
                            text = "LUXURY",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = darkGold,
                            fontSize = 10.sp
                        )
                    }
                }

                // Favorite button - top right
                Surface(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            // Tour content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Tour company
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    tour.expandedTourProvider?.let { provider ->
                        if (provider.logo.isNotEmpty()) {
                            AsyncImage(
                                model = provider.logo,
                                contentDescription = provider.name,
                                modifier = Modifier.size(18.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Text(
                            text = provider.name,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666666),
                            fontSize = 11.sp
                        )

                        // Gem icon for luxury
                        Text(
                            text = "💎",
                            fontSize = 10.sp
                        )
                    }
                }

                // Tour title
                Text(
                    text = tour.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Luxury amenities - parsed from included list
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    // Airline/Flight class from expanded relation
                    tour.expandedFlightProvider?.let { airline ->
                        val flightClass = tour.included?.find {
                            it.contains("Business", ignoreCase = true) ||
                            it.contains("First Class", ignoreCase = true)
                        }?.let { str ->
                            when {
                                str.contains("First Class", ignoreCase = true) -> "First Class"
                                str.contains("Business", ignoreCase = true) -> "Business"
                                else -> airline.name
                            }
                        } ?: airline.name

                        LuxuryAmenityTag(
                            emoji = "✈️",
                            text = flightClass
                        )
                    }

                    // Parse hotel stars from included list
                    tour.included?.find { it.contains("Khách sạn", ignoreCase = true) || it.contains("sao", ignoreCase = true) }?.let { hotelStr ->
                        val stars = Regex("(\\d+)\\s*sao").find(hotelStr)?.groupValues?.get(1)
                        if (stars != null) {
                            LuxuryAmenityTag(
                                emoji = "🏨",
                                text = "$stars sao${if (hotelStr.contains("+")) "+" else ""}"
                            )
                        }
                    }

                    // HDV riêng (private guide)
                    tour.included?.find {
                        it.contains("HDV", ignoreCase = true) ||
                        it.contains("hướng dẫn viên", ignoreCase = true)
                    }?.let {
                        LuxuryAmenityTag(
                            emoji = "👔",
                            text = "HDV riêng"
                        )
                    }

                    // VIP transport
                    tour.included?.find {
                        it.contains("VIP", ignoreCase = true) ||
                        it.contains("xe riêng", ignoreCase = true)
                    }?.let {
                        LuxuryAmenityTag(
                            emoji = "🚗",
                            text = "Xe VIP"
                        )
                    }

                    // Michelin restaurant
                    tour.included?.find { it.contains("Michelin", ignoreCase = true) }?.let {
                        LuxuryAmenityTag(
                            emoji = "🍽️",
                            text = "Michelin"
                        )
                    }

                    // Cruise/Du thuyền
                    tour.included?.find {
                        it.contains("du thuyền", ignoreCase = true) ||
                        it.contains("cruise", ignoreCase = true)
                    }?.let {
                        LuxuryAmenityTag(
                            emoji = "🚢",
                            text = "Du thuyền"
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0)
                )

                // Bottom section - price and button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price
                    Text(
                        text = tour.getFormattedPrice(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = goldColor,
                        fontSize = 18.sp
                    )

                    // Book button
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(goldColor, Color(0xFFF4E4A6))
                                ),
                                shape = RoundedCornerShape(6.dp)
                            ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Đặt ngay",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = darkGold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LuxuryAmenityTag(
    emoji: String,
    text: String
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Color(0xFFF5F5F5),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                fontSize = 10.sp
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF555555),
                fontSize = 10.sp
            )
        }
    }
}
