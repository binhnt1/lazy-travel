package com.lazytravel.ui.components.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.ui.components.atoms.LanguageDropdown
import com.lazytravel.ui.components.atoms.PrimaryButton
import com.lazytravel.ui.components.atoms.SecondaryButton
import com.lazytravel.ui.components.molecules.Logo
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.ui.theme.AppSpacing
import com.lazytravel.ui.theme.AppTypography
import com.lazytravel.core.i18n.localizedString

/**
 * Hero Section - Landing section with gradient background
 * Based on home_noauth.html .hero
 *
 * Contains:
 * - Logo + Login button
 * - Hero title & subtitle
 * - CTA button
 * - Decorative pattern
 */
@Composable
fun HeroSection(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        AppColors.PrimaryGradientStart, // #FF6B35
                        AppColors.PrimaryGradientEnd    // #F7931E
                    )
                )
            )
            .padding(
                start = AppSpacing.lg,
                end = AppSpacing.lg,
                top = AppSpacing.xl,
                bottom = AppSpacing.huge
            )
    ) {
        // Decorative pattern (airplane emoji in background)
        Text(
            text = "✈️",
            fontSize = 180.sp,
            color = Color.White.copy(alpha = 0.15f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = (-20).dp)
        )

        // Main content
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Navigation - Logo + Login button
            HeroNavigation(
                onLoginClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppSpacing.xxxl)
            )

            // Hero Content - Title, Subtitle, CTA
            HeroContent(
                onSignupClick = onSignupClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Hero Navigation - Top bar with Logo and Login button
 */
@Composable
private fun HeroNavigation(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Top row with Logo and Login button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            Logo(
                iconSize = 24,
                textColor = Color.White
            )

            // Login Button
            SecondaryButton(
                text = localizedString("login"),
                onClick = onLoginClick
            )
        }

        // Language Dropdown - placed below to avoid iOS dynamic island
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppSpacing.md),
            horizontalArrangement = Arrangement.End
        ) {
            LanguageDropdown(
                textColor = Color.White,
                backgroundColor = Color.White.copy(alpha = 0.2f)
            )
        }
    }
}

/**
 * Hero Content - Main message and CTA
 */
@Composable
private fun HeroContent(
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
    ) {
        // Hero Title
        Text(
            text = localizedString("hero_title"),
            style = AppTypography.HeroTitle,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 38.sp
        )

        // Hero Subtitle
        Text(
            text = localizedString("hero_subtitle"),
            style = AppTypography.HeroSubtitle,
            color = Color.White.copy(alpha = 0.95f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = AppSpacing.md)
        )

        // CTA Button
        PrimaryButton(
            text = localizedString("get_started_free"),
            onClick = onSignupClick,
            modifier = Modifier.padding(top = AppSpacing.sm)
        )
    }
}
