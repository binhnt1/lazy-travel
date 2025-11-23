package com.lazytravel.ui.screens.trip.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.lazytravel.data.models.enums.TripStatus
import com.lazytravel.ui.components.cards.trip.StatusRadioGroup
import com.lazytravel.ui.components.cards.trip.TripPreviewCard
import com.lazytravel.ui.screens.trip.DestinationInput

@Composable
fun PreviewPublishStep(
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
    publishStatus: String,
    onPublishStatusChange: (String) -> Unit,
    acceptTerms: Boolean,
    onAcceptTermsChange: (Boolean) -> Unit,
    onEditBasicInfo: () -> Unit,
    onEditBudget: () -> Unit,
    onEditDestinations: () -> Unit,
    validationErrors: Map<String, String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Title
        Text(
            text = "Xem trước & Xuất bản",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B35)
        )

        // Preview Card
        TripPreviewCard(
            tripTitle = tripTitle,
            emoji = emoji,
            description = description,
            startDate = startDate,
            duration = duration,
            region = region,
            tags = tags,
            budgetMin = budgetMin,
            budgetMax = budgetMax,
            maxParticipants = maxParticipants,
            ageRange = ageRange,
            requirements = requirements,
            destinations = destinations,
            allowMultipleVotes = allowMultipleVotes,
            votingEndsAt = votingEndsAt,
            onEditBasicInfo = onEditBasicInfo,
            onEditBudget = onEditBudget,
            onEditDestinations = onEditDestinations
        )

        Divider()

        // Publish Status
        Column {
            Text(
                text = "Trạng thái xuất bản",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            StatusRadioGroup(
                selectedStatus = publishStatus,
                onStatusChange = onPublishStatusChange
            )

            if (validationErrors.containsKey("publishStatus")) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = validationErrors["publishStatus"]!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Terms & Conditions (only show when publishing)
        if (publishStatus == TripStatus.INVITING.name) {
            Divider()

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF8F0)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚠️ Lưu ý khi xuất bản",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B35)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Hành trình sẽ hiển thị công khai cho mọi người\n" +
                                "• Người dùng khác có thể xem và tham gia vote\n" +
                                "• Bạn vẫn có thể chỉnh sửa sau khi xuất bản\n" +
                                "• Nên theo dõi và trả lời câu hỏi của thành viên",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Accept Terms Checkbox
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = acceptTerms,
                    onCheckedChange = onAcceptTermsChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFFF6B35)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Tôi đã đọc và đồng ý với ")
                        withStyle(style = SpanStyle(
                            color = Color(0xFFFF6B35),
                            fontWeight = FontWeight.Bold
                        )) {
                            append("Điều khoản sử dụng")
                        }
                        append(" và ")
                        withStyle(style = SpanStyle(
                            color = Color(0xFFFF6B35),
                            fontWeight = FontWeight.Bold
                        )) {
                            append("Chính sách bảo mật")
                        }
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (validationErrors.containsKey("acceptTerms")) {
                Text(
                    text = validationErrors["acceptTerms"]!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 48.dp)
                )
            }
        }

        // Bottom spacing for scroll
        Spacer(modifier = Modifier.height(80.dp))
    }
}
