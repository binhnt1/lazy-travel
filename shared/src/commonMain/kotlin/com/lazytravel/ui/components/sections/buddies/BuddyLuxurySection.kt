package com.lazytravel.ui.components.sections.buddies

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
import com.lazytravel.data.models.Buddy
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.ui.components.cards.buddies.BuddyLuxuryCard
import kotlinx.coroutines.launch

@Composable
fun BuddyLuxurySection(
    onNavigateToDetail: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val buddyRepo = remember { BaseRepository<Buddy>() }
    var buddies by remember { mutableStateOf<List<Buddy>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            println("🔍 BuddyLuxurySection: Starting to fetch luxury trips...")
            buddyRepo.getRecords<Buddy>(
                page = 1,
                perPage = 10,
                sort = "-startDate",
                expand = "userId,cityId,cityId.countryId,buddyreviews_via_buddy",
                filter = "tags ~ '✨ LUXURY'",
            ).fold(
                onSuccess = { fetchedBuddies ->
                    fetchedBuddies.forEach { buddy ->
                        buddy.populateExpandedData()
                    }
                    println("✅ BuddyLuxurySection: Loaded ${fetchedBuddies.size} luxury trips")
                    buddies = fetchedBuddies
                    isLoading = false
                },
                onFailure = { exception ->
                    println("❌ BuddyLuxurySection: Failed to load luxury trips: ${exception.message}")
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
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD4AF37), // Gold
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row {
                        Text(
                            text = LocalizationManager.getString("buddy_luxury_title"),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "& Đẳng Cấp",
                            fontSize = 24.sp,
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
            if (buddies.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(buddies) { buddy ->
                        BuddyLuxuryCard(
                            buddy = buddy,
                            onClick = { onNavigateToDetail(buddy.id) }
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

