package com.lazytravel.ui.components.sections.tours

import androidx.compose.animation.core.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.models.Tour
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.ui.components.cards.tours.TourHotCard
import com.lazytravel.ui.components.atoms.LoadingIndicator
import kotlinx.coroutines.launch

@Composable
fun TourHotSection(
    onTourClick: (String) -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val tourRepo = remember { BaseRepository<Tour>() }
    var isLoading by remember { mutableStateOf(true) }
    var tours by remember { mutableStateOf<List<Tour>>(emptyList()) }

    // Load hot tours (tours with HOT tag)
    LaunchedEffect(Unit) {
        scope.launch {
            tourRepo.getRecords<Tour>(
                page = 1,
                perPage = 10,
                sort = "-created",
                expand = "tourProviderId,cityId,placeId,airlineId",
                filter = "tags ~ '🔥'",
            ).fold(
                onSuccess = { fetchedTours ->
                    fetchedTours.forEach { tour ->
                        tour.populateExpandedData()
                    }
                    tours = fetchedTours
                    isLoading = false
                },
                onFailure = { exception ->
                    exception.printStackTrace()
                    isLoading = false
                }
            )
        }
    }

    // Featured section with gradient background matching HTML design
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF8B6914), // Dark gold
                        Color(0xFFC19A6B)  // Light brown gold
                    ),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(1f, 1f)
                )
            )
    ) {
        // Add top border line like in HTML
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFFF39C12), // Gold color
                            Color.Transparent
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
            // Section header matching HTML design
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Fire icon with subtle animation
                    val infiniteTransition = rememberInfiniteTransition(label = "fire")
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "scale"
                    )
                    
                    Text(
                        text = "🔥",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .scale(scale)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Tour nổi bật",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                    }
                }

                // "Xem tất cả" button
                Text(
                    text = "Xem tất cả →",
                    fontSize = 13.sp,
                    color = Color(0xFF2C3E50),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable { onViewAllClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hot tours horizontal scroll
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
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
                            text = "Chưa có tour nổi bật nào",
                            color = Color(0xFF2C3E50),
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 0.dp)
                    ) {
                        items(tours) { tour ->
                            TourHotCard(
                                tour = tour,
                                onClick = { onTourClick(tour.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}