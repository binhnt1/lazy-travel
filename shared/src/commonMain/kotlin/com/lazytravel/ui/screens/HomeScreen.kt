package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.ui.theme.AppColors

@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToTrips: () -> Unit = {},
    onNavigateToFeed: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "✈️",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Chào mừng đến với LazyTravel!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Màn hình chính đang được phát triển...",
                fontSize = 16.sp,
                color = Color(0xFF666666)
            )
        }
    }
}
