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
import androidx.compose.material3.HorizontalDivider
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
import com.lazytravel.ui.components.cards.tours.TourLuxuryCard
import kotlinx.coroutines.launch

@Composable
fun TourLuxurySection(
    onTourClick: (Tour) -> Unit = {},
    onViewAllClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val tourRepo = remember { BaseRepository<Tour>() }
    var isLoading by remember { mutableStateOf(true) }
    var tours by remember { mutableStateOf<List<Tour>>(emptyList()) }

    // Load luxury tours (tours with LUXURY badge)
    LaunchedEffect(Unit) {
        scope.launch {
            tourRepo.getRecords<Tour>(
                page = 1,
                perPage = 10,
                sort = "-currentPrice", // Sort by price descending for luxury
                expand = "tourProviderId,cityId,placeId,airlineId"
            ).fold(
                onSuccess = { fetchedTours ->
                    // Filter tours with LUXURY badge
                    tours = fetchedTours.filter { tour ->
                        tour.badges?.any { it.contains("LUXURY", ignoreCase = true) } == true
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

    val navyColor = Color(0xFF050A30)
    val goldColor = Color(0xFFD4AF37)

    // Luxury section with navy background
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(navyColor)
            .padding(vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header with gold accent
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    // Small label
                    Text(
                        text = "💎 LUXURY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = goldColor,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Title row
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            Text(
                                text = "Tour Luxury",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "& Đẳng Cấp",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF4FACFE) // Blue accent
                            )
                        }

                        Text(
                            text = "Xem tất cả →",
                            fontSize = 13.sp,
                            color = goldColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Divider line
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFF1A2255)
                )
            }

            // Luxury tours horizontal scroll
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }
                tours.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Chưa có tour luxury",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(tours) { tour ->
                            TourLuxuryCard(
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
