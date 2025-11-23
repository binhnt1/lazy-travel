package com.lazytravel.ui.components.cards.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AgeRangeSelector(
    ageRange: String,
    onAgeRangeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val presets = listOf("18-25", "25-35", "35-45", "18-45", "Tất cả")
    var showCustomInput by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "Độ tuổi",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            presets.forEach { preset ->
                FilterChip(
                    selected = ageRange == preset,
                    onClick = {
                        onAgeRangeChange(preset)
                        showCustomInput = false
                    },
                    label = { Text(preset) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFFF6B35),
                        selectedLabelColor = Color.White
                    )
                )
            }

            FilterChip(
                selected = showCustomInput,
                onClick = { showCustomInput = !showCustomInput },
                label = { Text("Tuỳ chỉnh") }
            )
        }

        if (showCustomInput) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = ageRange,
                onValueChange = onAgeRangeChange,
                label = { Text("Độ tuổi (VD: 20-30)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}
