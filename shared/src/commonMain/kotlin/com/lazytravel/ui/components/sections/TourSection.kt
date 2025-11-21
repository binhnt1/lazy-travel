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
import com.lazytravel.data.models.TourPackage
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.ui.components.cards.tours.TourCard
import kotlinx.coroutines.launch

@Composable
fun TourSection(
    modifier: Modifier = Modifier,
    onTourClick: (TourPackage) -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val tourRepo = remember { BaseRepository<TourPackage>() }
    var tours by remember { mutableStateOf<List<TourPackage>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            tourRepo.getRecords<TourPackage>().fold(
                onSuccess = { fetchedTours ->
                    tours = fetchedTours.take(5) // Limit to 5 for section display
                    isLoading = false
                },
                onFailure = { _ ->
                    isLoading = false
                }
            )
        }
    }

    // Section container with border and background
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                .padding(vertical = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = localizedString("tour_section_title"),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFFF4444)
                ) {
                    Text(
                        text = localizedString("hot"),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }


            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.Primary)
                }
            } else {
                // Make the LazyRow show cards with a fixed width smaller than full screen
                // and use contentPadding so user can see there are more items (partial peek).
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tours) { tour ->
                        TourCard(
                            tour = tour,
                            onClick = { onTourClick(tour) },
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
                        text = localizedString("view_all"),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Primary
                    )
                }
            }
        }
    }
}
