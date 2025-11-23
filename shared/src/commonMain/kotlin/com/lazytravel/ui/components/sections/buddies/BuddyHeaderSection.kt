package com.lazytravel.ui.components.sections.buddies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.localizedString

@Composable
fun BuddyHeaderSection(
    onNavigateBack: () -> Unit,
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Main Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(8.dp)
                    )
                    .then(
                        Modifier.background(
                            Color(0xFFE0E0E0).copy(alpha = 0.3f),
                            RoundedCornerShape(8.dp)
                        )
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF212121),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Title
            Text(
                text = localizedString("buddy_screen_title"),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            // Create Trip Button
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B35)
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = localizedString("buddy_create_trip"),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        // Search Bar
        Spacer(modifier = Modifier.height(12.dp))

        SearchBarComponent(
            onSearchClick = onSearchClick,
            onFilterClick = onFilterClick
        )
    }
}

@Composable
private fun SearchBarComponent(
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .background(
                Color(0xFFF5F5F5),
                RoundedCornerShape(8.dp)
            )
            .clickable { onSearchClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color(0xFF888888),
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = localizedString("buddy_search_placeholder"),
            color = Color(0xFF888888),
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )

        IconButton(
            onClick = onFilterClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = Color(0xFF888888),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

