package com.lazytravel.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.Language
import com.lazytravel.core.i18n.LocalizationManager

/**
 * Language Dropdown - Combobox for language selection
 *
 * A dropdown menu that allows users to select between available languages.
 * Shows current language and expands to show all options.
 */
@Composable
fun LanguageDropdown(
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
    backgroundColor: Color = Color.White.copy(alpha = 0.2f)
) {
    val currentLanguage by LocalizationManager.currentLanguage.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // Dropdown Button
        Surface(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(4.dp),
            shape = RoundedCornerShape(8.dp),
            color = backgroundColor
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = currentLanguage.code.uppercase(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Language",
                    tint = textColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            Language.entries.forEach { language ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = language.code.uppercase(),
                                fontSize = 13.sp,
                                fontWeight = if (language == currentLanguage) FontWeight.Bold else FontWeight.Normal,
                                color = if (language == currentLanguage) Color(0xFFFF6B35) else Color(0xFF1A1A1A)
                            )
                            Text(
                                text = language.displayName,
                                fontSize = 13.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    },
                    onClick = {
                        LocalizationManager.setLanguage(language)
                        expanded = false
                    }
                )
            }
        }
    }
}
