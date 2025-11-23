package com.lazytravel.ui.components.sections.providers

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.lazytravel.ui.components.cards.providers.VisaCard

data class CountryVisa(val flag: String, val name: String)
data class VisaStat(val value: String, val label: String)
data class VisaStep(val number: String, val text: String)

@Composable
fun VisaProviderSection(
    onCountryClick: (String) -> Unit = {},
    onApplyClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val greenColor = Color(0xFF4CAF50)
    val lightGreen = Color(0xFFF1F8E9)

    val countries = listOf(
        CountryVisa("🇯🇵", "Nhật Bản"),
        CountryVisa("🇰🇷", "Hàn Quốc"),
        CountryVisa("🇺🇸", "Mỹ"),
        CountryVisa("🇦🇺", "Úc"),
        CountryVisa("🇨🇦", "Canada"),
        CountryVisa("🇬🇧", "Anh"),
        CountryVisa("🇩🇪", "Đức"),
        CountryVisa("🇫🇷", "Pháp")
    )

    val stats = listOf(
        VisaStat("50+", "QUỐC GIA"),
        VisaStat("98%", "TỶ LỆ ĐẬU"),
        VisaStat("3-5", "NGÀY LÀM")
    )

    val steps = listOf(
        VisaStep("1", "Gửi hồ sơ"),
        VisaStep("2", "Kiểm tra"),
        VisaStep("3", "Nộp hồ sơ"),
        VisaStep("4", "Nhận visa")
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Provider header - centered
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Circular logo
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(greenColor, Color(0xFF66BB6A))
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🛂",
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Vietnam Visa Express",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Làm visa nhanh chóng - Tỷ lệ đậu cao",
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats boxes (3 columns)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stats.forEach { stat ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(lightGreen, Color(0xFFDCEDC8))
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stat.value,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stat.label,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF558B2F),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Countries section
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "🌏", fontSize = 12.sp)
                    Text(
                        text = "Các quốc gia phổ biến",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Countries grid (4 columns)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false
                ) {
                    items(countries) { country ->
                        VisaCard(
                            flagEmoji = country.flag,
                            countryName = country.name,
                            onClick = { onCountryClick(country.name) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Visa process steps
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFE8F5E9), lightGreen)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "📋", fontSize = 12.sp)
                    Text(
                        text = "Quy trình làm visa",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    steps.forEachIndexed { index, step ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(greenColor, Color(0xFF66BB6A))
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = step.number,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = step.text,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF558B2F),
                                textAlign = TextAlign.Center
                            )
                        }

                        if (index < steps.size - 1) {
                            Text(
                                text = "→",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF81C784),
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .width(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CTA footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(greenColor, Color(0xFF43A047))
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "✅", fontSize = 16.sp)
                    Column {
                        Text(
                            text = "Cam kết đậu visa",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Hoàn tiền 100% nếu rớt",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                Button(
                    onClick = onApplyClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Nộp hồ sơ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = greenColor
                    )
                }
            }
        }
    }
}
