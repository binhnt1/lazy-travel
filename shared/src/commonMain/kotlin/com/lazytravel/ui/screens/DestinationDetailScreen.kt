package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.models.Destination
import com.lazytravel.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailScreen(
    destination: Destination,
    onNavigateBack: () -> Unit = {},
    onExploreClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = destination.name,
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
                .verticalScroll(rememberScrollState())
        ) {
            // Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        color = when (destination.emoji) {
                            "🏖️" -> Color(0xFF667EEA).copy(alpha = 0.15f)
                            "⛰️" -> Color(0xFFF093FB).copy(alpha = 0.15f)
                            "🌸" -> Color(0xFFFF9800).copy(alpha = 0.15f)
                            else -> Color(0xFF667EEA).copy(alpha = 0.15f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = destination.emoji,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 80.sp
                )
            }

            // Content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Name with badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = destination.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary,
                        modifier = Modifier.weight(1f)
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
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Rating and reviews
                Text(
                    text = "⭐ ${(destination.rating * 10).toInt() / 10.0} (${destination.reviewsCount} đánh giá) • ✈️ ${destination.tripsCount} chuyến",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF666666),
                    fontSize = 11.sp
                )

                // Location info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Thông tin địa điểm",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )

                    DestinationInfoRow(
                        icon = "💰",
                        label = "Chi phí",
                        value = destination.costRange
                    )
                    DestinationInfoRow(
                        icon = "🌡️",
                        label = "Nhiệt độ",
                        value = destination.temperature
                    )
                    DestinationInfoRow(
                        icon = "⏱️",
                        label = "Thời lượng",
                        value = destination.tripDuration
                    )
                    DestinationInfoRow(
                        icon = destination.bestSeasonEmoji,
                        label = "Mùa đẹp nhất",
                        value = destination.bestSeasonName
                    )
                }

                // Best for section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Điểm nổi bật",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        destination.highlights?.forEach { highlight ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFF5F5F5),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                            ) {
                                Text(
                                    text = highlight,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF666666),
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }

                // Visitor count
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = AppColors.Primary.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👥",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Đã được xem",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF999999),
                                fontSize = 10.sp
                            )
                            Text(
                                text = "${destination.viewersCount} lượt xem",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.TextPrimary
                            )
                        }
                    }
                }

                // Explore button
                Button(
                    onClick = onExploreClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.Primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Khám phá ngay",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun DestinationInfoRow(
    icon: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(24.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF999999),
                fontSize = 10.sp
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary
            )
        }
    }
}
