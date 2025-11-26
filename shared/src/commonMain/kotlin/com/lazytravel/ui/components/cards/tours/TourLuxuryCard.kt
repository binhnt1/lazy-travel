package com.lazytravel.ui.components.cards.tours

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ExpandLess

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
    val luxuryBlue = Color(0xFF08104D)
    val luxuryLightBlue = Color(0xFF1F2D6B)
    
    var showImageViewer by remember { mutableStateOf(false) }
    var showHighlights by remember { mutableStateOf(false) }
    var showIncluded by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier
            .width(280.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        luxuryBlue,
                        Color(0xFF050A30)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = goldColor,
                shape = RoundedCornerShape(8.dp)
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
                    .background(luxuryLightBlue)
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
                                        if (pagerState.currentPage == index) goldColor
                                        else goldColor.copy(alpha = 0.5f)
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
                                        color = parseHexColor(tour.thumbnailColor) ?: luxuryLightBlue,
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
                            color = Color(0xFFA0B0D0),
                            fontSize = 11.sp
                        )

                        if (provider.isVerified) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Verified",
                                tint = goldColor,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                        
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
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Collapsible Highlights and Included sections with luxury styling
                if (!tour.highlights.isNullOrEmpty() || !tour.included.isNullOrEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = luxuryLightBlue.copy(alpha = 0.3f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, goldColor.copy(alpha = 0.5f))
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
                                    color = Color(0xFFE65100), // Orange color like TourHotCard
                                    fontSize = 12.sp
                                )
                                Icon(
                                    imageVector = if (showHighlights) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                    contentDescription = if (showHighlights) "Hide highlights" else "Show highlights",
                                    tint = Color(0xFFE65100), // Orange color like TourHotCard
                                    modifier = Modifier.size(16.dp)
                                )
                            }
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
                                }
                            }
                        }
                    }
                }

                // Tour features - Quick summary features with luxury styling
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .background(
                            color = goldColor.copy(alpha = 0.2f), // Gold background
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(1.dp, goldColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Enhanced Airline information for luxury tours
                        tour.expandedFlightProvider?.let { airline ->
                            // Check for business/first class in included items
                            val flightClass = tour.included?.find {
                                it.contains("Business", ignoreCase = true) ||
                                it.contains("First Class", ignoreCase = true) ||
                                it.contains("Thương gia", ignoreCase = true)
                            }?.let { str ->
                                when {
                                    str.contains("First Class", ignoreCase = true) -> "First Class"
                                    str.contains("Business", ignoreCase = true) -> "Business Class"
                                    str.contains("Thương gia", ignoreCase = true) -> "Thương gia"
                                    else -> airline.name
                                }
                            } ?: airline.name
                            
                            LuxuryFeatureTag(
                                icon = Icons.Filled.Flight,
                                text = "Bay $flightClass"
                            )
                        }

                        // Duration
                        LuxuryFeatureTag(
                            icon = Icons.Filled.Schedule,
                            text = tour.getDurationText()
                        )

                        // Group size
                        LuxuryFeatureTag(
                            icon = Icons.Filled.People,
                            text = tour.getGroupSizeText()
                        )
                        
                        // Additional luxury flight amenities if available
                        tour.included?.forEach { item ->
                            when {
                                item.contains("VIP", ignoreCase = true) || item.contains("lounge", ignoreCase = true) -> {
                                    LuxuryFeatureTag(
                                        emoji = "🛋",
                                        text = "VIP Lounge"
                                    )
                                }
                                item.contains("fast track", ignoreCase = true) || item.contains("priority", ignoreCase = true) -> {
                                    LuxuryFeatureTag(
                                        emoji = "⚡",
                                        text = "Fast Track"
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = goldColor.copy(alpha = 0.3f)
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
                                color = Color(0xFFA0B0D0),
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = tour.getFormattedPrice(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = goldColor,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "/ ${localizedString("per_person")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFA0B0D0),
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
                                    color = if (index < tour.rating.toInt()) goldColor else Color(0xFFA0B0D0),
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Text(
                            text = "${tour.rating} (${tour.reviewCount} đánh giá)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFA0B0D0),
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action buttons section with luxury styling
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Xem chi tiết button
                    OutlinedButton(
                        onClick = onClick,
                        modifier = Modifier.weight(1f).height(36.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = goldColor
                        ),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, goldColor),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Chi tiết",
                                modifier = Modifier.size(16.dp),
                                tint = goldColor
                            )
                            Text(
                                text = "Chi tiết",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = goldColor
                            )
                        }
                    }

                    // Đặt tour button with luxury styling
                    Button(
                        onClick = { /* TODO: Handle booking */ },
                        modifier = Modifier.weight(1f).height(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(goldColor, Color(0xFFF4E4A6))
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ShoppingCart,
                                    contentDescription = "Đặt tour",
                                    modifier = Modifier.size(16.dp),
                                    tint = darkGold
                                )
                                Text(
                                    text = "Đặt tour",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = darkGold
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
private fun LuxuryFeatureTag(
    icon: ImageVector? = null,
    text: String,
    emoji: String? = null
) {
    val goldColor = Color(0xFFD4AF37)
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Color.White.copy(alpha = 0.1f) // Semi-transparent white background
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
                    tint = goldColor, // Gold icon color
                    modifier = Modifier.size(10.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = goldColor, // Gold text color
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
