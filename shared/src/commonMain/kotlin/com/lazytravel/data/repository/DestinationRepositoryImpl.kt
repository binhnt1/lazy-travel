package com.lazytravel.data.repository

import com.lazytravel.domain.model.Destination
import com.lazytravel.domain.repository.DestinationRepository

/**
 * Repository Implementation - Triển khai cụ thể việc lấy dữ liệu
 * Hiện tại đang sử dụng mock data, sau này có thể thay bằng API call
 */
class DestinationRepositoryImpl : DestinationRepository {

    private val mockDestinations = listOf(
        Destination(
            id = "1",
            name = "Hạ Long Bay",
            description = "Di sản thiên nhiên thế giới với cảnh quan thiên nhiên tuyệt đẹp",
            imageUrl = "https://example.com/halong.jpg",
            rating = 4.8,
            price = 1500000.0
        ),
        Destination(
            id = "2",
            name = "Phú Quốc",
            description = "Đảo ngọc với bãi biển đẹp và hải sản tươi ngon",
            imageUrl = "https://example.com/phuquoc.jpg",
            rating = 4.7,
            price = 2000000.0
        ),
        Destination(
            id = "3",
            name = "Đà Lạt",
            description = "Thành phố ngàn hoa với khí hậu mát mẻ quanh năm",
            imageUrl = "https://example.com/dalat.jpg",
            rating = 4.6,
            price = 1200000.0
        )
    )

    override suspend fun getDestinations(): List<Destination> {
        // Giả lập network delay
        kotlinx.coroutines.delay(1000)
        return mockDestinations
    }

    override suspend fun getDestinationById(id: String): Destination? {
        kotlinx.coroutines.delay(500)
        return mockDestinations.find { it.id == id }
    }

    override suspend fun searchDestinations(query: String): List<Destination> {
        kotlinx.coroutines.delay(500)
        return mockDestinations.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }
}
