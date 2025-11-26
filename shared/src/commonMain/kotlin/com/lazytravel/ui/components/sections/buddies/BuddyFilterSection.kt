package com.lazytravel.ui.components.sections.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.ui.theme.AppColors

@Composable
fun BuddyFilterSection(
    selectedTab: String,
    onTabChange: (String) -> Unit,
    selectedFilters: Set<String>,
    onFiltersChange: (Set<String>) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Filter Tabs
        FilterTabsRow(
            selectedTab = selectedTab,
            onTabChange = onTabChange
        )

        // Quick Filters Grid Section
        QuickFiltersSection(
            selectedFilters = selectedFilters,
            onFiltersChange = onFiltersChange
        )
    }
}

@Composable
private fun FilterTabsRow(
    selectedTab: String,
    onTabChange: (String) -> Unit
) {
    val tabs = listOf(
        "all" to LocalizationManager.getString("buddy_filter_all"),
        "domestic" to LocalizationManager.getString("buddy_filter_domestic"),
        "international" to LocalizationManager.getString("buddy_filter_international"),
        "weekend" to LocalizationManager.getString("buddy_filter_weekend"),
        "long_trip" to LocalizationManager.getString("buddy_filter_long_trip")
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEachIndexed { _, pair ->
            val (tabId, tabLabel) = pair
            FilterTab(
                label = tabLabel,
                isSelected = selectedTab == tabId,
                onClick = { onTabChange(tabId) }
            )
        }
    }

    // Bottom border for filter tabs
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(AppColors.Border)
    )
}

@Composable
private fun FilterTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        modifier = Modifier
            .border(
                1.dp,
                if (isSelected) AppColors.Primary else AppColors.Border,
                RoundedCornerShape(8.dp)
            )
            .background(
                if (isSelected) AppColors.Primary else Color.White,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = if (isSelected) Color.White else AppColors.TextSecondary
    )
}

@Composable
private fun QuickFiltersSection(
    selectedFilters: Set<String>,
    onFiltersChange: (Set<String>) -> Unit
) {
    val filters = listOf(
        "beach" to "🏖️",
        "mountain" to "⛰️",
        "city" to "🏙️",
        "backpacker" to "🎒",
        "photo" to "📸",
        "food" to "🍜",
        "camping" to "⛺",
        "cultural" to "🎨"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .border(1.dp, AppColors.Border, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = LocalizationManager.getString("buddy_filter_interests"),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Grid layout with 4 columns
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // First row: 4 items
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.take(4).forEachIndexed { _, pair ->
                    val (filterId, emoji) = pair
                    QuickFilterItem(
                        filterId = filterId,
                        emoji = emoji,
                        isSelected = selectedFilters.contains(filterId),
                        onClick = {
                            val newFilters = selectedFilters.toMutableSet()
                            if (newFilters.contains(filterId)) {
                                newFilters.remove(filterId)
                            } else {
                                newFilters.add(filterId)
                            }
                            onFiltersChange(newFilters)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Second row: 4 items
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.drop(4).take(4).forEachIndexed { _, pair ->
                    val (filterId, emoji) = pair
                    QuickFilterItem(
                        filterId = filterId,
                        emoji = emoji,
                        isSelected = selectedFilters.contains(filterId),
                        onClick = {
                            val newFilters = selectedFilters.toMutableSet()
                            if (newFilters.contains(filterId)) {
                                newFilters.remove(filterId)
                            } else {
                                newFilters.add(filterId)
                            }
                            onFiltersChange(newFilters)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickFilterItem(
    filterId: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(
                1.dp,
                if (isSelected) AppColors.Primary else AppColors.Border,
                RoundedCornerShape(8.dp)
            )
            .background(
                if (isSelected) Color(0xFFFFF3EE) else Color.White,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = LocalizationManager.getString("buddy_filter_$filterId"),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) AppColors.Primary else AppColors.TextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
fun BottomSortBar(
    selectedSort: String,
    onSortChange: (String) -> Unit
) {
    var showSortDropdown by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .border(1.dp, AppColors.Border, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = LocalizationManager.getString("buddy_sort_by"),
            fontSize = 14.sp,
            color = AppColors.TextSecondary
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box {
            Text(
                text = when (selectedSort) {
                    "recent" -> LocalizationManager.getString("buddy_sort_recent")
                    "matched" -> LocalizationManager.getString("buddy_sort_matched")
                    "popular" -> LocalizationManager.getString("buddy_sort_popular")
                    else -> LocalizationManager.getString("buddy_sort_recent")
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary,
                modifier = Modifier
                    .border(1.dp, AppColors.Border, RoundedCornerShape(6.dp))
                    .background(Color.White, RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .clickable { showSortDropdown = !showSortDropdown }
            )
            
            // Dropdown menu
            if (showSortDropdown) {
                Popup(
                    onDismissRequest = { showSortDropdown = false },
                    properties = PopupProperties(focusable = true)
                ) {
                    Column(
                        modifier = Modifier
                            .border(1.dp, AppColors.Border, RoundedCornerShape(6.dp))
                            .background(Color.White, RoundedCornerShape(6.dp))
                            .padding(2.dp)
                            .widthIn(min = 120.dp)
                    ) {
                        SortOption(
                            text = LocalizationManager.getString("buddy_sort_recent"),
                            isSelected = selectedSort == "recent",
                            onClick = {
                                onSortChange("recent")
                                showSortDropdown = false
                            }
                        )
                        
                        SortOption(
                            text = LocalizationManager.getString("buddy_sort_matched"),
                            isSelected = selectedSort == "matched",
                            onClick = {
                                onSortChange("matched")
                                showSortDropdown = false
                            }
                        )
                        
                        SortOption(
                            text = LocalizationManager.getString("buddy_sort_popular"),
                            isSelected = selectedSort == "popular",
                            onClick = {
                                onSortChange("popular")
                                showSortDropdown = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SortOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        color = if (isSelected) AppColors.Primary else AppColors.TextPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    )
}
