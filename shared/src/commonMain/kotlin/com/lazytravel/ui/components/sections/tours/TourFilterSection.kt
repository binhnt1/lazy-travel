package com.lazytravel.ui.components.sections.tours

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.ui.theme.AppColors

@Composable
fun TourFilterSection(
    onFilterChanged: (Map<String, Any>) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedDuration by remember { mutableStateOf<String?>(null) }
    var selectedPriceRange by remember { mutableStateOf<String?>(null) }
    var selectedType by remember { mutableStateOf<String?>(null) }

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Section title
            Text(
                text = "🔍 Bộ lọc",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )

            // Duration filter
            Text(
                text = "Thời lượng",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF666666)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("3N2Đ", "4N3Đ", "5N4Đ", "7N6Đ", "10N+").forEach { duration ->
                    FilterChip(
                        selected = selectedDuration == duration,
                        onClick = {
                            selectedDuration = if (selectedDuration == duration) null else duration
                            onFilterChanged(
                                mapOf(
                                    "duration" to (selectedDuration ?: ""),
                                    "priceRange" to (selectedPriceRange ?: ""),
                                    "type" to (selectedType ?: "")
                                )
                            )
                        },
                        label = { Text(duration, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppColors.Primary,
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFF5F5F5),
                            labelColor = Color(0xFF666666)
                        )
                    )
                }
            }

            // Price range filter
            Text(
                text = "Khoảng giá",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF666666)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("< 5tr", "5-10tr", "10-20tr", "20-50tr", "> 50tr").forEach { range ->
                    FilterChip(
                        selected = selectedPriceRange == range,
                        onClick = {
                            selectedPriceRange = if (selectedPriceRange == range) null else range
                            onFilterChanged(
                                mapOf(
                                    "duration" to (selectedDuration ?: ""),
                                    "priceRange" to (selectedPriceRange ?: ""),
                                    "type" to (selectedType ?: "")
                                )
                            )
                        },
                        label = { Text(range, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppColors.Primary,
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFF5F5F5),
                            labelColor = Color(0xFF666666)
                        )
                    )
                }
            }

            // Tour type filter
            Text(
                text = "Loại tour",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF666666)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Trong nước", "Nước ngoài", "Du thuyền", "Trekking", "Nghỉ dưỡng").forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = {
                            selectedType = if (selectedType == type) null else type
                            onFilterChanged(
                                mapOf(
                                    "duration" to (selectedDuration ?: ""),
                                    "priceRange" to (selectedPriceRange ?: ""),
                                    "type" to (selectedType ?: "")
                                )
                            )
                        },
                        label = { Text(type, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppColors.Primary,
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFF5F5F5),
                            labelColor = Color(0xFF666666)
                        )
                    )
                }
            }
        }
    }
}
