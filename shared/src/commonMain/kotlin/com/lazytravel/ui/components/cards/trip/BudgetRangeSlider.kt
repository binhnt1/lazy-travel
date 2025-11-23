package com.lazytravel.ui.components.cards.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BudgetRangeSlider(
    budgetMin: Double,
    budgetMax: Double,
    onBudgetRangeChange: (Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Ngân sách dự kiến (triệu VNĐ)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        RangeSlider(
            value = budgetMin.toFloat()..budgetMax.toFloat(),
            onValueChange = { range ->
                onBudgetRangeChange(range.start.toDouble(), range.endInclusive.toDouble())
            },
            valueRange = 0f..50f,
            steps = 49,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFFF6B35),
                activeTrackColor = Color(0xFFFF6B35)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Từ: ${budgetMin.toInt()}tr",
                color = Color(0xFFFF6B35),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Đến: ${budgetMax.toInt()}tr",
                color = Color(0xFFFF6B35),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
