package com.lazytravel.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.Tour
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.ui.utils.parseHexColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourScreen(
    onNavigateBack: () -> Unit = {},
    onSelectTour: (Tour) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val tourRepo = remember { BaseRepository<Tour>() }
    var allTours by remember { mutableStateOf<List<Tour>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Filter state
    var groupBy by remember { mutableStateOf("NONE") } // NONE, PROVIDER, LOCATION, TYPE
    var selectedDuration by remember { mutableStateOf<Int?>(null) }
    var priceRange by remember { mutableStateOf(0.0..10000000.0) }

    LaunchedEffect(Unit) {
        scope.launch {
            tourRepo.getRecords<Tour>().fold(
                onSuccess = { fetchedTours ->
                    allTours = fetchedTours
                    isLoading = false
                },
                onFailure = { _ ->
                    isLoading = false
                }
            )
        }
    }

    // Filter tours based on criteria
    val filteredTours = allTours.filter { tour ->
        val durationMatch = selectedDuration == null || tour.durationNights == selectedDuration
        val priceMatch = tour.currentPrice in priceRange
        durationMatch && priceMatch
    }

    // Group tours if needed
    val groupedTours = when (groupBy) {
        "PROVIDER" -> filteredTours.groupBy { it.expandedTourProvider?.name ?: "Unknown" }
        "LOCATION" -> filteredTours.groupBy { it.expandedCity?.name ?: "Unknown" }
        "TYPE" -> filteredTours.groupBy { it.tourType }
        else -> mapOf("Tất cả tour" to filteredTours)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Danh sách tour",
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
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.Primary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    // Hot tours section
                    if (allTours.isNotEmpty()) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "🔥 Tour nổi bật",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = AppColors.TextPrimary
                                )

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(allTours.filter { it.featured }.take(5)) { tour ->
                                        HotTourCard(
                                            tour = tour,
                                            onTourClick = { onSelectTour(tour) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Filter section
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Bộ lọc & Phân loại",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.TextPrimary
                            )

                            // Group by options
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    "NONE" to "Tất cả",
                                    "PROVIDER" to "Nhà cung cấp",
                                    "LOCATION" to "Địa điểm",
                                    "TYPE" to "Loại tour"
                                ).forEach { (value, label) ->
                                    FilterChip(
                                        selected = groupBy == value,
                                        onClick = { groupBy = value },
                                        label = { Text(label, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = AppColors.Primary,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }

                            // Duration filter
                            Text(
                                text = "Thời lượng",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AppColors.TextPrimary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(1, 2, 3, 4).forEach { nights ->
                                    FilterChip(
                                        selected = selectedDuration == nights,
                                        onClick = { selectedDuration = if (selectedDuration == nights) null else nights },
                                        label = { Text("${nights}N${nights-1}Đ", fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = AppColors.Primary,
                                            selectedLabelColor = Color.White
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            // Price range info
                            Text(
                                text = "Khoảng giá: ${(priceRange.start / 1000000).toInt()}M - ${(priceRange.endInclusive / 1000000).toInt()}M",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF666666)
                            )
                        }
                    }

                    // Grouped tours
                    groupedTours.forEach { (groupName, tours) ->
                        if (tours.isNotEmpty()) {
                            item {
                                Text(
                                    text = groupName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppColors.TextPrimary,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            items(tours) { tour ->
                                TourListCard(
                                    tour = tour,
                                    onTourClick = { onSelectTour(tour) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HotTourCard(
    tour: Tour,
    onTourClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onTourClick,
        modifier = modifier.width(200.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
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
                            fontSize = 40.sp
                        )
                    }
                }

                // Discount badge
                if (tour.discount > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFF3B30)
                    ) {
                        Text(
                            text = "-${tour.discount}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = tour.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 11.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⭐", fontSize = 10.sp)
                    Text(
                        text = "${tour.rating}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    Text(
                        text = "(${tour.reviewCount})",
                        fontSize = 9.sp,
                        color = Color(0xFF666666)
                    )
                }

                Text(
                    text = tour.getFormattedPrice(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppColors.Primary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun TourListCard(
    tour: Tour,
    onTourClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onTourClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tour image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (tour.bgImage.isNotEmpty()) {
                    AsyncImage(
                        model = tour.bgImage,
                        contentDescription = tour.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = tour.emoji,
                        fontSize = 40.sp
                    )
                }

                // Discount badge
                if (tour.discount > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFF3B30)
                    ) {
                        Text(
                            text = "-${tour.discount}%",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            // Tour info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = tour.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐ ${tour.rating} (${tour.reviewCount})",
                        fontSize = 11.sp,
                        color = Color(0xFF666666)
                    )
                    Text(
                        text = "•",
                        color = Color(0xFFCCCCCC)
                    )
                    Text(
                        text = tour.getDurationText(),
                        fontSize = 11.sp,
                        color = Color(0xFF666666)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tour.getFormattedPrice(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppColors.Primary,
                        fontSize = 14.sp
                    )
                    if (tour.originalPrice > 0) {
                        Text(
                            text = tour.getFormattedOriginalPrice(),
                            fontSize = 10.sp,
                            color = Color(0xFF999999),
                            style = MaterialTheme.typography.labelSmall.copy(
                                textDecoration = TextDecoration.LineThrough
                            )
                        )
                    }
                }
            }
        }
    }
}
