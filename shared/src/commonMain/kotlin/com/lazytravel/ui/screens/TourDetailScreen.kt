package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.data.models.Tour
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.ui.utils.parseHexColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourDetailScreen(
    tour: Tour,
    onNavigateBack: () -> Unit = {},
    onBookClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = tour.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
            // Hero image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                if (tour.bgImage.isNotEmpty()) {
                    AsyncImage(
                        model = tour.bgImage,
                        contentDescription = tour.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(parseHexColor(tour.thumbnailColor) ?: AppColors.Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tour.emoji,
                            fontSize = 80.sp
                        )
                    }
                }

                // Discount badge
                if (tour.discount > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFF3B30)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Giảm",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "-${tour.discount}%",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title and rating
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = tour.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFFFF3E0)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "⭐", fontSize = 14.sp)
                                Text(
                                    text = "${tour.rating}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF57F17)
                                )
                            }
                        }

                        Text(
                            text = "${tour.reviewCount} đánh giá",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }

                // Price section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5F5)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (tour.originalPrice > 0) {
                            Text(
                                text = tour.getFormattedOriginalPrice(),
                                fontSize = 14.sp,
                                color = Color(0xFF999999),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    textDecoration = TextDecoration.LineThrough
                                )
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = tour.getFormattedPrice(),
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = AppColors.Primary,
                                fontSize = 32.sp
                            )
                            Text(
                                text = "/ người",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }

                // Quick info chips
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Thông tin chuyến đi",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TourInfoChip(
                            icon = "📅",
                            label = tour.getDurationText(),
                            modifier = Modifier.weight(1f)
                        )
                        TourInfoChip(
                            icon = "👥",
                            label = tour.getGroupSizeText(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TourInfoChip(
                            icon = "🏢",
                            label = tour.expandedTourProvider?.name ?: "Tour Provider",
                            modifier = Modifier.weight(1f)
                        )
                        TourInfoChip(
                            icon = "📍",
                            label = tour.expandedCity?.name ?: "Destination",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Description
                if (tour.description.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Mô tả",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )

                        Text(
                            text = tour.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF424242),
                            lineHeight = 22.sp
                        )
                    }
                }

                // Highlights
                if (!tour.highlights.isNullOrEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Điểm nổi bật",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            tour.highlights.forEach { highlight ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "✓",
                                        color = AppColors.Primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = highlight,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF666666)
                                    )
                                }
                            }
                        }
                    }
                }

                // Included
                if (!tour.included.isNullOrEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Bao gồm",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                tour.included?.forEach { item ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text("✓", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                                        Text(
                                            text = item,
                                            fontSize = 12.sp,
                                            color = Color(0xFF2E7D32)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Excluded
                if (!tour.excluded.isNullOrEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Không bao gồm",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFEBEE)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                tour.excluded?.forEach { item ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text("✗", color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                                        Text(
                                            text = item,
                                            fontSize = 12.sp,
                                            color = Color(0xFFC62828)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Book button
                Button(
                    onClick = onBookClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.Primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Đặt tour ngay",
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
private fun TourInfoChip(
    icon: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFF5F5F5),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 14.sp)
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF666666),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
