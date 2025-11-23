package com.lazytravel.ui.screens.trip.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazytravel.ui.components.cards.trip.AgeRangeSelector
import com.lazytravel.ui.components.cards.trip.BudgetRangeSlider
import com.lazytravel.ui.components.cards.trip.ParticipantsSlider
import com.lazytravel.ui.components.cards.trip.RequirementsInput

@Composable
fun BudgetMembersStep(
    budgetMin: Double,
    budgetMax: Double,
    onBudgetRangeChange: (Double, Double) -> Unit,
    maxParticipants: Int,
    onMaxParticipantsChange: (Int) -> Unit,
    ageRange: String,
    onAgeRangeChange: (String) -> Unit,
    requirements: List<String>,
    onAddRequirement: (String) -> Unit,
    onRemoveRequirement: (String) -> Unit,
    validationErrors: Map<String, String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Title
        Text(
            text = "Ngân sách & Thành viên",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B35)
        )

        // Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF8F0)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "💡 Gợi ý",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B35)
                )
                Text(
                    text = "• Ngân sách nên bao gồm: Di chuyển, lưu trú, ăn uống, vui chơi\n" +
                            "• Số người phù hợp: 4-8 người (dễ quản lý, chi phí hợp lý)\n" +
                            "• Độ tuổi tương đồng giúp hành trình vui vẻ hơn",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        HorizontalDivider()

        // Budget Range
        Column {
            BudgetRangeSlider(
                budgetMin = budgetMin,
                budgetMax = budgetMax,
                onBudgetRangeChange = onBudgetRangeChange
            )

            if (validationErrors.containsKey("budget")) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = validationErrors["budget"]!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // Max Participants
        Column {
            ParticipantsSlider(
                maxParticipants = maxParticipants,
                onMaxParticipantsChange = onMaxParticipantsChange
            )

            if (validationErrors.containsKey("maxParticipants")) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = validationErrors["maxParticipants"]!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // Age Range
        Column {
            Text(
                text = "Độ tuổi phù hợp *",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Chọn độ tuổi mong muốn của thành viên",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))

            AgeRangeSelector(
                ageRange = ageRange,
                onAgeRangeChange = onAgeRangeChange
            )

            if (validationErrors.containsKey("ageRange")) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = validationErrors["ageRange"]!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // Requirements
        Column {
            Text(
                text = "Yêu cầu thành viên (tùy chọn)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Những yêu cầu với người tham gia hành trình",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))

            RequirementsInput(
                requirements = requirements,
                onAddRequirement = onAddRequirement,
                onRemoveRequirement = onRemoveRequirement
            )
        }

        // Bottom spacing for scroll
        Spacer(modifier = Modifier.height(80.dp))
    }
}
