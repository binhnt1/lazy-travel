package com.lazytravel.ui.components.atoms.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RegionSelector(
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val regions = listOf("Miền Bắc", "Miền Trung", "Miền Nam", "Nước ngoài")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        regions.forEach { region ->
            FilterChip(
                selected = region == selected,
                onClick = { onSelect(region) },
                label = { Text(region) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFF6B35),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}
