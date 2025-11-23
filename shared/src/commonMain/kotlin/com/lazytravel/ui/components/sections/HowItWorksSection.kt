package com.lazytravel.ui.components.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.HowItWork
import com.lazytravel.ui.components.cards.HowItWorkCard
import kotlinx.coroutines.launch

@Composable
fun HowItWorkSection() {
    val scope = rememberCoroutineScope()
    val repository = remember { BaseRepository<HowItWork>() }
    var isLoading by remember { mutableStateOf(true) }
    var steps by remember { mutableStateOf<List<HowItWork>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            repository.getRecords<HowItWork>().fold(
                onSuccess = { fetchedSteps ->
                    steps = fetchedSteps
                    isLoading = false
                },
                onFailure = { _ ->
                    isLoading = false
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFFFF3E0)
            ) {
                Text(
                    text = "🎯 HƯỚNG DẪN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B35),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Text(
                text = "Cách hoạt động",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Text(
                text = "Đơn giản và hiệu quả",
                fontSize = 13.sp,
                color = Color(0xFF666666)
            )
        }

        // 2x2 Grid of cards
        if (isLoading) {
            Text(
                text = "Đang tải...",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(16.dp)
            )
        } else if (steps.isEmpty()) {
            Text(
                text = "Chưa có dữ liệu",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                steps.chunked(2).forEach { rowSteps ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowSteps.forEach { step ->
                            HowItWorkCard(
                                step = step,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Add empty space for odd number of items
                        if (rowSteps.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}