package com.lazytravel.ui.components.cards.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CreateTripBottomBar(
    currentStep: Int,
    totalSteps: Int,
    canProceed: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onPublish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button (hidden on first step)
            if (currentStep > 0) {
                @Suppress("DEPRECATION")
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFF6B35)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp
                    )
                ) {
                    Text("Quay lại")
                }
            } else {
                // Placeholder to keep layout balanced
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Next/Publish button
            if (currentStep < totalSteps - 1) {
                // Next button for steps 0-2
                Button(
                    onClick = onNext,
                    enabled = canProceed,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B35),
                        disabledContainerColor = Color(0xFFE0E0E0)
                    )
                ) {
                    Text("Tiếp theo")
                }
            } else {
                // Publish button for final step
                Button(
                    onClick = onPublish,
                    enabled = canProceed,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B35),
                        disabledContainerColor = Color(0xFFE0E0E0)
                    )
                ) {
                    Text("Xuất bản")
                }
            }
        }
    }
}
