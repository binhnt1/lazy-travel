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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.InsurancePackage
import com.lazytravel.data.models.InsuranceProvider
import com.lazytravel.ui.components.cards.providers.InsurancePackageCard
import kotlinx.coroutines.launch

@Composable
fun InsuranceProviderSection(
    onPackageClick: (InsurancePackage) -> Unit = {},
    onBuyClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val providerRepo = remember { BaseRepository<InsuranceProvider>() }
    val packageRepo = remember { BaseRepository<InsurancePackage>() }

    var provider by remember { mutableStateOf<InsuranceProvider?>(null) }
    var packages by remember { mutableStateOf<List<InsurancePackage>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            // Fetch first insurance provider (Bảo Việt)
            providerRepo.getRecords<InsuranceProvider>(
                page = 1,
                perPage = 1,
                sort = "-created"
            ).fold(
                onSuccess = { providers ->
                    provider = providers.firstOrNull()

                    // Fetch packages for this provider
                    provider?.let { prov ->
                        packageRepo.getRecords<InsurancePackage>(
                            page = 1,
                            perPage = 10,
                            filter = "insuranceProviderId='${prov.id}'",
                            sort = "displayOrder"
                        ).fold(
                            onSuccess = { pkgs ->
                                packages = pkgs.take(3) // Show 3 packages
                                isLoading = false
                            },
                            onFailure = {
                                isLoading = false
                            }
                        )
                    }
                },
                onFailure = {
                    isLoading = false
                }
            )
        }
    }

    val blueColor = Color(0xFF1976D2)
    val lightBlue = Color(0xFFE3F2FD)

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
                .padding(16.dp)
        ) {
            // Provider header with gradient background
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(lightBlue, Color(0xFFF0F7FF))
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shield icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(blueColor, Color(0xFF1E88E5))
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🛡️",
                        fontSize = 28.sp
                    )
                }

                // Provider info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = blueColor
                    ) {
                        Text(
                            text = "Đối tác chính thức",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = provider?.name ?: "Bảo Việt Travel Insurance",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )

                    Text(
                        text = "Bảo vệ toàn diện mọi chuyến đi",
                        fontSize = 11.sp,
                        color = Color(0xFF666666)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Insurance packages grid (3 columns)
            if (packages.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false
                ) {
                    items(packages) { pkg ->
                        InsurancePackageCard(
                            insurancePackage = pkg,
                            onClick = { onPackageClick(pkg) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Benefits grid (2x2)
            val benefits = listOf(
                "🏥" to "Y tế khẩn cấp\nHỗ trợ 24/7",
                "🧳" to "Hành lý thất lạc\nBồi thường 100%",
                "✈️" to "Hủy/Hoãn chuyến\nĐền bù chi phí",
                "📞" to "Tư vấn miễn phí\nHotline 1900xxx"
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(benefits) { (icon, text) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(lightBlue, Color(0xFFBBDEFB))
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = icon, fontSize = 16.sp)
                        }

                        Column {
                            val lines = text.split("\n")
                            Text(
                                text = lines[0],
                                fontSize = 10.sp,
                                color = Color(0xFF888888),
                                lineHeight = 12.sp
                            )
                            if (lines.size > 1) {
                                Text(
                                    text = lines[1],
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF333333),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CTA footer with gradient
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(blueColor, Color(0xFF1565C0))
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Giá ưu đãi chỉ từ",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        text = "50.000đ/ngày",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Giảm 20% khi mua cùng tour",
                        fontSize = 9.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Button(
                    onClick = onBuyClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Mua ngay",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = blueColor
                    )
                }
            }
        }
    }
}
