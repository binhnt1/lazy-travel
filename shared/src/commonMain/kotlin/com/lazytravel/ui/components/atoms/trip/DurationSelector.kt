package com.lazytravel.ui.components.atoms.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DurationSelector(
    duration: Int,
    onDurationChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 1..30
) {
    Column(modifier = modifier) {
        Slider(
            value = duration.toFloat(),
            onValueChange = { onDurationChange(it.toInt()) },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = range.count() - 2,
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
                text = "${range.first} ngày",
                color = Color.Gray
            )

            Text(
                text = if (duration > 1) "$duration ngày ${duration - 1} đêm" else "$duration ngày",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B35)
            )

            Text(
                text = "${range.last} ngày",
                color = Color.Gray
            )
        }
    }
}
