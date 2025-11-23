package com.lazytravel.ui.components.cards.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun StepIndicator(
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        "Thông tin",
        "Ngân sách",
        "Địa điểm",
        "Xuất bản"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 16.dp, horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, stepName ->
                StepItem(
                    stepNumber = index + 1,
                    stepName = stepName,
                    isActive = index == currentStep,
                    isCompleted = index < currentStep,
                    modifier = Modifier.weight(1f)
                )

                // Connector line between steps (except after last step)
                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .weight(0.3f)
                            .height(2.dp)
                            .background(
                                if (index < currentStep) Color(0xFFFF6B35) else Color(0xFFE0E0E0)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun StepItem(
    stepNumber: Int,
    stepName: String,
    isActive: Boolean,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step circle
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isActive -> Color(0xFFFF6B35)
                        isCompleted -> Color(0xFFFF6B35)
                        else -> Color(0xFFE0E0E0)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted && !isActive) {
                Text(
                    text = "✓",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = stepNumber.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Step name
        Text(
            text = stepName,
            style = MaterialTheme.typography.bodySmall,
            color = when {
                isActive -> Color(0xFFFF6B35)
                isCompleted -> Color.Black
                else -> Color.Gray
            },
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
