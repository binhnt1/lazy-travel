package com.lazytravel.ui.screens.trip.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazytravel.ui.components.cards.trip.DestinationInputCard
import com.lazytravel.ui.components.cards.trip.VotingSettingsCard
import com.lazytravel.ui.screens.trip.DestinationInput

@Composable
fun DestinationsStep(
    destinations: List<DestinationInput>,
    onAddDestination: () -> Unit,
    onUpdateDestination: (Int, DestinationInput) -> Unit,
    onDeleteDestination: (Int) -> Unit,
    allowMultipleVotes: Boolean,
    onAllowMultipleVotesChange: (Boolean) -> Unit,
    votingEndsAt: Long,
    onVotingEndsAtChange: (Long) -> Unit,
    validationErrors: Map<String, String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item {
            Text(
                text = "Địa điểm đề xuất",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B35)
            )
        }

        // Info Card
        item {
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
                        text = "🗺️ Cách hoạt động",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B35)
                    )
                    Text(
                        text = "• Đề xuất ít nhất 2 địa điểm để thành viên bình chọn\n" +
                                "• Thành viên sẽ vote cho địa điểm yêu thích\n" +
                                "• Địa điểm có nhiều vote nhất sẽ được chọn\n" +
                                "• Sau khi chốt địa điểm, hành trình chuyển sang giai đoạn Schedule",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        // Destination Cards
        if (destinations.isNotEmpty()) {
            itemsIndexed(
                items = destinations,
                key = { _, dest -> dest.id }
            ) { index, destination ->
                DestinationInputCard(
                    destination = destination,
                    onUpdate = { updated -> onUpdateDestination(index, updated) },
                    onDelete = { onDeleteDestination(index) }
                )
            }
        }

        // Validation Error
        if (validationErrors.containsKey("destinations")) {
            item {
                Text(
                    text = validationErrors["destinations"]!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Add Destination Button
        item {
            OutlinedButton(
                onClick = onAddDestination,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFFF6B35)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Thêm địa điểm")
            }
        }

        item {
            HorizontalDivider()
        }

        // Voting Settings
        item {
            Text(
                text = "Cài đặt bình chọn",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            VotingSettingsCard(
                allowMultipleVotes = allowMultipleVotes,
                votingEndsAt = votingEndsAt,
                onAllowMultipleVotesChange = onAllowMultipleVotesChange,
                onVotingEndsAtChange = onVotingEndsAtChange
            )
        }

        if (validationErrors.containsKey("votingEndsAt")) {
            item {
                Text(
                    text = validationErrors["votingEndsAt"]!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
