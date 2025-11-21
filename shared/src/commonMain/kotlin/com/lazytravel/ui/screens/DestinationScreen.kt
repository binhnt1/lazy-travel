package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.Destination
import com.lazytravel.ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationScreen(
    onNavigateBack: () -> Unit = {},
    onSelectDestination: (Destination) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val destRepo = remember { BaseRepository<Destination>() }
    var destinations by remember { mutableStateOf<List<Destination>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            destRepo.getRecords<Destination>().fold(
                onSuccess = { fetchedDestinations ->
                    destinations = fetchedDestinations
                    isLoading = false
                },
                onFailure = { _ ->
                    isLoading = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Điểm đến phổ biến",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = AppColors.TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppColors.Background)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.Primary)
                }
            } else if (destinations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Chưa có điểm đến nào",
                        color = Color(0xFF999999),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(destinations) { destination ->
                        DestinationListCard(
                            destination = destination,
                            onCardClick = { onSelectDestination(destination) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DestinationListCard(
    destination: Destination,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = destination.emoji,
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 40.sp
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Title with badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = destination.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Badge
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when (destination.badgeType) {
                            "TRENDING" -> Color(0xFFFF6B6B).copy(alpha = 0.2f)
                            "BEST_SEASON" -> Color(0xFF64B5F6).copy(alpha = 0.2f)
                            else -> Color(0xFF81C784).copy(alpha = 0.2f)
                        }
                    ) {
                        Text(
                            text = destination.badgeText.take(15),
                            style = MaterialTheme.typography.labelSmall,
                            color = when (destination.badgeType) {
                                "TRENDING" -> Color(0xFFC62828)
                                "BEST_SEASON" -> Color(0xFF1565C0)
                                else -> Color(0xFF2E7D32)
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Rating and reviews
                Text(
                    text = "⭐ ${destination.rating} (${destination.reviewsCount}) • ✈️ ${destination.tripsCount}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF666666),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Cost and duration
                Text(
                    text = "💰 ${destination.costRange} • ⏱️ ${destination.tripDuration}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF888888),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Highlights preview
                if (!destination.highlights.isNullOrEmpty()) {
                    Text(
                        text = destination.highlights.take(2).joinToString(" • "),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppColors.Primary,
                        fontSize = 9.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
