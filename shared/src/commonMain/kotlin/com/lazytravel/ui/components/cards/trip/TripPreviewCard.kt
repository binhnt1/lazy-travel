package com.lazytravel.ui.components.cards.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazytravel.ui.screens.trip.DestinationInput
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TripPreviewCard(
    tripTitle: String,
    emoji: String,
    description: String,
    startDate: Long,
    duration: Int,
    region: String,
    tags: List<String>,
    budgetMin: Double,
    budgetMax: Double,
    maxParticipants: Int,
    ageRange: String,
    requirements: List<String>,
    destinations: List<DestinationInput>,
    allowMultipleVotes: Boolean,
    votingEndsAt: Long,
    onEditBasicInfo: () -> Unit,
    onEditBudget: () -> Unit,
    onEditDestinations: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Title
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = tripTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sections
            PreviewSection(
                title = "Thông tin cơ bản",
                onEdit = onEditBasicInfo
            ) {
                PreviewItem("Mô tả", description)
                PreviewItem("Ngày đi", formatDate(startDate))
                PreviewItem("Thời gian", if (duration > 1) "$duration ngày ${duration - 1} đêm" else "$duration ngày")
                PreviewItem("Vùng", region)
                if (tags.isNotEmpty()) {
                    PreviewItem("Tags", tags.joinToString(", "))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            PreviewSection(
                title = "Ngân sách & Thành viên",
                onEdit = onEditBudget
            ) {
                PreviewItem("Ngân sách", "${budgetMin.toInt()}tr - ${budgetMax.toInt()}tr VNĐ")
                PreviewItem("Số người", "$maxParticipants người")
                PreviewItem("Độ tuổi", ageRange)
                if (requirements.isNotEmpty()) {
                    PreviewItem("Yêu cầu", requirements.joinToString(", "))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            PreviewSection(
                title = "Địa điểm đề xuất (${destinations.size})",
                onEdit = onEditDestinations
            ) {
                destinations.forEach { dest ->
                    DestinationPreviewChip(dest)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            PreviewSection(
                title = "Bình chọn",
                onEdit = onEditDestinations
            ) {
                PreviewItem("Chọn nhiều", if (allowMultipleVotes) "Có" else "Không")
                if (votingEndsAt > 0) {
                    PreviewItem("Hạn chót", formatDateTime(votingEndsAt))
                }
            }
        }
    }
}

@Composable
private fun PreviewSection(
    title: String,
    onEdit: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B35)
            )
            TextButton(onClick = onEdit) {
                Text("Sửa")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun PreviewItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.6f)
        )
    }
}

private fun formatDate(timestamp: Long): String {
    if (timestamp == 0L) return "Chưa chọn"
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("vi"))
    return sdf.format(Date(timestamp))
}

private fun formatDateTime(timestamp: Long): String {
    if (timestamp == 0L) return "Chưa chọn"
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("vi"))
    return sdf.format(Date(timestamp))
}
