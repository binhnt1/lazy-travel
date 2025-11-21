package com.lazytravel.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * HeaderBar Component - Atomic Design (Layout)
 *
 * A flexible header component with multiple variants for different screens.
 * Based on the design patterns from HTML prototypes.
 */

// ============================================================================
// Data Classes
// ============================================================================

/**
 * Represents different types of headers used throughout the app
 */
sealed class HeaderType {
    /**
     * Hero header for landing page with logo and login button
     */
    data class Hero(
        val logoText: String = "LazyTravel",
        val onLoginClick: () -> Unit = {}
    ) : HeaderType()

    /**
     * Greeting header for authenticated home screen
     */
    data class Greeting(
        val userName: String,
        val subtitle: String = "Sẵn sàng cho chuyến phiêu lưu tiếp theo?"
    ) : HeaderType()

    /**
     * Navigation header with back button and title
     */
    data class Navigation(
        val title: String,
        val subtitle: String? = null,
        val onBackClick: () -> Unit,
        val showMenu: Boolean = false,
        val onMenuClick: () -> Unit = {}
    ) : HeaderType()

    /**
     * Navigation header with trip info
     */
    data class TripNavigation(
        val tripTitle: String,
        val dateRange: String,
        val participantCount: Int,
        val onBackClick: () -> Unit
    ) : HeaderType()
}

// ============================================================================
// Main Component
// ============================================================================

/**
 * Main HeaderBar component that renders different header types
 */
@Composable
fun HeaderBar(
    type: HeaderType,
    modifier: Modifier = Modifier
) {
    when (type) {
        is HeaderType.Hero -> HeroHeaderBar(
            logoText = type.logoText,
            onLoginClick = type.onLoginClick,
            modifier = modifier
        )
        is HeaderType.Greeting -> GreetingHeaderBar(
            userName = type.userName,
            subtitle = type.subtitle,
            modifier = modifier
        )
        is HeaderType.Navigation -> NavigationHeaderBar(
            title = type.title,
            subtitle = type.subtitle,
            onBackClick = type.onBackClick,
            showMenu = type.showMenu,
            onMenuClick = type.onMenuClick,
            modifier = modifier
        )
        is HeaderType.TripNavigation -> TripNavigationHeaderBar(
            tripTitle = type.tripTitle,
            dateRange = type.dateRange,
            participantCount = type.participantCount,
            onBackClick = type.onBackClick,
            modifier = modifier
        )
    }
}

// ============================================================================
// Header Variants
// ============================================================================

/**
 * Hero header for landing page
 * Matches hero-nav from home_noauth.html
 */
@Composable
private fun HeroHeaderBar(
    logoText: String,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
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
                fontSize = 24.sp
            )
            Text(
                text = logoText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Login Button
        OutlinedButton(
            onClick = onLoginClick,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White.copy(alpha = 0.2f),
                contentColor = Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Đăng nhập",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Greeting header for authenticated home
 * Matches header from home.html
 */
@Composable
private fun GreetingHeaderBar(
    userName: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Xin chào, $userName! 👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = Color(0xFF757575)
        )
    }
}

/**
 * Navigation header with back button
 * Matches header from create_trip.html and other pages
 */
@Composable
private fun NavigationHeaderBar(
    title: String,
    subtitle: String?,
    onBackClick: () -> Unit,
    showMenu: Boolean,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(40.dp)
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                color = Color(0xFF212121)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Title & Subtitle
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            subtitle?.let {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
            }
        }

        // Optional Menu Button
        if (showMenu) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    text = "⋮",
                    fontSize = 24.sp,
                    color = Color(0xFF212121)
                )
            }
        }
    }
}

/**
 * Trip navigation header with trip details
 * Matches header from vote_detail.html and create_schedule.html
 */
@Composable
private fun TripNavigationHeaderBar(
    tripTitle: String,
    dateRange: String,
    participantCount: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Back button and trip title row
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    color = Color(0xFF212121)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = tripTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "📅 $dateRange",
                        fontSize = 12.sp,
                        color = Color(0xFF757575)
                    )
                    Text(
                        text = "👥 $participantCount người",
                        fontSize = 12.sp,
                        color = Color(0xFF757575)
                    )
                }
            }
        }
    }
}

// ============================================================================
// Preview/Usage Examples (for documentation)
// ============================================================================

/**
 * Usage Examples:
 *
 * // Hero header for landing page
 * HeaderBar(
 *     type = HeaderType.Hero(
 *         logoText = "LazyTravel",
 *         onLoginClick = { /* navigate to login */ }
 *     )
 * )
 *
 * // Greeting header for home
 * HeaderBar(
 *     type = HeaderType.Greeting(
 *         userName = "Minh",
 *         subtitle = "Sẵn sàng cho chuyến phiêu lưu tiếp theo?"
 *     )
 * )
 *
 * // Navigation header with back button
 * HeaderBar(
 *     type = HeaderType.Navigation(
 *         title = "Tạo Chuyến Đi Mới",
 *         subtitle = "Lên kế hoạch chi tiết cho chuyến đi",
 *         onBackClick = { /* navigate back */ }
 *     )
 * )
 *
 * // Trip navigation header
 * HeaderBar(
 *     type = HeaderType.TripNavigation(
 *         tripTitle = "Chuyến đi Nha Trang",
 *         dateRange = "15-18/12/2024",
 *         participantCount = 5,
 *         onBackClick = { /* navigate back */ }
 *     )
 * )
 */
