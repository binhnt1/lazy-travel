package com.lazytravel.ui.components.cards.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RequirementsInput(
    requirements: List<String>,
    onAddRequirement: (String) -> Unit,
    onRemoveRequirement: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }

    val suggestions = listOf(
        "Hòa đồng, vui vẻ",
        "Đúng giờ",
        "Có kinh nghiệm đi du lịch",
        "Thích chụp ảnh",
        "Không hút thuốc",
        "Tôn trọng lịch trình nhóm"
    )

    Column(modifier = modifier) {
        Text(
            text = "Yêu cầu thành viên",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Input field
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Thêm yêu cầu") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        onAddRequirement(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add requirement",
                    tint = Color(0xFFFF6B35)
                )
            }
        }

        // Suggestions
        if (requirements.isEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Gợi ý:",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                suggestions.forEach { suggestion ->
                    AssistChip(
                        onClick = { onAddRequirement(suggestion) },
                        label = { Text(suggestion, style = MaterialTheme.typography.bodySmall) }
                    )
                }
            }
        }

        // Added requirements
        if (requirements.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                requirements.forEach { requirement ->
                    InputChip(
                        selected = true,
                        onClick = { onRemoveRequirement(requirement) },
                        label = { Text(requirement) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = InputChipDefaults.inputChipColors(
                            selectedContainerColor = Color(0xFFFFEEE6),
                            selectedLabelColor = Color(0xFFFF6B35)
                        )
                    )
                }
            }
        }
    }
}
