package com.lazytravel.ui.components.sections.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.lazytravel.data.models.Buddy
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.ui.components.cards.buddies.BuddyNormalCard
import com.lazytravel.ui.components.atoms.LoadingIndicator
import kotlinx.coroutines.launch

@Composable
fun BuddyNormalSection(
    filterTab: String = "all",
    filterInterests: Set<String> = emptySet(),
    sortBy: String = "recent",
    searchQuery: String = "",
    filterMinCost: String = "",
    filterMaxCost: String = "",
    filterMonth: String = "",
    filterYear: String = "",
    onNavigateToDetail: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val buddyRepo = remember { BaseRepository<Buddy>() }
    var isLoading by remember { mutableStateOf(true) }
    var buddies by remember { mutableStateOf<List<Buddy>>(emptyList()) }

    // ✅ Reload data when filter changes
    LaunchedEffect(filterTab, filterInterests, sortBy, searchQuery, filterMinCost, filterMaxCost, filterMonth, filterYear) {
        isLoading = true
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
                        "backpacker" -> "tags ~ 'Backpacker'"
                        "photo" -> "tags ~ 'Nhiếp ảnh'"
                        "food" -> "tags ~ 'Ẩm thực'"
                        "camping" -> "tags ~ 'Cắm trại'"
                        "cultural" -> "tags ~ 'Văn hóa'"
                        else -> ""
                    }
                }.let { " && ($it)" }
            } else ""

            // Add search filter
            val searchFilter = if (searchQuery.isNotEmpty()) {
                " && (tripTitle ~ '$searchQuery' || description ~ '$searchQuery' || destination ~ '$searchQuery' || tags ~ '$searchQuery' || cityId.name ~ '$searchQuery' || cityId.countryId.name ~ '$searchQuery')"
            } else ""

            // Add cost filter (using estimatedBudget in VND)
            val costFilter = buildString {
                if (filterMinCost.isNotEmpty() || filterMaxCost.isNotEmpty()) {
                    append(" && (")
                    val filters = mutableListOf<String>()
                    if (filterMinCost.isNotEmpty()) {
                        filterMinCost.toDoubleOrNull()?.let {
                            val minBudgetVND = (it * 1_000_000).toLong()
                            filters.add("estimatedBudget>=$minBudgetVND")
                        }
                    }
                    if (filterMaxCost.isNotEmpty()) {
                        filterMaxCost.toDoubleOrNull()?.let {
                            val maxBudgetVND = (it * 1_000_000).toLong()
                            filters.add("estimatedBudget<=$maxBudgetVND")
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
                "matched" -> "-matchScore,-startDate" // Custom match score (you can implement this)
                "popular" -> "-viewCount,-startDate"
                else -> "-startDate" // "recent"
            }

            buddyRepo.getRecords<Buddy>(
                page = 1,
                perPage = 20, // Show more results when filtered
                sort = sortOrder,
                expand = "userId,cityId,cityId.countryId,buddyreviews_via_buddy,buddyparticipant_via_buddy.userId",
                filter = finalFilter,
            ).fold(
                onSuccess = { fetchedBuddies ->
                    fetchedBuddies.forEach { buddy ->
                        buddy.populateExpandedData()
                    }
                    buddies = fetchedBuddies
                    isLoading = false
                },
                onFailure = { exception ->
                    exception.printStackTrace()
                    isLoading = false
                }
            )
        }
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
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Section header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🔥 Sắp khởi hành",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )

                    // Show filter status
                    if (filterTab != "all" || filterInterests.isNotEmpty() || sortBy != "recent" ||
                        searchQuery.isNotEmpty() || filterMinCost.isNotEmpty() ||
                        filterMaxCost.isNotEmpty() || filterMonth.isNotEmpty() || filterYear.isNotEmpty()) {
                        Text(
                            text = if (searchQuery.isNotEmpty()) {
                                "Tìm kiếm: \"$searchQuery\" - ${buddies.size} kết quả"
                            } else {
                                "Kết quả: ${buddies.size} hành trình"
                            },
                            fontSize = 11.sp,
                            color = Color(0xFFFF6B35),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Text(
                    text = "Xem tất cả →",
                    fontSize = 13.sp,
                    color = Color(0xFFFF6B35),
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Normal trips vertical list
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
                buddies.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Chưa có hành trình nào",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        buddies.forEach { trip ->
                            BuddyNormalCard(
                                buddy = trip,
                                onClick = onNavigateToDetail
                            )
                        }
                    }
                }
            }
        }
    }
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

