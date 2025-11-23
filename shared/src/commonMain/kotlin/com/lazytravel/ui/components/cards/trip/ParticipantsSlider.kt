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
fun ParticipantsSlider(
    maxParticipants: Int,
    onMaxParticipantsChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Số người tối đa",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Slider(
            value = maxParticipants.toFloat(),
            onValueChange = { onMaxParticipantsChange(it.toInt()) },
            valueRange = 2f..20f,
            steps = 17,
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
                text = "2 người",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "$maxParticipants người",
                color = Color(0xFFFF6B35),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "20 người",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
