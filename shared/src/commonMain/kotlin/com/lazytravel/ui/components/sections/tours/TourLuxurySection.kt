package com.lazytravel.ui.components.sections.tours

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.models.Tour
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.ui.components.cards.tours.TourLuxuryCard
import kotlinx.coroutines.launch

@Composable
fun TourLuxurySection(
    onTourClick: (String) -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val tourRepo = remember { BaseRepository<Tour>() }
    var tours by remember { mutableStateOf<List<Tour>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            println("🔍 TourLuxurySection: Starting to fetch luxury tours...")
            tourRepo.getRecords<Tour>(
                page = 1,
                perPage = 10,
                sort = "-startDate",
                expand = "userId,cityId,cityId.countryId,placeId,airlineId,tourProviderId,tourreviews_via_tour",
                filter = "tags ~ '✨ LUXURY'",
            ).fold(
                onSuccess = { fetchedTours ->
                    fetchedTours.forEach { tour ->
                        tour.populateExpandedData()
                    }
                    println("✅ TourLuxurySection: Loaded ${fetchedTours.size} luxury tours")
                    tours = fetchedTours
                    isLoading = false
                },
                onFailure = { exception ->
                    println("❌ TourLuxurySection: Failed to load luxury tours: ${exception.message}")
                    exception.printStackTrace()
                    isLoading = false
                }
            )
        }
    }

    // Navy blue luxury background
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF050A30))
            .padding(vertical = 24.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header with border bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "💎 LUXURY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD4AF37), // Gold
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row {
                        Text(
                            text = LocalizationManager.getString("tour_luxury_title"),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "& Đẳng Cấp",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF4FACFE), // Blue accent
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                // Border line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF1A2255))
                )
            }

            // Horizontal scroll with cards
            if (tours.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(tours) { tour ->
                        TourLuxuryCard(
                            tour = tour,
                            onClick = { onTourClick("placeholder") }
                        )
                    }
                }
            } else if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = LocalizationManager.getString("loading"),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = LocalizationManager.getString("no_data"),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
