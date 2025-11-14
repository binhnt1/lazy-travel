package com.lazytravel.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Design Tokens - Colors for LazyTravel App
 * Based on home_noauth.html design
 */
object AppColors {
    // Primary Colors (Orange gradient)
    val Primary = Color(0xFFFF6B35)
    val PrimaryLight = Color(0xFFF7931E)
    val PrimaryGradientStart = Color(0xFFFF6B35)
    val PrimaryGradientEnd = Color(0xFFF7931E)

    // Secondary Colors (Purple gradient)
    val Secondary = Color(0xFF667EEA)
    val SecondaryLight = Color(0xFF764BA2)
    val SecondaryGradientStart = Color(0xFF667EEA)
    val SecondaryGradientEnd = Color(0xFF764BA2)

    // Background Colors
    val Background = Color(0xFFFAFAFA)
    val Surface = Color(0xFFFFFFFF)
    val CardBackground = Color(0xFFFFFFFF)

    // Border & Divider
    val Border = Color(0xFFE0E0E0)
    val Divider = Color(0xFFF0F0F0)

    // Text Colors
    val TextPrimary = Color(0xFF212121)
    val TextSecondary = Color(0xFF666666)
    val TextTertiary = Color(0xFF999999)
    val TextOnPrimary = Color.White

    // Tag & Badge Colors
    val TagBackground = Color(0xFFFFF3E0)
    val TagText = Color(0xFFFF6B35)

    // Status Colors
    val Success = Color(0xFF4CAF50)
    val SuccessLight = Color(0xFFE8F5E9)
    val Error = Color(0xFFF44336)
    val ErrorLight = Color(0xFFFFEBEE)
    val Warning = Color(0xFFFF9800)
    val WarningLight = Color(0xFFFFE0B2)
    val Info = Color(0xFF2196F3)

    // Destination Gradients
    val DestNhaTrang1 = Color(0xFFFF6B6B)
    val DestNhaTrang2 = Color(0xFFC44569)

    val DestDaLat1 = Color(0xFF4ECDC4)
    val DestDaLat2 = Color(0xFF44A08D)

    val DestSapa1 = Color(0xFF667EEA)
    val DestSapa2 = Color(0xFF764BA2)

    val DestHoiAn1 = Color(0xFFFF9800)
    val DestHoiAn2 = Color(0xFFF57C00)

    // Overlay
    val OverlayDark = Color(0x80000000) // rgba(0,0,0,0.5)
    val OverlayLight = Color(0x1AFFFFFF) // rgba(255,255,255,0.1)
}
