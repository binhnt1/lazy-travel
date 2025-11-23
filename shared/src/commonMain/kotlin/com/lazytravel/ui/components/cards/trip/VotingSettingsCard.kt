package com.lazytravel.ui.components.cards.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotingSettingsCard(
    allowMultipleVotes: Boolean,
    votingEndsAt: Long,
    onAllowMultipleVotesChange: (Boolean) -> Unit,
    onVotingEndsAtChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = votingEndsAt)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFAF5)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Cài đặt bình chọn",
                style = MaterialTheme.typography.titleMedium
            )

            Divider()

            // Allow Multiple Votes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Cho phép chọn nhiều địa điểm",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Thành viên có thể vote cho nhiều destinations",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Switch(
                    checked = allowMultipleVotes,
                    onCheckedChange = onAllowMultipleVotesChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFF6B35)
                    )
                )
            }

            Divider()

            // Voting Deadline
            Column {
                Text(
                    text = "Hạn chót bình chọn",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedCard(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFFFF6B35)
                        )
                        Text(
                            text = if (votingEndsAt > 0) {
                                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("vi"))
                                    .format(Date(votingEndsAt))
                            } else {
                                "Chọn thời hạn"
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Text(
                    text = "Nên đặt hạn chót trước ngày khởi hành ít nhất 3-7 ngày",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onVotingEndsAtChange(it) }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Huỷ")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
