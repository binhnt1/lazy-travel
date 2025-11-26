package com.lazytravel.ui.components.cards.tours

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ExpandLess

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TourHotCard(
    tour: Tour,
    onClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showImageViewer by remember { mutableStateOf(false) }
    var showHighlights by remember { mutableStateOf(false) }
    var showIncluded by remember { mutableStateOf(false) }
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
                    .height(170.dp)
            ) {
                // Get images for card display (limited to 6)
                val displayImages = tour.cardImages

                // Only use pager if there are multiple images
                if (displayImages.size > 1) {
                    val pagerState = rememberPagerState(pageCount = { displayImages.size })

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    if (tour.allImages.isNotEmpty()) {
                                        showImageViewer = true
                                    }
                                }
                        ) {
                            AsyncImage(
                                model = displayImages[page],
                                contentDescription = "${tour.name} - Image ${page + 1}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

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
                        }
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

                    // Image counter (bottom left)
                    Text(
                        text = "${pagerState.currentPage + 1}/${tour.allImages.size}",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                } else {
                    // Single image display
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                if (tour.allImages.isNotEmpty()) {
                                    showImageViewer = true
                                }
                            }
                    ) {
                        val bgImage = displayImages.firstOrNull() ?: ""
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
                    }
                }

                // Badge row - top left (from tags)
                val badgeTags = tour.tags?.filter { tag ->
                    tag.contains("🔥", ignoreCase = true) ||
                    tag.contains("✨", ignoreCase = true) ||
                    tag.contains("Budget", ignoreCase = true) ||
                    tag.contains("Best Seller", ignoreCase = true) ||
                    tag.contains("Top Rated", ignoreCase = true)
                }?.take(2)

                if (!badgeTags.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        badgeTags.forEach { tag ->
                            val (bgColor, textColor) = when {
                                tag.contains("🔥") -> Pair(Color(0xFFE53935), Color.White)
                                tag.contains("✨") -> Pair(Color(0xFFF1C40F), Color(0xFF2C3E50))
                                tag.contains("Budget", ignoreCase = true) -> Pair(Color(0xFF4CAF50), Color.White)
                                tag.contains("Best Seller", ignoreCase = true) -> Pair(Color(0xFFFF6B35), Color.White)
                                tag.contains("Top Rated", ignoreCase = true) -> Pair(Color(0xFF667EEA), Color.White)
                                else -> Pair(Color(0xFF666666), Color.White)
                            }

                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = bgColor
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textColor,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
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
                    shape = RoundedCornerShape(4.dp),
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
                            Box(
                                modifier = Modifier
                                    .size(18.dp) // Increased size for better visibility
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
                                        .size(12.dp)
                                        .align(Alignment.Center)
                                )
                            }
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

                // Collapsible Highlights and Included sections
                if (!tour.highlights.isNullOrEmpty() || !tour.included.isNullOrEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF9F9F9)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showHighlights = !showHighlights }
                                    .padding(bottom = if (showHighlights) 6.dp else 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✨ Điểm nổi bật",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE65100),
                                    fontSize = 12.sp
                                )
                                Icon(
                                    imageVector = if (showHighlights) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                    contentDescription = if (showHighlights) "Hide highlights" else "Show highlights",
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // Highlights section header and content
                            if (showHighlights) {
                                if (!tour.highlights.isNullOrEmpty()) {   
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = Color(0xFFE0E0E0),
                                        modifier = Modifier.padding(vertical = 12.dp)
                                    )
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        tour.highlights.take(4).forEach { highlight ->
                                            HighlightItem(
                                                text = highlight
                                            )
                                        }
                                    }
                                }
                                if (!tour.included.isNullOrEmpty()) {
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = Color(0xFFE0E0E0),
                                        modifier = Modifier.padding(vertical = 12.dp)
                                    )
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        tour.included.take(6).forEach { item ->
                                            IncludedItem(
                                                text = item
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }

                // Tour features - Quick summary features
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .background(
                            color = Color(0xFF1E88E5), // Vibrant blue background
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(12.dp)
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Airline from expanded relation
                        tour.expandedFlightProvider?.let { airline ->
                            FeatureTag(
                                icon = Icons.Filled.Flight,
                                text = "Bay ${airline.name}"
                            )
                        }

                        // Duration
                        FeatureTag(
                            icon = Icons.Filled.Schedule,
                            text = tour.getDurationText()
                        )

                        // Group size
                        FeatureTag(
                            icon = Icons.Filled.People,
                            text = tour.getGroupSizeText()
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFF0F0F0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price and rating section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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

                Spacer(modifier = Modifier.height(8.dp))

                // Action buttons section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Xem chi tiết button
                    OutlinedButton(
                        onClick = onClick,
                        modifier = Modifier.weight(1f).height(36.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AppColors.Primary
                        ),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Chi tiết",
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Chi tiết",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Đặt tour button
                    Button(
                        onClick = { /* TODO: Handle booking */ },
                        modifier = Modifier.weight(1f).height(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935)
                        ),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Đặt tour",
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Đặt tour",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
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
            images = tour.allImages,
            onDismiss = { showImageViewer = false }
        )
    }
}

@Composable
private fun ImageViewerDialog(
    images: List<String>,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ảnh tour (${images.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Text(
                            text = "✕",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 18.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }

                // Images grid
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    images.forEach { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
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
        color = Color.White.copy(alpha = 0.9f) // Semi-transparent white background
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
                    tint = Color(0xFF1565C0), // Blue icon color
                    modifier = Modifier.size(10.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF0D47A1), // Dark blue text
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun HighlightItem(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFFFF3E0), // Light orange background
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF9800)) // Orange border
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE65100), // Dark orange text
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun IncludedItem(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFE3F2FD), // Light blue background
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2196F3)) // Blue border
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0D47A1), // Dark blue text
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
