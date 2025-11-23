package com.lazytravel.ui.components.sections.tours

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.ui.theme.AppColors
import org.jetbrains.compose.resources.painterResource
import lazytravel.shared.generated.resources.Res
import lazytravel.shared.generated.resources.ic_search
import lazytravel.shared.generated.resources.ic_filter

@Composable
fun TourHeaderSection(
    onSearchChanged: (String) -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top row: Logo and icons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "✈️",
                    fontSize = 32.sp
                )
                Text(
                    text = "TravelGo",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary,
                    fontSize = 18.sp
                )
            }

            // Icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Notification icon with badge
                Box {
                    Surface(
                        onClick = onNotificationClick,
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🔔",
                                fontSize = 16.sp
                            )
                        }
                    }

                    // Badge dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(8.dp)
                            .background(Color(0xFFE53935), shape = RoundedCornerShape(50))
                    )
                }

                // Message icon
                Surface(
                    onClick = onMessageClick,
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "💬",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search box
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF5F5F5),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Search icon
                Icon(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = "Search",
                    tint = Color(0xFF888888),
                    modifier = Modifier.size(16.dp)
                )

                // Search input
                BasicTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        onSearchChanged(it)
                    },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF333333)
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Tìm tour, điểm đến, công ty...",
                                fontSize = 14.sp,
                                color = Color(0xFF999999)
                            )
                        }
                        innerTextField()
                    }
                )

                // Filter icon
                IconButton(
                    onClick = onFilterClick,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_filter),
                        contentDescription = "Filter",
                        tint = Color(0xFF888888),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
