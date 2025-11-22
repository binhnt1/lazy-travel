package com.lazytravel.ui.components.sections.tours

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazytravel.ui.components.cards.tours.ServiceCard

data class ServiceItem(
    val iconUrl: String,
    val name: String,
    val backgroundColor: Color,
    val route: String
)

@Composable
fun ServicesGridSection(
    onServiceClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val services = listOf(
        ServiceItem(
            iconUrl = "https://cdn-icons-png.flaticon.com/512/201/201623.png",
            name = "Tour du lịch",
            backgroundColor = Color(0xFFE3F2FD),
            route = "tours"
        ),
        ServiceItem(
            iconUrl = "https://cdn-icons-png.flaticon.com/512/2313/2313906.png",
            name = "Vé máy bay",
            backgroundColor = Color(0xFFFCE4EC),
            route = "flights"
        ),
        ServiceItem(
            iconUrl = "https://cdn-icons-png.flaticon.com/512/235/235889.png",
            name = "Khách sạn",
            backgroundColor = Color(0xFFE8F5E9),
            route = "hotels"
        ),
        ServiceItem(
            iconUrl = "https://cdn-icons-png.flaticon.com/512/2503/2503508.png",
            name = "Visa",
            backgroundColor = Color(0xFFFFF3E0),
            route = "visa"
        ),
        ServiceItem(
            iconUrl = "https://cdn-icons-png.flaticon.com/512/2830/2830312.png",
            name = "Bảo hiểm",
            backgroundColor = Color(0xFFF3E5F5),
            route = "insurance"
        ),
        ServiceItem(
            iconUrl = "https://cdn-icons-png.flaticon.com/512/3097/3097180.png",
            name = "Thuê xe",
            backgroundColor = Color(0xFFE0F2F1),
            route = "car_rental"
        ),
        ServiceItem(
            iconUrl = "https://cdn-icons-png.flaticon.com/512/684/684908.png",
            name = "Bản đồ",
            backgroundColor = Color(0xFFFAFAFA),
            route = "map"
        ),
        ServiceItem(
            iconUrl = "https://cdn-icons-png.flaticon.com/512/4727/4727266.png",
            name = "Ưu đãi",
            backgroundColor = Color(0xFFFFF8E1),
            route = "deals"
        )
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
                .padding(top = 14.dp, bottom = 16.dp)
        ) {
            // Section header
            Text(
                text = "🎯 Dịch vụ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )

            // Services grid (4 columns)
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false // Disable scroll since it's in LazyColumn
            ) {
                items(services) { service ->
                    ServiceCard(
                        iconUrl = service.iconUrl,
                        name = service.name,
                        backgroundColor = service.backgroundColor,
                        onClick = { onServiceClick(service.route) }
                    )
                }
            }
        }
    }
}
