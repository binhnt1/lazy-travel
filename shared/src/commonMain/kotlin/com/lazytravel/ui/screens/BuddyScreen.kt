package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import kotlinx.coroutines.launch

@Composable
fun BuddyScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Measure header height dynamically
    var headerHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    // ✅ Hoist filter state
    var selectedTab by remember { mutableStateOf("all") }
    var selectedFilters by remember { mutableStateOf(setOf<String>()) }
    var selectedSort by remember { mutableStateOf("recent") }
    var searchQuery by remember { mutableStateOf("") }

    // Advanced filter state
    var filterMinCost by remember { mutableStateOf("") }
    var filterMaxCost by remember { mutableStateOf("") }
    var filterMonth by remember { mutableStateOf("") }
    var filterYear by remember { mutableStateOf("") }

    // LazyListState for scrolling
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Track when filter changes to scroll to BuddyNormalSection
    LaunchedEffect(selectedTab, selectedFilters, selectedSort, searchQuery, filterMinCost, filterMaxCost, filterMonth, filterYear) {
        // Only scroll if user has interacted with filters (not on first load)
        if (selectedTab != "all" || selectedFilters.isNotEmpty() || selectedSort != "recent" ||
            searchQuery.isNotEmpty() || filterMinCost.isNotEmpty() ||
            filterMaxCost.isNotEmpty() || filterMonth.isNotEmpty() || filterYear.isNotEmpty()) {
            // Scroll to item index 3 (Stats=0, Filter=1, Content=2, BuddyNormal inside=3)
            coroutineScope.launch {
                listState.animateScrollToItem(2) // Scroll to content section
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Scrollable content with top padding for sticky header
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = headerHeight,
                bottom = 80.dp
            )
        ) {
            // Stats Section - manages its own loading state
            item(key = "stats") {
                BuddyStatsSection()
            }

            // Filter Section
            item(key = "filter") {
                BuddyFilterSection(
                    selectedTab = selectedTab,
                    onTabChange = { selectedTab = it },
                    selectedFilters = selectedFilters,
                    onFiltersChange = { selectedFilters = it },
                    selectedSort = selectedSort,
                    onSortChange = { selectedSort = it }
                )
            }

            // Content Sections - each manages its own loading state
            item(key = "content") {
                BuddyHotSection(onNavigateToDetail = onNavigateToDetail)
                BuddyLuxurySection(onNavigateToDetail = onNavigateToDetail)
                BuddyNormalSection(
                    filterTab = selectedTab,
                    filterInterests = selectedFilters,
                    sortBy = selectedSort,
                    searchQuery = searchQuery,
                    filterMinCost = filterMinCost,
                    filterMaxCost = filterMaxCost,
                    filterMonth = filterMonth,
                    filterYear = filterYear,
                    onNavigateToDetail = onNavigateToDetail
                )
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
            BuddyHeaderSection(
                onNavigateBack = onNavigateBack,
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                filterMinCost = filterMinCost,
                onMinCostChange = { filterMinCost = it },
                filterMaxCost = filterMaxCost,
                onMaxCostChange = { filterMaxCost = it },
                filterMonth = filterMonth,
                onMonthChange = { filterMonth = it },
                filterYear = filterYear,
                onYearChange = { filterYear = it }
            )
        }
    }
}