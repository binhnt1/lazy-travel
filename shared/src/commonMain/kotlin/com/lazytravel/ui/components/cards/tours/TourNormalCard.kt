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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TourNormalCard(
    tour: Tour,
    onClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(260.dp),
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
            // Tour image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
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

                // Discount badge if available
                if (tour.discount > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFE53935)
                    ) {
                        Text(
                            text = "-${tour.discount}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
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
                                modifier = Modifier.size(16.dp),
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
                                imageVector = Icons.Default.CheckCircle,
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

                // Tour features (simplified)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    // Duration
                    SimpleFeatureTag(
                        emoji = "⏱️",
                        text = tour.getDurationText()
                    )

                    // Location
                    tour.expandedCity?.name?.let { city ->
                        SimpleFeatureTag(
                            emoji = "📍",
                            text = city
                        )
                    }

                    // Rating
                    if (tour.rating > 0) {
                        SimpleFeatureTag(
                            emoji = "⭐",
                            text = tour.rating.toString()
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFF0F0F0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom section - price and reviews
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
                            color = AppColors.Primary,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "/ ${localizedString("per_person")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF888888),
                            fontSize = 10.sp
                        )
                    }

                    // Review count
                    if (tour.reviewCount > 0) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "${tour.reviewCount} đánh giá",
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
}

@Composable
private fun SimpleFeatureTag(
    emoji: String,
    text: String
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
