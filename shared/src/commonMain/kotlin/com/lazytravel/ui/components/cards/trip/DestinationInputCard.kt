package com.lazytravel.ui.components.cards.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazytravel.ui.screens.trip.DestinationInput

@Composable
fun DestinationInputCard(
    destination: DestinationInput,
    onUpdate: (DestinationInput) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with badge and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Badge(
                        containerColor = Color(0xFFFF6B35)
                    ) {
                        Text("#${destination.orderIndex + 1}")
                    }

                    Text(
                        text = destination.name.ifEmpty { "Địa điểm ${destination.orderIndex + 1}" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Toggle edit"
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.DragHandle,
                        contentDescription = "Drag to reorder",
                        tint = Color.Gray
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))

                // Destination Name
                OutlinedTextField(
                    value = destination.name,
                    onValueChange = { onUpdate(destination.copy(name = it)) },
                    label = { Text("Tên địa điểm *") },
                    placeholder = { Text("VD: Phú Quốc, Đà Lạt...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                OutlinedTextField(
                    value = destination.description,
                    onValueChange = { onUpdate(destination.copy(description = it)) },
                    label = { Text("Lý do đề xuất") },
                    placeholder = { Text("Tại sao nên chọn địa điểm này?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Image URL
                OutlinedTextField(
                    value = destination.imageUrl,
                    onValueChange = { onUpdate(destination.copy(imageUrl = it)) },
                    label = { Text("Link ảnh") },
                    placeholder = { Text("https://...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Estimated Budget
                OutlinedTextField(
                    value = if (destination.estimatedBudget > 0) destination.estimatedBudget.toString() else "",
                    onValueChange = {
                        onUpdate(destination.copy(estimatedBudget = it.toDoubleOrNull() ?: 0.0))
                    },
                    label = { Text("Chi phí ước tính (triệu VNĐ)") },
                    placeholder = { Text("VD: 5.5") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }
}
