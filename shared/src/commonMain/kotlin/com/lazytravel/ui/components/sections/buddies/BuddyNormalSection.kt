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
    onNavigateToDetail: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val buddyRepo = remember { BaseRepository<Buddy>() }
    var isLoading by remember { mutableStateOf(true) }
    var buddies by remember { mutableStateOf<List<Buddy>>(emptyList()) }

    // Load normal trips (AVAILABLE status, excluding HOT and LUXURY tags)
    LaunchedEffect(Unit) {
        scope.launch {
            buddyRepo.getRecords<Buddy>(
                page = 1,
                perPage = 10,
                sort = "-startDate",
                expand = "userId,cityId,cityId.countryId,buddyreviews_via_buddy,buddyparticipant_via_buddy.userId",
                filter = "status='AVAILABLE' && tags !~ '🔥 HOT' && tags !~ '✨ LUXURY'",
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
                Text(
                    text = "🔥 Sắp khởi hành",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )

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

