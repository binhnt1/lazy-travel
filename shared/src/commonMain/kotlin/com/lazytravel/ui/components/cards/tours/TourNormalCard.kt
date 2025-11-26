package com.lazytravel.ui.components.cards.tours

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import com.lazytravel.helpers.formatDateFromTimestamp
import com.lazytravel.ui.theme.AppColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TourNormalCard(
    tour: Tour,
    onClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    onViewDetailsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) Color(0xFFF8F9FA) else Color.White,
        animationSpec = tween(durationMillis = 150),
        label = "backgroundColor"
    )
    
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 8f else 2f,
        animationSpec = tween(durationMillis = 150),
        label = "elevation"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Image + Main Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Image Section with Enhanced Design
                Box(
                    modifier = Modifier
                        .size(140.dp, 120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFF5F5F5),
                                    Color(0xFFE8E8E8)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(1f, 1f)
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    val bgImage = tour.images?.firstOrNull() ?: ""
                    if (bgImage.isNotEmpty()) {
                        AsyncImage(
                            model = bgImage,
                            contentDescription = tour.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        
                        // Gradient Overlay for better text visibility
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.3f)
                                        ),
                                        startY = 80f
                                    )
                                )
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = tour.emoji,
                                    fontSize = 32.sp
                                )
                                Text(
                                    text = "No Image",
                                    color = Color(0xFF999999),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    
                    // Discount Badge
                    if (tour.discount > 0) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(8.dp),
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFF4757),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                        ) {
                            Text(
                                text = "-${tour.discount}%",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                    
                    // Favorite Button - Same as TourHotCard
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
                    
                    // Booking Status Indicator
                    if (tour.availableSlots <= 3 && tour.availableSlots > 0) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFFF6B35),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                        ) {
                            Text(
                                text = "Sắp hết",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                
                // Main Content Section
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Company and Rating Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Company Info
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
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
                                    color = Color(0xFF666666),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                
                                if (provider.isVerified) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Verified",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            } ?: Text(
                                text = "Công ty du lịch",
                                color = Color(0xFF666666),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        // Rating
                        if (tour.rating > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "%.1f".format(tour.rating),
                                    color = Color(0xFF333333),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (tour.reviewCount > 0) {
                                    Text(
                                        text = "(${tour.reviewCount})",
                                        color = Color(0xFF999999),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                    
                    // Tour Title
                    Text(
                        text = tour.name,
                        color = Color(0xFF2C3E50),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                    
                    // Key Info Tags Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoTag(
                            icon = Icons.Filled.Schedule,
                            text = tour.getDurationText(),
                            backgroundColor = Color(0xFFE3F2FD),
                            iconColor = Color(0xFF1976D2)
                        )
                        
                        if (tour.startDate > 0) {
                            InfoTag(
                                icon = Icons.Filled.CalendarToday,
                                text = formatDateFromTimestamp(tour.startDate),
                                backgroundColor = Color(0xFFF3E5F5),
                                iconColor = Color(0xFF7B1FA2)
                            )
                        }
                        
                        InfoTag(
                            icon = Icons.Filled.LocationOn,
                            text = tour.expandedCity?.name ?: "Điểm đến",
                            backgroundColor = Color(0xFFFFF3E0),
                            iconColor = Color(0xFFF57C00)
                        )
                    }
                    
                    // Availability Info
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (tour.availableSlots > 0) {
                            InfoTag(
                                icon = Icons.Filled.People,
                                text = "Còn ${tour.availableSlots} chỗ",
                                backgroundColor = Color(0xFFE8F5E8),
                                iconColor = Color(0xFF388E3C)
                            )
                        }
                        
                        if (tour.bookedCount > 0) {
                            InfoTag(
                                icon = Icons.Filled.TrendingUp,
                                text = "${tour.bookedCount} đã đặt",
                                backgroundColor = Color(0xFFFFF8E1),
                                iconColor = Color(0xFFFFA000)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Description Section
            if (tour.description.isNotEmpty()) {
                Text(
                    text = tour.description,
                    color = Color(0xFF5A6C7D),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            
            // Highlights Section
            if (!tour.highlights.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "✨ Điểm nổi bật",
                        color = Color(0xFF2C3E50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        tour.highlights.take(4).forEach { highlight ->
                            HighlightChip(text = highlight)
                        }
                    }
                }
            }
            
            // Included Services Section
            if (!tour.included.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "🎁 Dịch vụ bao gồm",
                        color = Color(0xFF2C3E50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        tour.included.take(4).forEach { item ->
                            IncludedChip(text = item)
                        }
                    }
                }
            }
            
            // Footer: Price and Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price Section
                Column {
                    if (tour.originalPrice > 0 && tour.discount > 0) {
                        Text(
                            text = tour.getFormattedOriginalPrice(),
                            color = Color(0xFF999999),
                            fontSize = 11.sp,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = tour.getFormattedPrice(),
                            color = Color(0xFFE74C3C),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "/${localizedString("per_person")}",
                            color = Color(0xFF999999),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
                
                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // View Details Button
                    OutlinedButton(
                        onClick = onViewDetailsClick,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AppColors.Primary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.Primary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = null,
                                tint = AppColors.Primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Chi tiết",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    
                    // Book Now Button
                    Button(
                        onClick = { /* TODO: Handle booking */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Primary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Đặt tour",
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

@Composable
private fun InfoTag(
    icon: ImageVector,
    text: String,
    backgroundColor: Color,
    iconColor: Color
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, backgroundColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(10.dp)
            )
            Text(
                text = text,
                color = iconColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun HighlightChip(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF3E0),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCC80))
    ) {
        Text(
            text = text,
            color = Color(0xFFE65100),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun IncludedChip(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFE8F5E8),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA5D6A7))
    ) {
        Text(
            text = text,
            color = Color(0xFF2E7D32),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
