package com.lazytravel.ui.components.sections.tours

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.Tour
import com.lazytravel.ui.components.atoms.LoadingIndicator
import com.lazytravel.ui.components.cards.tours.TourHotCard
import kotlinx.coroutines.launch

@Composable
fun TourFeaturedSection(
    onTourClick: (Tour) -> Unit = {},
    onViewAllClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val tourRepo = remember { BaseRepository<Tour>() }
    var isLoading by remember { mutableStateOf(true) }
    var tours by remember { mutableStateOf<List<Tour>>(emptyList()) }

    // Load featured tours (tours with HOT/FEATURED badge or featured=true)
    LaunchedEffect(Unit) {
        scope.launch {
            tourRepo.getRecords<Tour>(
                page = 1,
                perPage = 10,
                sort = "-created",
                expand = "tourProviderId,cityId,placeId,airlineId"
            ).fold(
                onSuccess = { fetchedTours ->
                    // Filter tours with HOT, FEATURED badges or featured flag
                    tours = fetchedTours.filter { tour ->
                        tour.featured ||
                        tour.badges?.any { it.contains("HOT", ignoreCase = true) || it.contains("FEATURED", ignoreCase = true) } == true
                    }.take(10)

                    // Populate expanded data
                    tours.forEach { tour ->
                        tour.populateExpandedData()
                    }

                    isLoading = false
                },
                onFailure = { exception ->
                    exception.printStackTrace()
                    isLoading = false
                }
            )
        }
    }

    // Featured section with yellow/gold background
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF1C40F)) // Yellow background like design
            .padding(vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Section header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tour nổi bật",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50) // Dark text on yellow
                    )
                }

                Text(
                    text = "Xem tất cả →",
                    fontSize = 13.sp,
                    color = Color(0xFF2C3E50),
                    fontWeight = FontWeight.Medium
                )
            }

            // Featured tours horizontal scroll
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
                            text = "Chưa có tour nổi bật",
                            color = Color(0xFF2C3E50),
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        items(tours) { tour ->
                            TourHotCard(
                                tour = tour,
                                onClick = { onTourClick(tour) }
                            )
                        }
                    }
                }
            }
        }
    }
}
