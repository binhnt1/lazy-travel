package com.lazytravel.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.localizedString

/**
 * Feature Card - Molecule
 * Displays a single feature with icon, title and description
 * Fixed width for horizontal scroll (140dp like HTML)
 */
@Composable
fun FeatureCard(
    icon: String,
    title: String,        // Translation key
    description: String,  // Translation key
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(140.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF8F9FA),
                        Color(0xFFFFFFFF)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Icon emoji
        Text(
            text = icon,
            fontSize = 32.sp,
            textAlign = TextAlign.Center
        )

        // Title (translated)
        Text(
            text = localizedString(title),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )

        // Description (translated)
        Text(
            text = localizedString(description),
            fontSize = 10.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}