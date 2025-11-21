package com.lazytravel.ui.components.sections.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.lazytravel.ui.components.cards.buddies.BuddyHotCard
import com.lazytravel.ui.components.atoms.LoadingIndicator
import kotlinx.coroutines.launch

@Composable
fun BuddyHotSection(
    onNavigateToDetail: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val buddyRepo = remember { BaseRepository<Buddy>() }
    var isLoading by remember { mutableStateOf(true) }
    var buddies by remember { mutableStateOf<List<Buddy>>(emptyList()) }

    // Load hot trips (trips with HOT tag)
    LaunchedEffect(Unit) {
        scope.launch {
            buddyRepo.getRecords<Buddy>(
                page = 1,
                perPage = 10,
                sort = "-startDate",
                expand = "userId,cityId,cityId.countryId,buddyreviews_via_buddy",
                filter = "tags ~ '🔥 HOT' || status='AVAILABLE' || status='URGENT'",
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

    // Hot section with gradient background
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFF3EE) // Light orange background like the design
            )
            .border(
                width = 1.dp,
                color = Color(0xFFFFE0D0),
                shape = RoundedCornerShape(0.dp)
            )
            .padding(vertical = 16.dp)
    ) {
        Column {
            // Section header with hot theme
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                        text = LocalizationManager.getString("buddy_hot_trips"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE64A19)
                    )
                }

                Text(
                    text = "Xem tất cả",
                    fontSize = 12.sp,
                    color = Color(0xFFFF6B35),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hot trips horizontal scroll
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
                            text = "Chưa có hành trình HOT nào",
                            color = Color.Gray,
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
                        items(buddies) { trip ->
                            BuddyHotCard(
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
