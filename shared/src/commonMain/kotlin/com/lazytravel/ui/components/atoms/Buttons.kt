package com.lazytravel.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.ui.theme.AppSpacing
import com.lazytravel.ui.theme.AppTypography

/**
 * Primary Button - Used for main CTAs
 * White background with orange text
 * Based on home_noauth.html .cta-button
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: String? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .heightIn(min = 48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = AppColors.Primary,
            disabledContainerColor = Color.White.copy(alpha = 0.6f),
            disabledContentColor = AppColors.Primary.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(AppSpacing.radiusMedium),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = AppSpacing.lg, vertical = AppSpacing.xs)
        ) {
            Text(
                text = text,
                style = AppTypography.ButtonLarge
            )
            icon?.let {
                Text(
                    text = it,
                    style = AppTypography.ButtonLarge
                )
            }
        }
    }
}

/**
 * Secondary Button - Used for login, secondary actions
 * Transparent background with white border and text
 * Based on home_noauth.html .login-btn
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: String? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .heightIn(min = 40.dp)
            .border(
                width = 1.dp,
                color = if (enabled) Color.White else Color.White.copy(alpha = 0.4f),
                shape = RoundedCornerShape(AppSpacing.radiusMedium)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.2f),
            contentColor = Color.White,
            disabledContainerColor = Color.White.copy(alpha = 0.1f),
            disabledContentColor = Color.White.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(AppSpacing.radiusMedium),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs)
        ) {
            icon?.let {
                Text(
                    text = it,
                    style = AppTypography.ButtonSmall
                )
            }
            Text(
                text = text,
                style = AppTypography.ButtonSmall
            )
        }
    }
}

/**
 * Gradient Button - Primary button with gradient background
 * Orange to yellow gradient
 */
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: String? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .heightIn(min = 48.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        AppColors.PrimaryGradientStart,
                        AppColors.PrimaryGradientEnd
                    )
                ),
                shape = RoundedCornerShape(AppSpacing.radiusMedium)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.White.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(AppSpacing.radiusMedium),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = AppSpacing.lg, vertical = AppSpacing.xs)
        ) {
            Text(
                text = text,
                style = AppTypography.ButtonLarge
            )
            icon?.let {
                Text(
                    text = it,
                    style = AppTypography.ButtonLarge
                )
            }
        }
    }
}

/**
 * Small Button - Used for compact actions like "Join", "View more"
 */
@Composable
fun SmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = AppColors.Primary,
    contentColor: Color = Color.White,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .heightIn(min = 36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.6f),
            disabledContentColor = contentColor.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(AppSpacing.radiusSmall),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        contentPadding = PaddingValues(horizontal = AppSpacing.md, vertical = AppSpacing.sm)
    ) {
        Text(
            text = text,
            style = AppTypography.ButtonSmall,
            textAlign = TextAlign.Center
        )
    }
}
