package com.lazytravel.ui.components.sections.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.ui.theme.AppColors

@Composable
fun BuddyFilterSection() {
    var selectedTab by remember { mutableStateOf("all") }
    var selectedFilters by remember { mutableStateOf(setOf<String>()) }
    var selectedSort by remember { mutableStateOf("recent") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Filter Tabs
        FilterTabsRow(
            selectedTab = selectedTab,
            onTabChange = { selectedTab = it }
        )

        // Quick Filters Grid Section
        QuickFiltersSection(
            selectedFilters = selectedFilters,
            onFiltersChange = { selectedFilters = it }
        )

        // Sort & Filter Bar
        SortFilterBar(
            selectedSort = selectedSort,
            onSortChange = { selectedSort = it }
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
        fontSize = 13.sp,
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
            fontSize = 15.sp,
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
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = LocalizationManager.getString("buddy_filter_$filterId"),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) AppColors.Primary else AppColors.TextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun SortFilterBar(
    selectedSort: String,
    onSortChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp, 0.dp, 16.dp, 16.dp)
            .border(1.dp, AppColors.Border, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LocalizationManager.getString("buddy_sort_by"),
                fontSize = 12.sp,
                color = AppColors.TextSecondary
            )

            Text(
                text = when (selectedSort) {
                    "recent" -> LocalizationManager.getString("buddy_sort_recent")
                    "matched" -> LocalizationManager.getString("buddy_sort_matched")
                    "popular" -> LocalizationManager.getString("buddy_sort_popular")
                    else -> LocalizationManager.getString("buddy_sort_recent")
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary,
                modifier = Modifier
                    .border(1.dp, AppColors.Border, RoundedCornerShape(6.dp))
                    .background(Color.White, RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .clickable {
                        val sorts = listOf("recent", "matched", "popular")
                        val nextSort = sorts[(sorts.indexOf(selectedSort) + 1) % sorts.size]
                        onSortChange(nextSort)
                    }
            )
        }

        Text(
            text = "Kết quả: 45",
            fontSize = 12.sp,
            color = AppColors.TextSecondary
        )
    }
}

