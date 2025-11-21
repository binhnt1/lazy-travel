package com.lazytravel.ui.components.molecules

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.ui.theme.AppSpacing
import com.lazytravel.ui.theme.AppTypography

/**
 * Logo Component - Brand identity
 * Icon (✈️) + Text ("Lazy Travel")
 * Based on home_noauth.html .logo
 */
@Composable
fun Logo(
    modifier: Modifier = Modifier,
    iconSize: Int = 24,
    textColor: Color = Color.White,
    showText: Boolean = true
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // Logo Icon
        Text(
            text = "✈️",
            fontSize = iconSize.sp
        )

        // Logo Text
        if (showText) {
            Text(
                text = "Lazy Travel",
                style = AppTypography.LogoText,
                color = textColor
            )
        }
    }
}

/**
 * Logo Compact - Just the icon
 */
@Composable
fun LogoCompact(
    modifier: Modifier = Modifier,
    size: Int = 32
) {
    Text(
        text = "✈️",
        fontSize = size.sp,
        modifier = modifier
    )
}
