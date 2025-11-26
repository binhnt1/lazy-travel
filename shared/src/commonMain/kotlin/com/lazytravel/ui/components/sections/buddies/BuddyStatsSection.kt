package com.lazytravel.ui.components.sections.buddies

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.core.i18n.localizedString

@Composable
fun BuddyStatsSection(
    openTrips: Int = 0,
    thisWeekTrips: Int = 0,
    completedCount: Int = 0,
    modifier: Modifier = Modifier,
    onStatClick: ((String) -> Unit)? = null
) {
    // Animation states for each stat
    val openTripsScale = remember { Animatable(0.8f) }
    val thisWeekTripsScale = remember { Animatable(0.8f) }
    val completedCountScale = remember { Animatable(0.8f) }
    
    // Animate on composition
    LaunchedEffect(openTrips, thisWeekTrips, completedCount) {
        openTripsScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
        thisWeekTripsScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
        completedCountScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    }
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Compact stats container with minimal height
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Open trips stat - compact design
            CompactStatItem(
                number = openTrips,
                label = localizedString("buddy_stats_open"),
                icon = Icons.Default.FlightTakeoff,
                iconColor = Color(0xFF4CAF50),
                scale = openTripsScale.value,
                onClick = { onStatClick?.invoke("open_trips") }
            )

            // This week stat - compact design
            CompactStatItem(
                number = thisWeekTrips,
                label = localizedString("buddy_stats_this_week"),
                icon = Icons.Default.DateRange,
                iconColor = Color(0xFF2196F3),
                scale = thisWeekTripsScale.value,
                onClick = { onStatClick?.invoke("this_week") }
            )

            // Completed stat - compact design (changed from matched to completed)
            CompactStatItem(
                number = completedCount,
                label = "Đã hoàn thành",
                icon = Icons.Default.CheckCircle,
                iconColor = Color(0xFFFF6B35),
                scale = completedCountScale.value,
                onClick = { onStatClick?.invoke("completed") }
            )
        }

        // Bottom border
        HorizontalDivider(
            color = Color(0xFFE0E0E0),
            thickness = 1.dp
        )
    }
}

/**
 * Stat item with icon - number on first line, title below
 */
@Composable
private fun CompactStatItem(
    number: Int,
    label: String,
    icon: ImageVector,
    iconColor: Color,
    scale: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .scale(scale)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon and number on first line
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(6.dp))
            
            // Large, clear number
            Text(
                text = if (number > 99) "99+" else number.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Title below
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}