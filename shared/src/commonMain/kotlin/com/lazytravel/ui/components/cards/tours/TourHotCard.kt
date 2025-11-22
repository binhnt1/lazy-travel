package com.lazytravel.ui.components.cards.tours

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.data.models.Tour
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.ui.utils.parseHexColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TourHotCard(
    tour: Tour,
    onClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(280.dp),
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
            // Tour image with overlays
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                // Background image
                if (tour.bgImage.isNotEmpty()) {
                    AsyncImage(
                        model = tour.bgImage,
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

                // Gradient overlay for better text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )

                // Badge row - top left
                if (!tour.badges.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tour.badges.take(2).forEach { badge ->
                            val (bgColor, textColor, icon) = when (badge.uppercase()) {
                                Tour.BADGE_HOT -> Triple(Color(0xFFE53935), Color.White, "🔥")
                                Tour.BADGE_SALE -> Triple(Color(0xFFFFEB3B), Color(0xFF333333), "")
                                Tour.BADGE_NEW -> Triple(Color(0xFF4CAF50), Color.White, "✨")
                                Tour.BADGE_FEATURED -> Triple(Color(0xFFF1C40F), Color(0xFF2C3E50), "⭐")
                                else -> Triple(Color(0xFF666666), Color.White, "")
                            }

                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = bgColor
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (icon.isNotEmpty()) {
                                        Text(
                                            text = icon,
                                            fontSize = 10.sp
                                        )
                                    }
                                    Text(
                                        text = badge,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = textColor,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
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
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Featured overlay - bottom
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    // Location
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = tour.expandedCity?.name ?: tour.expandedPlace?.name ?: "",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    // Stats
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Schedule,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.9f),
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = tour.getDurationText(),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )
                        }

                        if (tour.bookedCount > 0) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.People,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = "${tour.bookedCount} đã đặt",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Tour content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
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
                                modifier = Modifier
                                    .size(18.dp),
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

                        if (provider.isVerified) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Verified",
                                tint = AppColors.Primary,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }

                // Tour title
                Text(
                    text = tour.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF222222),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Tour features - parsed from included list
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    // Airline from expanded relation
                    tour.expandedFlightProvider?.let { airline ->
                        FeatureTag(
                            icon = Icons.Filled.Flight,
                            text = "Bay ${airline.name}"
                        )
                    }

                    // Parse hotel stars from included list
                    tour.included?.find { it.contains("Khách sạn", ignoreCase = true) }?.let { hotelStr ->
                        val stars = Regex("(\\d+)\\s*\\*").find(hotelStr)?.groupValues?.get(1)
                        if (stars != null) {
                            FeatureTag(
                                icon = null,
                                text = "$stars sao",
                                emoji = "🏨"
                            )
                        }
                    }

                    // Parse meals from included list
                    tour.included?.find { it.contains("bữa ăn", ignoreCase = true) }?.let { mealStr ->
                        val mealsCount = Regex("(\\d+)\\s*bữa").find(mealStr)?.groupValues?.get(1)
                        if (mealsCount != null) {
                            FeatureTag(
                                icon = null,
                                text = "$mealsCount bữa ăn",
                                emoji = "🍽️"
                            )
                        }
                    }

                    // Parse transport from included list
                    tour.included?.find { it.contains("xe", ignoreCase = true) || it.contains("đưa đón", ignoreCase = true) }?.let {
                        FeatureTag(
                            icon = null,
                            text = "Xe đưa đón",
                            emoji = "🚌"
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFF0F0F0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom section - price and rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Price block
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        if (tour.originalPrice > 0) {
                            Text(
                                text = tour.getFormattedOriginalPrice(),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    textDecoration = TextDecoration.LineThrough
                                ),
                                color = Color(0xFF999999),
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = tour.getFormattedPrice(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53935),
                            fontSize = 16.sp
                        )
                        Text(
                            text = "/ ${localizedString("per_person")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF888888),
                            fontSize = 10.sp
                        )
                    }

                    // Rating block
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            repeat(5) { index ->
                                Text(
                                    text = "★",
                                    color = if (index < tour.rating.toInt()) Color(0xFFFFC107) else Color(0xFFE0E0E0),
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Text(
                            text = "${tour.rating} (${tour.reviewCount} đánh giá)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF666666),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureTag(
    icon: ImageVector? = null,
    text: String,
    emoji: String? = null
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Color(0xFFF5F5F5)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (emoji != null) {
                Text(
                    text = emoji,
                    fontSize = 10.sp
                )
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF888888),
                    modifier = Modifier.size(10.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF555555),
                fontSize = 10.sp
            )
        }
    }
}
