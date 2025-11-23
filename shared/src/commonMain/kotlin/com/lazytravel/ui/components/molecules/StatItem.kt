package com.lazytravel.ui.components.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.ui.theme.AppColors

/**
 * Stat Item - Molecule
 *
 * Displays a single statistic with number and label
 * Used in StatsBar organism
 *
 * @param number The statistic number (e.g., "50K+", "120K", "4.8★")
 * @param label The description label (e.g., "Người dùng", "Chuyến đi")
 */
@Composable
fun StatItem(
    number: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Number - large, bold, orange
        Text(
            text = number,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.Primary  // #FF6B35
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Label - small, gray
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.TextSecondary  // #666
        )
    }
}
