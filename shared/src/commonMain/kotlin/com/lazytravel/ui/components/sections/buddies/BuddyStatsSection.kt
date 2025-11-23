package com.lazytravel.ui.components.sections.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.data.repositories.BuddyRepository
import com.lazytravel.data.repositories.BuddyStats
import kotlinx.coroutines.launch

@Composable
fun BuddyStatsSection(
    modifier: Modifier = Modifier
) {
    // Internal state management
    val scope = rememberCoroutineScope()
    val repository = remember { BuddyRepository() }
    var stats by remember { mutableStateOf(BuddyStats(0, 0, 0)) }
    var isLoading by remember { mutableStateOf(true) }

    // Load stats on first composition
    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            try {
                val result = repository.getBuddyStats()
                result.fold(
                    onSuccess = { fetchedStats ->
                        stats = fetchedStats
                        isLoading = false
                    },
                    onFailure = {
                        isLoading = false
                    }
                )
            } catch (_: Exception) {
                isLoading = false
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            // Loading state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.White)
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = AppColors.Primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        } else {
            // Stats content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    ),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Open trips stat
                StatItem(
                    number = stats.openTrips,
                    label = localizedString("buddy_stats_open")
                )

                // This week stat
                StatItem(
                    number = stats.thisWeekTrips,
                    label = localizedString("buddy_stats_this_week")
                )

                // Matched stat
                StatItem(
                    number = stats.matchedCount,
                    label = localizedString("buddy_stats_matched")
                )
            }
        }

        // Bottom border
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