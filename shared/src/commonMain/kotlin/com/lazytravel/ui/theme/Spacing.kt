package com.lazytravel.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Design Tokens - Spacing for LazyTravel App
 * Based on home_noauth.html design
 */
object AppSpacing {
    // Base spacing scale
    val none = 0.dp
    val xxs = 2.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
    val xxl = 24.dp
    val xxxl = 32.dp
    val huge = 40.dp

    // Specific component spacing
    val containerPadding = lg // 16dp
    val cardPadding = lg // 16dp
    val sectionSpacing = lg // 16dp
    val itemSpacing = md // 12dp

    // Border radius
    val radiusSmall = 6.dp
    val radiusMedium = 8.dp
    val radiusLarge = 12.dp
    val radiusFull = 999.dp // For circular shapes
}
