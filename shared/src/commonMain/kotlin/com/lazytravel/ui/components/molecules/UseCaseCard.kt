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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.models.UseCase

@Composable
fun UseCaseCard(
    useCase: UseCase,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Icon Box
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    brush = parseGradient(useCase.gradient),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = useCase.icon,
                fontSize = 24.sp
            )
        }

        // Content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = useCase.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Text(
                text = useCase.description,
                fontSize = 12.sp,
                color = Color(0xFF666666),
                lineHeight = 18.sp
            )

            // Features
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Top
            ) {
                useCase.features.forEach { feature ->
                    Text(
                        text = feature,
                        fontSize = 10.sp,
                        color = Color(0xFFFF6B35),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

// Helper function to parse gradient string
private fun parseGradient(gradientString: String): Brush {
    // Extract colors from gradient string like "linear-gradient(135deg, #667EEA, #764BA2)"
    val colorPattern = "#[0-9A-Fa-f]{6}".toRegex()
    val colors = colorPattern.findAll(gradientString)
        .map { hexToColor(it.value) }
        .toList()
    
    return if (colors.size >= 2) {
        Brush.linearGradient(colors = colors)
    } else {
        // Fallback gradient if parsing fails
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF667EEA),
                Color(0xFF764BA2)
            )
        )
    }
}

// Helper function to convert hex color to Compose Color
private fun hexToColor(hex: String): Color {
    val cleanHex = hex.removePrefix("#")
    val colorInt = cleanHex.toLongOrNull(16) ?: 0xFF667EEA
    return Color(0xFF000000 or colorInt)
}
