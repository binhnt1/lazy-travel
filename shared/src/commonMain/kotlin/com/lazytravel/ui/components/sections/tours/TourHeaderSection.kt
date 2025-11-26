package com.lazytravel.ui.components.sections.tours

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.ui.theme.AppColors

@Composable
fun TourHeaderSection(
    onNavigateBack: () -> Unit,
    searchQuery: String = "",
    onSearchChange: (String) -> Unit = {},
    filterMinCost: String = "",
    onMinCostChange: (String) -> Unit = {},
    filterMaxCost: String = "",
    onMaxCostChange: (String) -> Unit = {},
    filterMonth: String = "",
    onMonthChange: (String) -> Unit = {},
    filterYear: String = "",
    onYearChange: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Main Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(8.dp)
                    )
                    .then(
                        Modifier.background(
                            Color(0xFFE0E0E0).copy(alpha = 0.3f),
                            RoundedCornerShape(8.dp)
                        )
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF212121),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Title
            Text(
                text = localizedString("tour_screen_title"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            // Create Tour Button
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1A73E8)
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = localizedString("tour_create"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        // Search Bar
        Spacer(modifier = Modifier.height(12.dp))

        SearchBarComponent(
            searchQuery = searchQuery,
            onSearchChange = onSearchChange,
            filterMinCost = filterMinCost,
            onMinCostChange = onMinCostChange,
            filterMaxCost = filterMaxCost,
            onMaxCostChange = onMaxCostChange,
            filterMonth = filterMonth,
            onMonthChange = onMonthChange,
            filterYear = filterYear,
            onYearChange = onYearChange
        )
    }
}

@Composable
private fun SearchBarComponent(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    filterMinCost: String,
    onMinCostChange: (String) -> Unit,
    filterMaxCost: String,
    onMaxCostChange: (String) -> Unit,
    filterMonth: String,
    onMonthChange: (String) -> Unit,
    filterYear: String,
    onYearChange: (String) -> Unit
) {
    var showAdvancedFilter by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = LocalizationManager.getString("tour_search_placeholder"),
                    fontSize = 16.sp,
                    color = AppColors.TextSecondary
                )
            },
            leadingIcon = {
                Text(
                    text = "🔍",
                    fontSize = 20.sp
                )
            },
            trailingIcon = {
                // Advanced Filter Icon - using a filter icon instead of arrow
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = AppColors.Primary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { showAdvancedFilter = !showAdvancedFilter }
                )
            },
            singleLine = true,
            shape = if (showAdvancedFilter) {
                RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            } else {
                RoundedCornerShape(8.dp)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.Primary,
                unfocusedBorderColor = AppColors.Border,
                cursorColor = AppColors.Primary
            )
        )

        // Advanced Filter Dropdown
        if (showAdvancedFilter) {
            AdvancedFilterDropdown(
                filterMinCost = filterMinCost,
                onMinCostChange = onMinCostChange,
                filterMaxCost = filterMaxCost,
                onMaxCostChange = onMaxCostChange,
                filterMonth = filterMonth,
                onMonthChange = onMonthChange,
                filterYear = filterYear,
                onYearChange = onYearChange,
                onDismiss = { showAdvancedFilter = false }
            )
        }
    }
}

@Composable
private fun AdvancedFilterDropdown(
    filterMinCost: String,
    onMinCostChange: (String) -> Unit,
    filterMaxCost: String,
    onMaxCostChange: (String) -> Unit,
    filterMonth: String,
    onMonthChange: (String) -> Unit,
    filterYear: String,
    onYearChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Store temporary filter values
    var tempMinCost by remember { mutableStateOf(filterMinCost) }
    var tempMaxCost by remember { mutableStateOf(filterMaxCost) }
    var tempMonth by remember { mutableStateOf(filterMonth) }
    var tempYear by remember { mutableStateOf(filterYear) }

    // ✅ FIX: Sync temp values when props change
    LaunchedEffect(filterMinCost, filterMaxCost, filterMonth, filterYear) {
        tempMinCost = filterMinCost
        tempMaxCost = filterMaxCost
        tempMonth = filterMonth
        tempYear = filterYear
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(
                width = 1.dp,
                color = AppColors.Border,
                shape = RoundedCornerShape(
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            )
            .clip(
                RoundedCornerShape(
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Text(
            text = LocalizationManager.getString("tour_advanced_filter_title"),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )

        // Cost Filter
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = LocalizationManager.getString("tour_filter_cost"),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Min Cost
                BasicTextField(
                    value = tempMinCost,
                    onValueChange = { tempMinCost = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(45.dp)
                        .border(1.dp, AppColors.Border, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 15.sp,
                        color = AppColors.TextPrimary
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (tempMinCost.isEmpty()) {
                                Text(
                                    text = LocalizationManager.getString("tour_filter_min_cost"),
                                    fontSize = 15.sp,
                                    color = AppColors.TextSecondary
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                // Max Cost
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(45.dp)
                        .border(
                            width = 1.dp,
                            color = AppColors.Border,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .background(Color.White, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = tempMaxCost,
                        onValueChange = { tempMaxCost = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 15.sp,
                            color = AppColors.TextPrimary
                        ),
                        cursorBrush = SolidColor(AppColors.Primary),
                        singleLine = true
                    )
                    if (tempMaxCost.isEmpty()) {
                        Text(
                            text = LocalizationManager.getString("tour_filter_max_cost"),
                            fontSize = 15.sp,
                            color = AppColors.TextSecondary
                        )
                    }
                }
            }
        }

        // Month/Year Filter
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Tháng/Năm khởi hành",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Month
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(45.dp)
                        .border(
                            width = 1.dp,
                            color = AppColors.Border,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .background(Color.White, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = tempMonth,
                        onValueChange = { tempMonth = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 15.sp,
                            color = AppColors.TextPrimary
                        ),
                        cursorBrush = SolidColor(AppColors.Primary),
                        singleLine = true
                    )
                    if (tempMonth.isEmpty()) {
                        Text(
                            text = "Tháng (1-12)",
                            fontSize = 15.sp,
                            color = AppColors.TextSecondary
                        )
                    }
                }

                // Year
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(45.dp)
                        .border(
                            width = 1.dp,
                            color = AppColors.Border,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .background(Color.White, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = tempYear,
                        onValueChange = { tempYear = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 15.sp,
                            color = AppColors.TextPrimary
                        ),
                        cursorBrush = SolidColor(AppColors.Primary),
                        singleLine = true
                    )
                    if (tempYear.isEmpty()) {
                        Text(
                            text = "Năm (2025, 2026...)",
                            fontSize = 15.sp,
                            color = AppColors.TextSecondary
                        )
                    }
                }
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Clear Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, AppColors.Border, RoundedCornerShape(6.dp))
                    .background(Color.White, RoundedCornerShape(6.dp))
                    .clickable {
                        tempMinCost = ""
                        tempMaxCost = ""
                        tempMonth = ""
                        tempYear = ""
                        onMinCostChange("")
                        onMaxCostChange("")
                        onMonthChange("")
                        onYearChange("")
                        onDismiss()
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = LocalizationManager.getString("tour_filter_clear"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.TextSecondary
                )
            }

            // Apply Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(AppColors.Primary, RoundedCornerShape(6.dp))
                    .clickable {
                        onMinCostChange(tempMinCost)
                        onMaxCostChange(tempMaxCost)
                        onMonthChange(tempMonth)
                        onYearChange(tempYear)
                        onDismiss()
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = LocalizationManager.getString("tour_filter_apply"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
