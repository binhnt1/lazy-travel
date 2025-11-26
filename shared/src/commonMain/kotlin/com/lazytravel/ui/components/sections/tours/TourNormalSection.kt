package com.lazytravel.ui.components.sections.tours

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.models.Tour
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.ui.components.cards.tours.TourNormalCard
import com.lazytravel.ui.components.atoms.LoadingIndicator
import kotlinx.coroutines.launch

@Composable
fun TourNormalSection(
    filterTab: String = "all",
    filterInterests: Set<String> = emptySet(),
    sortBy: String = "recent",
    searchQuery: String = "",
    filterMinCost: String = "",
    filterMaxCost: String = "",
    filterMonth: String = "",
    filterYear: String = "",
    onNavigateToDetail: (String) -> Unit = {},
    onSortChange: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val tourRepo = remember { BaseRepository<Tour>() }
    var isLoading by remember { mutableStateOf(true) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var tours by remember { mutableStateOf<List<Tour>>(emptyList()) }
    var currentPage by remember { mutableStateOf(1) }
    var hasMorePages by remember { mutableStateOf(true) }
    val perPage = 10 // Items per page

    // Function to load tours with pagination
    fun loadTours(page: Int, append: Boolean = false) {
        if (append) {
            isLoadingMore = true
        } else {
            isLoading = true
        }

        scope.launch {
            // Build filter query based on selected filters
            val baseFilter = "status='AVAILABLE' && tags !~ '🔥 HOT' && tags !~ '✨ LUXURY'"

            // Add tab filter
            val tabFilter = when (filterTab) {
                "domestic" -> " && cityId.countryId.name='Vietnam'"
                "international" -> " && cityId.countryId.name!='Vietnam'"
                "weekend" -> " && duration<=3"
                "long_trip" -> " && duration>7"
                else -> "" // "all"
            }

            // Add interest filters
            val interestFilter = if (filterInterests.isNotEmpty()) {
                filterInterests.joinToString(" || ") { interest ->
                    when (interest) {
                        "beach" -> "tags ~ 'Biển'"
                        "mountain" -> "tags ~ 'Núi'"
                        "city" -> "tags ~ 'Thành phố'"
                        "adventure" -> "tags ~ 'Phiêu lưu'"
                        "cultural" -> "tags ~ 'Văn hóa'"
                        "food" -> "tags ~ 'Ẩm thực'"
                        "shopping" -> "tags ~ 'Mua sắm'"
                        "relaxation" -> "tags ~ 'Nghỉ dưỡng'"
                        else -> ""
                    }
                }.let { " && ($it)" }
            } else ""

            // Add search filter
            val searchFilter = if (searchQuery.isNotEmpty()) {
                " && (tourName ~ '$searchQuery' || description ~ '$searchQuery' || destination ~ '$searchQuery' || tags ~ '$searchQuery' || cityId.name ~ '$searchQuery' || cityId.countryId.name ~ '$searchQuery')"
            } else ""

            // Add cost filter (using price in VND)
            val costFilter = buildString {
                if (filterMinCost.isNotEmpty() || filterMaxCost.isNotEmpty()) {
                    append(" && (")
                    val filters = mutableListOf<String>()
                    if (filterMinCost.isNotEmpty()) {
                        filterMinCost.toDoubleOrNull()?.let {
                            val minPriceVND = (it * 1_000_000).toLong()
                            filters.add("price>=$minPriceVND")
                        }
                    }
                    if (filterMaxCost.isNotEmpty()) {
                        filterMaxCost.toDoubleOrNull()?.let {
                            val maxPriceVND = (it * 1_000_000).toLong()
                            filters.add("price<=$maxPriceVND")
                        }
                    }
                    append(filters.joinToString(" && "))
                    append(")")
                }
            }

            // Add month/year filter
            val dateFilter = buildString {
                if (filterMonth.isNotEmpty() || filterYear.isNotEmpty()) {
                    val year = filterYear.toIntOrNull() ?: 2026
                    if (filterMonth.isNotEmpty()) {
                        val month = filterMonth.toIntOrNull() ?: 1
                        // Calculate start and end timestamps for the month
                        val startOfMonth = getTimestampForDate(year, month, 1, 0, 0, 0)
                        val endOfMonth = if (month == 12) {
                            getTimestampForDate(year + 1, 1, 1, 0, 0, 0) - 1
                        } else {
                            getTimestampForDate(year, month + 1, 1, 0, 0, 0) - 1
                        }
                        append(" && startDate>=$startOfMonth && startDate<=$endOfMonth")
                    } else {
                        // Only year filter
                        val startOfYear = getTimestampForDate(year, 1, 1, 0, 0, 0)
                        val endOfYear = getTimestampForDate(year + 1, 1, 1, 0, 0, 0) - 1
                        append(" && startDate>=$startOfYear && startDate<=$endOfYear")
                    }
                }
            }

            val finalFilter = baseFilter + tabFilter + interestFilter + searchFilter + costFilter + dateFilter

            // Determine sort order
            val sortOrder = when (sortBy) {
                "popular" -> "-viewCount,-created"
                "price_low" -> "price,created"
                "price_high" -> "-price,created"
                else -> "-created" // "recent"
            }

            tourRepo.getRecords<Tour>(
                page = page,
                perPage = perPage,
                sort = sortOrder,
                expand = "tourProviderId,cityId,cityId.countryId,placeId,airlineId",
                filter = finalFilter,
            ).fold(
                onSuccess = { fetchedTours ->
                    fetchedTours.forEach { tour ->
                        tour.populateExpandedData()
                    }

                    tours = if (append) {
                        // Append for pagination
                        tours + fetchedTours
                    } else {
                        // Replace for new search/filter
                        fetchedTours
                    }

                    // Check if there are more pages
                    hasMorePages = fetchedTours.size >= perPage

                    isLoading = false
                    isLoadingMore = false
                },
                onFailure = { exception ->
                    exception.printStackTrace()
                    isLoading = false
                    isLoadingMore = false
                }
            )
        }
    }

    // ✅ Reload data when filter changes (reset pagination)
    LaunchedEffect(filterTab, filterInterests, sortBy, searchQuery, filterMinCost, filterMaxCost, filterMonth, filterYear) {
        currentPage = 1
        hasMorePages = true
        loadTours(page = 1, append = false)
    }

    // Normal section with white background (section-box style)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Section header with sort bar aligned with title
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = LocalizationManager.getString("tour_normal_section_title"),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )

                        // Show filter status
                        if (filterTab != "all" || filterInterests.isNotEmpty() ||
                            searchQuery.isNotEmpty() || filterMinCost.isNotEmpty() ||
                            filterMaxCost.isNotEmpty() || filterMonth.isNotEmpty() || filterYear.isNotEmpty()) {
                            Text(
                                text = if (searchQuery.isNotEmpty()) {
                                    LocalizationManager.getString("tour_search_results")
                                        .replace("{query}", searchQuery)
                                        .replace("{count}", tours.size.toString())
                                } else {
                                    LocalizationManager.getString("tour_filter_results")
                                        .replace("{count}", tours.size.toString())
                                },
                                fontSize = 13.sp,
                                color = Color(0xFF1A73E8),
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    // Sort bar aligned with title (same baseline)
                    InternalSortBar(
                        selectedSort = sortBy,
                        onSortChange = onSortChange
                    )
                }
            }

            // Normal tours vertical list
            Box(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingIndicator()
                        }
                    }
                    tours.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = LocalizationManager.getString("tour_empty_state"),
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                    else -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            tours.forEachIndexed { index, tour ->
                                TourNormalCard(
                                    tour = tour,
                                    onClick = { onNavigateToDetail(tour.id) }
                                )

                                // Auto-load more when near end
                                if (index == tours.size - 3 && hasMorePages && !isLoadingMore) {
                                    LaunchedEffect(Unit) {
                                        currentPage++
                                        loadTours(page = currentPage, append = true)
                                    }
                                }
                            }

                            // Auto-pagination loading indicator
                            if (isLoadingMore) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LoadingIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InternalSortBar(
    selectedSort: String,
    onSortChange: (String) -> Unit
) {
    var showSortDropdown by remember { mutableStateOf(false) }
    
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = LocalizationManager.getString("tour_sort_by"),
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box {
            Text(
                text = when (selectedSort) {
                    "recent" -> LocalizationManager.getString("tour_sort_recent")
                    "popular" -> LocalizationManager.getString("tour_sort_popular")
                    "price_low" -> LocalizationManager.getString("tour_sort_price_low")
                    "price_high" -> LocalizationManager.getString("tour_sort_price_high")
                    else -> LocalizationManager.getString("tour_sort_recent")
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF212121),
                modifier = Modifier
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                    .background(Color.White, RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .clickable { showSortDropdown = !showSortDropdown }
            )
            
            // Dropdown menu with smaller width
            if (showSortDropdown) {
                androidx.compose.ui.window.Popup(
                    onDismissRequest = { showSortDropdown = false },
                    properties = androidx.compose.ui.window.PopupProperties(focusable = true)
                ) {
                    Column(
                        modifier = Modifier
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                            .background(Color.White, RoundedCornerShape(6.dp))
                            .padding(2.dp)
                            .widthIn(min = 100.dp, max = 110.dp)
                    ) {
                        SortOption(
                            text = LocalizationManager.getString("tour_sort_recent"),
                            isSelected = selectedSort == "recent",
                            onClick = {
                                onSortChange("recent")
                                showSortDropdown = false
                            }
                        )
                        
                        SortOption(
                            text = LocalizationManager.getString("tour_sort_popular"),
                            isSelected = selectedSort == "popular",
                            onClick = {
                                onSortChange("popular")
                                showSortDropdown = false
                            }
                        )
                        
                        SortOption(
                            text = LocalizationManager.getString("tour_sort_price_low"),
                            isSelected = selectedSort == "price_low",
                            onClick = {
                                onSortChange("price_low")
                                showSortDropdown = false
                            }
                        )
                        
                        SortOption(
                            text = LocalizationManager.getString("tour_sort_price_high"),
                            isSelected = selectedSort == "price_high",
                            onClick = {
                                onSortChange("price_high")
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
        color = if (isSelected) Color(0xFF1A73E8) else Color(0xFF212121),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    )
}

private fun getTimestampForDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Long {
    // Calculate number of days since Unix epoch (01/01/1970)
    val daysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    // Check leap year
    val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    if (isLeapYear) {
        daysInMonth[1] = 29
    }

    // Calculate days from 01/01/1970 to 01/01/year
    var totalDays = 0L
    for (y in 1970 until year) {
        totalDays += if ((y % 4 == 0 && y % 100 != 0) || (y % 400 == 0)) 366 else 365
    }

    // Add days from 01/01/year to the target date
    for (m in 1 until month) {
        totalDays += daysInMonth[m - 1]
    }
    totalDays += day - 1

    // Calculate total seconds and convert to milliseconds
    val totalSeconds = totalDays * 86400L + hour * 3600L + minute * 60L + second
    return totalSeconds * 1000
}
