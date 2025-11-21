package com.lazytravel.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.models.HowItWork

@Composable
fun HowItWorkCard(
    step: HowItWork,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFE3E8F0),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF6F8FC),
                        Color.White
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        // Absolute positioned number badge (top-right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(50.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF667EEA),
                            Color(0xFF764BA2)
                        )
                    ),
                    shape = RoundedCornerShape(8.dp),
                    alpha = 0.15f
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step.order.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF667EEA).copy(alpha = 0.5f)
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Icon
            Text(
                text = step.icon,
                fontSize = 48.sp
            )

            // Title
            Text(
                text = step.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                lineHeight = 16.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Description
            Text(
                text = step.description,
                fontSize = 11.sp,
                color = Color(0xFF666666),
                lineHeight = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Badge at bottom
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFFFF3E0)
            ) {
                Text(
                    text = step.badge,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B35),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
