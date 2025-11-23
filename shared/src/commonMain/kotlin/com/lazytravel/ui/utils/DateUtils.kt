package com.lazytravel.ui.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime as toLocalDateTimeKotlin
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Format timestamp to readable date string
 * Example: 1234567890000 -> "15 Th2, 2009"
 */
@OptIn(ExperimentalTime::class)
fun formatDateFromTimestamp(timestamp: Long): String {
    val instant = Instant.fromEpochMilliseconds(timestamp)
    val localDateTime = instant.toLocalDateTimeKotlin(TimeZone.currentSystemDefault())

    val day = localDateTime.date.day
    val month = getVietnameseMonth(localDateTime.date.month.ordinal + 1)
    val year = localDateTime.date.year

    return "$day $month, $year"
}

/**
 * Format timestamp to short date string
 * Example: 1234567890000 -> "15/02/2009"
 */
@OptIn(ExperimentalTime::class)
fun formatShortDate(timestamp: Long): String {
    val instant = Instant.fromEpochMilliseconds(timestamp)
    val localDateTime = instant.toLocalDateTimeKotlin(TimeZone.currentSystemDefault())

    val day = localDateTime.date.day.toString().padStart(2, '0')
    val month = (localDateTime.date.month.ordinal + 1).toString().padStart(2, '0')
    val year = localDateTime.date.year

    return "$day/$month/$year"
}

/**
 * Get Vietnamese month abbreviation
 */
private fun getVietnameseMonth(monthNumber: Int): String {
    return when (monthNumber) {
        1 -> "Th1"
        2 -> "Th2"
        3 -> "Th3"
        4 -> "Th4"
        5 -> "Th5"
        6 -> "Th6"
        7 -> "Th7"
        8 -> "Th8"
        9 -> "Th9"
        10 -> "Th10"
        11 -> "Th11"
        12 -> "Th12"
        else -> "Th$monthNumber"
    }
}

/**
 * Get current timestamp in milliseconds
 */
expect fun getCurrentTimeMillis(): Long

