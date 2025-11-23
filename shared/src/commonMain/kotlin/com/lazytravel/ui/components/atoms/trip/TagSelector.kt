package com.lazytravel.ui.components.atoms.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSelector(
    selectedTags: List<String>,
    onToggleTag: (String) -> Unit,
    maxSelect: Int = 5,
    modifier: Modifier = Modifier
) {
    val availableTags = listOf(
        "Phượt", "Luxury", "Budget", "Backpacker",
        "Gia đình", "Nhiếp ảnh", "Ẩm thực"
    )

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        availableTags.forEach { tag ->
            val isSelected = selectedTags.contains(tag)
            val canSelect = selectedTags.size < maxSelect || isSelected

            FilterChip(
                selected = isSelected,
                onClick = {
                    if (canSelect) onToggleTag(tag)
                },
                label = { Text(tag) },
                enabled = canSelect,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFF6B35),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}
