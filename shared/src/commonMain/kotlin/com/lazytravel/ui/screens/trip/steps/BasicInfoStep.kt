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
import com.lazytravel.ui.components.atoms.trip.EmojiPicker
import com.lazytravel.ui.components.atoms.trip.RegionChip
import com.lazytravel.ui.components.atoms.trip.TagSelector
import com.lazytravel.ui.components.cards.trip.DateDurationPicker
import com.lazytravel.ui.components.cards.trip.TripImagePicker

@Composable
fun BasicInfoStep(
    tripTitle: String,
    onTripTitleChange: (String) -> Unit,
    emoji: String,
    onEmojiChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    imageUrl: String,
    onImageUrlChange: (String) -> Unit,
    startDate: Long,
    onStartDateChange: (Long) -> Unit,
    duration: Int,
    onDurationChange: (Int) -> Unit,
    region: String,
    onRegionChange: (String) -> Unit,
    tags: List<String>,
    onTagsChange: (List<String>) -> Unit,
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
            text = "Thông tin cơ bản",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF6B35)
        )

        // Trip Title
        OutlinedTextField(
            value = tripTitle,
            onValueChange = onTripTitleChange,
            label = { Text("Tên hành trình *") },
            placeholder = { Text("VD: Khám phá Phú Quốc cùng nhau") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = validationErrors.containsKey("tripTitle"),
            supportingText = {
                validationErrors["tripTitle"]?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        // Emoji Picker
        Column {
            Text(
                text = "Chọn biểu tượng *",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            EmojiPicker(
                selected = emoji,
                onSelect = onEmojiChange
            )
        }

        // Description
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Mô tả hành trình *") },
            placeholder = { Text("Chia sẻ ý tưởng và kế hoạch của bạn...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            maxLines = 6,
            isError = validationErrors.containsKey("description"),
            supportingText = {
                validationErrors["description"]?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        // Image Picker
        TripImagePicker(
            imageUrl = imageUrl,
            onImageUrlChange = onImageUrlChange
        )

        Divider()

        // Date & Duration
        DateDurationPicker(
            startDate = startDate,
            duration = duration,
            onStartDateChange = onStartDateChange,
            onDurationChange = onDurationChange
        )

        if (validationErrors.containsKey("startDate")) {
            Text(
                text = validationErrors["startDate"]!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Divider()

        // Region
        Column {
            Text(
                text = "Vùng miền *",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            RegionChip(
                selected = region,
                onSelect = onRegionChange
            )
            if (validationErrors.containsKey("region")) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = validationErrors["region"]!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Divider()

        // Tags
        Column {
            Text(
                text = "Tags (tùy chọn)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Chọn tối đa 5 tags",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            TagSelector(
                selectedTags = tags,
                onTagsChange = onTagsChange
            )
        }

        // Bottom spacing for scroll
        Spacer(modifier = Modifier.height(80.dp))
    }
}
