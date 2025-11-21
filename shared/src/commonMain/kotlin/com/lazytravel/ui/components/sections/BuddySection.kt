package com.lazytravel.ui.components.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.Buddy
import com.lazytravel.ui.components.cards.buddies.BuddyCard
import com.lazytravel.ui.theme.AppColors
import kotlinx.coroutines.launch


@Composable
fun BuddySection(
    modifier: Modifier = Modifier,
    onJoinClick: (Buddy) -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val buddyRepo = remember { BaseRepository<Buddy>() }
    var isLoading by remember { mutableStateOf(true) }
    var buddies by remember { mutableStateOf<List<Buddy>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            buddyRepo.getRecords<Buddy>(
                page = 1,
                perPage = 10,
                sort = "-startDate",
                filter = "status='AVAILABLE' || status='URGENT'",
                expand = "userId,cityId,cityId.countryId,buddyreviews_via_buddy"
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

    // Section container
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = "👥 CỘNG ĐỒNG",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tìm bạn đồng hành",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Kết nối với người có cùng sở thích",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.Primary)
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(buddies) { buddy ->
                        BuddyCard(
                            buddy = buddy,
                            onJoinClick = { onJoinClick(buddy) },
                            modifier = Modifier.width(290.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // View All button
                Button(
                    onClick = onViewAllClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.Primary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Xem tất cả →",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Primary
                    )
                }
            }
        }
    }
}
