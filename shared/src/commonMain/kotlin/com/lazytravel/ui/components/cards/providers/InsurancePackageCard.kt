package com.lazytravel.ui.components.cards.providers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.models.InsurancePackage

@Composable
fun InsurancePackageCard(
    insurancePackage: InsurancePackage,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isFeatured = insurancePackage.badges?.contains(InsurancePackage.BADGE_POPULAR) == true ||
                     insurancePackage.badges?.contains(InsurancePackage.BADGE_HOT) == true

    val borderColor = if (isFeatured) Color(0xFF1976D2) else Color(0xFFE3F2FD)
    val backgroundColor = if (isFeatured) {
        Brush.linearGradient(
            colors = listOf(Color(0xFFE3F2FD), Color.White)
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color.White, Color.White)
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        // Popular badge on top
        if (isFeatured) {
            Text(
                text = insurancePackage.badges?.firstOrNull() ?: "PHỔ BIẾN",
                modifier = Modifier
                    .offset(y = (-6).dp)
                    .background(
                        color = Color(0xFFFF5252),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Card(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isFeatured) 8.dp else 0.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Package name
                Text(
                    text = insurancePackage.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center
                )

                // Coverage amount
                Text(
                    text = formatCoverage(insurancePackage.coverage),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    textAlign = TextAlign.Center
                )

                // Price per day
                Text(
                    text = formatPricePerDay(insurancePackage.pricePerDay),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun formatCoverage(coverage: Double): String {
    return when {
        coverage >= 1_000_000_000 -> {
            val billions = coverage / 1_000_000_000
            "${billions.toInt()} tỷ đ"
        }
        coverage >= 1_000_000 -> {
            val millions = coverage / 1_000_000
            "${millions.toInt()} triệu đ"
        }
        else -> {
            "${coverage.toInt().toString().reversed().chunked(3).joinToString(".").reversed()}đ"
        }
    }
}

private fun formatPricePerDay(price: Double): String {
    return when {
        price >= 1_000_000 -> {
            val millions = price / 1_000_000
            "${millions.toInt()}tr/ngày"
        }
        price >= 1_000 -> {
            val thousands = price / 1_000
            "${thousands.toInt()}k/ngày"
        }
        else -> {
            "${price.toInt()}đ/ngày"
        }
    }
}
