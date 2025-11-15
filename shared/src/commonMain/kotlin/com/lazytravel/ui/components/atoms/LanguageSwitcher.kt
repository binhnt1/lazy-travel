package com.lazytravel.ui.components.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.Language
import com.lazytravel.core.i18n.LocalizationManager

/**
 * Language Switcher - Atom
 *
 * Allows users to switch between English and Vietnamese
 * Displays: EN | VI
 * Active language is bold, inactive is semi-transparent
 */
@Composable
fun LanguageSwitcher(
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
) {
    val currentLanguage by LocalizationManager.currentLanguage.collectAsState()

    Row(modifier = modifier) {
        // English
        Text(
            text = "EN",
            fontSize = 13.sp,
            fontWeight = if (currentLanguage == Language.ENGLISH) FontWeight.Bold else FontWeight.Normal,
            color = textColor.copy(alpha = if (currentLanguage == Language.ENGLISH) 1f else 0.6f),
            modifier = Modifier
                .clickable { LocalizationManager.setLanguage(Language.ENGLISH) }
                .padding(horizontal = 6.dp, vertical = 4.dp)
        )

        // Separator
        Text(
            text = "|",
            fontSize = 13.sp,
            color = textColor.copy(alpha = 0.4f),
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        // Vietnamese
        Text(
            text = "VI",
            fontSize = 13.sp,
            fontWeight = if (currentLanguage == Language.VIETNAMESE) FontWeight.Bold else FontWeight.Normal,
            color = textColor.copy(alpha = if (currentLanguage == Language.VIETNAMESE) 1f else 0.6f),
            modifier = Modifier
                .clickable { LocalizationManager.setLanguage(Language.VIETNAMESE) }
                .padding(horizontal = 6.dp, vertical = 4.dp)
        )
    }
}
