package com.lazytravel.ui.utils

import androidx.compose.ui.graphics.Color

/**
 * Cross-platform color parsing function that converts hex color strings to Compose Color.
 * Supports both RGB (#RRGGBB) and ARGB (#AARRGGBB) formats.
 *
 * @param hex The hex color string (with or without # prefix)
 * @return Color object or null if parsing fails
 */
fun parseHexColor(hex: String): Color? {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorInt = cleanHex.toLong(16)

        if (cleanHex.length == 6) {
            // RGB format, add alpha
            Color((0xFF000000 or colorInt).toInt())
        } else if (cleanHex.length == 8) {
            // ARGB format
            Color(colorInt.toInt())
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}
