package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.lazytravel.ui.components.sections.tours.TourFilterSection
import com.lazytravel.ui.components.sections.tours.TourHeaderSection
import com.lazytravel.ui.components.sections.tours.TourHotSection
import com.lazytravel.ui.components.sections.tours.TourNormalSection
import com.lazytravel.ui.components.sections.tours.TourLuxurySection

@Composable
fun TourScreen(
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

    // Determine if filters are active (excluding sort to keep Featured/Luxury visible)
    val hasActiveFilters = selectedTab != "all" || selectedFilters.isNotEmpty() ||
        searchQuery.isNotEmpty() ||
        filterMinCost.isNotEmpty() || filterMaxCost.isNotEmpty() ||
        filterMonth.isNotEmpty() || filterYear.isNotEmpty()

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
            // Filter Section
            item(key = "filter") {
                TourFilterSection(
                    selectedTab = selectedTab,
                    onTabChange = { selectedTab = it },
                    selectedFilters = selectedFilters,
                    onFiltersChange = { selectedFilters = it }
                )
            }

            // Content Sections - each manages its own loading state
            item(key = "content") {
                // Hide Featured and Luxury sections when filters are active
                if (!hasActiveFilters) {
                    TourHotSection(
                        onTourClick = { onNavigateToDetail("placeholder") },
                        onViewAllClick = { }
                    )
//                    TourLuxurySection(
//                        onTourClick = { onNavigateToDetail("placeholder") },
//                        onViewAllClick = { }
//                    )
                }

//                TourNormalSection(
//                    filterTab = selectedTab,
//                    filterInterests = selectedFilters,
//                    sortBy = selectedSort,
//                    searchQuery = searchQuery,
//                    filterMinCost = filterMinCost,
//                    filterMaxCost = filterMaxCost,
//                    filterMonth = filterMonth,
//                    filterYear = filterYear,
//                    onNavigateToDetail = onNavigateToDetail,
//                    onSortChange = { selectedSort = it }
//                )
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
            TourHeaderSection(
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
