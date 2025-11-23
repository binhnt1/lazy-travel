package com.lazytravel.ui.components.cards.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazytravel.data.models.enums.TripStatus

@Composable
fun StatusRadioGroup(
    selectedStatus: String,
    onStatusChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        StatusOption(
            status = TripStatus.DRAFT.name,
            title = "Lưu nháp",
            description = "Chỉ mình tôi có thể xem. Chưa ai thấy được.",
            selected = selectedStatus == TripStatus.DRAFT.name,
            onSelect = { onStatusChange(TripStatus.DRAFT.name) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatusOption(
            status = TripStatus.INVITING.name,
            title = "Xuất bản & mời người",
            description = "Công khai cho mọi người xem và tham gia. Bắt đầu mời thành viên.",
            selected = selectedStatus == TripStatus.INVITING.name,
            onSelect = { onStatusChange(TripStatus.INVITING.name) }
        )
    }
}

@Composable
private fun StatusOption(
    status: String,
    title: String,
    description: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color(0xFFFFEEE6) else Color(0xFFF5F5F5)
        ),
        border = if (selected) CardDefaults.outlinedCardBorder() else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFFFF6B35)
                )
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) Color(0xFFFF6B35) else Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
