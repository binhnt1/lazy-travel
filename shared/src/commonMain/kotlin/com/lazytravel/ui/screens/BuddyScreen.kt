package com.lazytravel.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazytravel.data.repositories.BuddyRepository
import com.lazytravel.data.repositories.BuddyStats
import com.lazytravel.ui.components.sections.buddies.BuddyFilterSection
import com.lazytravel.ui.components.sections.buddies.BuddyHeaderSection
import com.lazytravel.ui.components.sections.buddies.BuddyHotSection
import com.lazytravel.ui.components.sections.buddies.BuddyNormalSection
import com.lazytravel.ui.components.sections.buddies.BuddyLuxurySection
import com.lazytravel.ui.components.sections.buddies.BuddyStatsSection
import kotlinx.coroutines.launch

@Composable
fun BuddyScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val repository = remember { BuddyRepository() }
    var stats by remember { mutableStateOf(BuddyStats(0, 0, 0)) }
    var isLoading by remember { mutableStateOf(true) }

    // Load stats only
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = repository.getBuddyStats()
                result.fold(
                    onSuccess = { fetchedStats ->
                        stats = fetchedStats
                        isLoading = false
                    },
                    onFailure = { error ->
                        isLoading = false
                    }
                )
            } catch (_: Exception) {
                isLoading = false
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Header Section
        item {
            BuddyHeaderSection(onNavigateBack = onNavigateBack)
        }

        // Stats Section
        item {
            BuddyStatsSection(
                openTrips = stats.openTrips,
                thisWeekTrips = stats.thisWeekTrips,
                matchedCount = stats.matchedCount
            )
        }

        // Filter Section
        item {
            BuddyFilterSection()
        }

        // Hot Section
        item {
            BuddyHotSection(onNavigateToDetail = onNavigateToDetail)
            BuddyLuxurySection(onNavigateToDetail = onNavigateToDetail)
            BuddyNormalSection(onNavigateToDetail = onNavigateToDetail)
        }
    }
}