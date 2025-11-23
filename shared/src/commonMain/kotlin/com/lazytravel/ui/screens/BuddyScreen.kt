package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lazytravel.ui.components.sections.buddies.BuddyFilterSection
import com.lazytravel.ui.components.sections.buddies.BuddyHeaderSection
import com.lazytravel.ui.components.sections.buddies.BuddyHotSection
import com.lazytravel.ui.components.sections.buddies.BuddyNormalSection
import com.lazytravel.ui.components.sections.buddies.BuddyLuxurySection
import com.lazytravel.ui.components.sections.buddies.BuddyStatsSection

@Composable
fun BuddyScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Measure header height dynamically
    var headerHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Scrollable content with top padding for sticky header
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = headerHeight,
                bottom = 80.dp
            )
        ) {
            // Stats Section - manages its own loading state
            item {
                BuddyStatsSection()
            }

            // Filter Section
            item {
                BuddyFilterSection()
            }

            // Content Sections - each manages its own loading state
            item {
                BuddyHotSection(onNavigateToDetail = onNavigateToDetail)
                BuddyLuxurySection(onNavigateToDetail = onNavigateToDetail)
                BuddyNormalSection(onNavigateToDetail = onNavigateToDetail)
            }
        }

        // Sticky Header on top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .zIndex(100f)
                .onGloballyPositioned { coordinates ->
                    headerHeight = with(density) { coordinates.size.height.toDp() }
                }
        ) {
            BuddyHeaderSection(onNavigateBack = onNavigateBack)
        }
    }
}