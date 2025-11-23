package com.lazytravel.helpers

import androidx.compose.ui.graphics.Color
import com.lazytravel.data.models.Buddy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.time.ExperimentalTime

/**
 * Extracts text content from JSON string
 */
fun extractTextContent(jsonContent: String): String {
    return try {
        val json = Json.parseToJsonElement(jsonContent)
        json.jsonObject["text"]?.jsonPrimitive?.content ?: ""
    } catch (_: Exception) {
        jsonContent
    }
}

/**
 * Extracts image URL from JSON content
 */
fun extractImageUrl(jsonContent: String): String? {
    return try {
        val json = Json.parseToJsonElement(jsonContent)
        json.jsonObject["imageUrl"]?.jsonPrimitive?.content
    } catch (_: Exception) {
        null
    }
}

/**
 * Extracts video URL from JSON content
 */
fun extractVideoUrl(jsonContent: String): String? {
    return try {
        val json = Json.parseToJsonElement(jsonContent)
        json.jsonObject["videoUrl"]?.jsonPrimitive?.content
    } catch (_: Exception) {
        null
    }
}

/**
 * Extracts thumbnail URL from JSON content
 */
fun extractThumbnailUrl(jsonContent: String): String? {
    return try {
        val json = Json.parseToJsonElement(jsonContent)
        json.jsonObject["thumbnailUrl"]?.jsonPrimitive?.content
    } catch (_: Exception) {
        null
    }
}

/**
 * Extracts album images from JSON content
 */
fun extractAlbumImages(jsonContent: String): List<String> {
    return try {
        val json = Json.parseToJsonElement(jsonContent)
        json.jsonObject["images"]?.jsonArray?.map {
            it.jsonPrimitive.content
        } ?: emptyList()
    } catch (_: Exception) {
        emptyList()
    }
}

/**
 * Calculates time ago from timestamp
 */
@OptIn(ExperimentalTime::class)
fun calculateTimeAgo(timestamp: Long?): String {
    if (timestamp == null || timestamp == 0L) return "Vừa xong"

    return try {
        // Convert timestamp (seconds) to Instant
        val instant = kotlin.time.Instant.fromEpochSeconds(timestamp)
        val now = kotlin.time.Clock.System.now()
        val duration = now - instant

        when {
            duration.inWholeMinutes < 1 -> "Vừa xong"
            duration.inWholeMinutes < 60 -> "${duration.inWholeMinutes} phút trước"
            duration.inWholeHours < 24 -> "${duration.inWholeHours} giờ trước"
            duration.inWholeDays < 7 -> "${duration.inWholeDays} ngày trước"
            duration.inWholeDays < 30 -> "${duration.inWholeDays / 7} tuần trước"
            duration.inWholeDays < 365 -> "${duration.inWholeDays / 30} tháng trước"
            else -> "${duration.inWholeDays / 365} năm trước"
        }
    } catch (e: Exception) {
        println("⚠️ Error parsing timestamp: $timestamp - ${e.message}")
        "Vừa xong"
    }
}

/**
 * Custom format function for Double to one decimal place
 */
private fun formatOneDecimal(value: Double): String {
    val rounded = (value * 10).toInt() / 10.0
    return if (rounded == rounded.toInt().toDouble()) {
        rounded.toInt().toString()
    } else {
        rounded.toString()
    }
}

/**
 * Format number to short string (e.g., 1.2K, 3.5M)
 */
fun formatNumber(count: Int): String {
    return when {
        count >= 1_000_000 -> {
            val value = count / 1_000_000.0
            formatOneDecimal(value) + "M"
        }
        count >= 1_000 -> {
            val value = count / 1_000.0
            formatOneDecimal(value) + "K"
        }
        else -> count.toString()
    }
}

/**
 * Format duration in seconds to MM:SS or HH:MM:SS
 */
@Suppress("unused")
fun formatDuration(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return if (hours > 0) {
        "$hours:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
    } else {
        "$minutes:${secs.toString().padStart(2, '0')}"
    }
}

/**
 * Truncate text with ellipsis
 */
@Suppress("unused")
fun truncateText(text: String, maxLength: Int): String {
    return if (text.length <= maxLength) {
        text
    } else {
        text.take(maxLength - 3) + "..."
    }
}


@Suppress("unused")
fun formatDate(date: String): String {
    return date.take(10)
}

@Suppress("unused")
fun formatBudget(budget: Long): String {
    return when {
        budget >= 1_000_000 -> "${budget / 1_000_000}tr"
        else -> "${budget / 1_000}k"
    }
}

@Suppress("unused")
fun getRandomColor(): Color {
    val colors = listOf(
        Color(0xFF667EEA),
        Color(0xFF764BA2),
        Color(0xFFFF6B35),
        Color(0xFF4FACFE),
        Color(0xFFE91E63),
        Color(0xFF00F2FE)
    )
    return colors.random()
}

@Suppress("unused")
fun getStatusColor(buddy: Buddy): Color {
    return when {
        buddy.status == "FULL" -> Color(0xFFF44336) // Red - Full
        buddy.status == "URGENT" -> Color(0xFFFF9800) // Orange - Urgent
        buddy.verified -> Color(0xFF2196F3) // Blue - Verified
        else -> Color(0xFF4CAF50) // Green - Available
    }
}

@Suppress("unused")
fun formatDateFromTimestamp(timestamp: Long): String {
    // Convert milliseconds to seconds
    val seconds = timestamp / 1000

    // Tính ngày/tháng/năm
    val totalDays = seconds / 86400

    // Số ngày trong mỗi tháng (không phải năm nhuận)
    val daysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    // Tìm năm
    var year = 1970
    var daysLeft = totalDays

    while (true) {
        val daysInYear = if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 366 else 365
        if (daysLeft < daysInYear) break
        daysLeft -= daysInYear
        year++
    }

    // Cập nhật array ngày trong tháng nếu là năm nhuận
    if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
        daysInMonth[1] = 29
    }

    // Tìm tháng
    var month = 1
    while (daysLeft >= daysInMonth[month - 1]) {
        daysLeft -= daysInMonth[month - 1]
        month++
    }

    // Day là daysLeft + 1
    val day = daysLeft + 1

    // Format: DD/MM/YYYY
    val dayStr = if (day < 10) "0$day" else "$day"
    val monthStr = if (month < 10) "0$month" else "$month"
    return "$dayStr/$monthStr/$year"
}

@Suppress("unused")
fun formatCurrency(value: Double): String {
    return when {
        value >= 1.0 -> "${value.toInt()}tr"
        value >= 0.1 -> "${(value * 10).toInt()}00k"
        else -> "${(value * 1000).toInt()}k"
    }
}

@Suppress("unused")
fun formatTimeAgo(timestamp: Long): String {
    return calculateTimeAgo(timestamp)
}



