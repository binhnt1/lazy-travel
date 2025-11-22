package com.lazytravel.ui.components.sections.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    matchedCount: Int = 0,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    vertical = 12.dp,    // matches HTML padding: 12px 16px
                    horizontal = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceAround, // matches HTML justify-content: space-around
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Open trips stat
            StatItem(
                number = openTrips,
                label = localizedString("buddy_stats_open")
            )

            // This week stat
            StatItem(
                number = thisWeekTrips,
                label = localizedString("buddy_stats_this_week")
            )

            // Matched stat
            StatItem(
                number = matchedCount,
                label = localizedString("buddy_stats_matched")
            )
        }

        // Bottom border - matches HTML border-bottom: 1px solid #E0E0E0
        HorizontalDivider(
            color = Color(0xFFE0E0E0),
            thickness = 1.dp
        )
    }
}

/**
 * Individual stat item with number and label
 */
@Composable
private fun StatItem(
    number: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Number
        Text(
            text = number.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.Primary, // #FF6B35
            textAlign = TextAlign.Center
        )

        // Label
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}