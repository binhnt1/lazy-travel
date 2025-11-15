package com.lazytravel.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lazytravel.ui.components.molecules.StatItem

/**
 * Stats Bar - Organism
 *
 * Displays 4 statistics in a horizontal row:
 * - 50K+ Người dùng (Users)
 * - 120K Chuyến đi (Trips)
 * - 180 Điểm đến (Destinations)
 * - 4.8★ Đánh giá (Rating)
 *
 * From home_noauth.html design
 */
@Composable
fun StatsBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem(
            number = "50K+",
            label = "Người dùng"
        )

        StatItem(
            number = "120K",
            label = "Chuyến đi"
        )

        StatItem(
            number = "180",
            label = "Điểm đến"
        )

        StatItem(
            number = "4.8★",
            label = "Đánh giá"
        )
    }
}
