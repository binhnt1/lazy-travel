package com.lazytravel.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Design Tokens - Typography for LazyTravel App
 * Based on home_noauth.html design
 */
object AppTypography {
    // Hero Section
    val HeroTitle = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 38.sp
    )

    val HeroSubtitle = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp
    )

    // Section Headers
    val SectionTitle = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 28.sp
    )

    val SectionSubtitle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp
    )

    val SectionTag = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 14.sp
    )

    // Body Text
    val BodyLarge = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp
    )

    val BodyMedium = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    )

    val BodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp
    )

    // Captions
    val Caption = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 14.sp
    )

    val CaptionSmall = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 13.sp
    )

    val CaptionTiny = TextStyle(
        fontSize = 9.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 12.sp
    )

    // Buttons
    val ButtonLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 20.sp
    )

    val ButtonMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 18.sp
    )

    val ButtonSmall = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 16.sp
    )

    // Cards
    val CardTitle = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 19.sp
    )

    val CardSubtitle = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 17.sp
    )

    // Stats
    val StatNumber = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    )

    val StatLabel = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 14.sp
    )

    // Logo
    val LogoText = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 22.sp
    )
}
