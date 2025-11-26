package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import com.lazytravel.data.models.enums.ParticipantStatus
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TourParticipant(
    @EncodeDefault val tourId: String = "",              // Tour ID
    @EncodeDefault val userId: String = "",              // Người tham gia
    @EncodeDefault val status: String = ParticipantStatus.APPROVED.name, // Tour usually auto-approve on booking
    @EncodeDefault val bookedAt: Long = 0L,              // Thời gian đặt tour
    @EncodeDefault val numberOfPeople: Int = 1,          // Số người book (có thể book cho nhiều người)
    @EncodeDefault val specialRequests: String = "",     // Yêu cầu đặc biệt
    @EncodeDefault val paymentStatus: String = "",       // Trạng thái thanh toán
    @EncodeDefault val notes: String = ""                // Ghi chú
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as TourParticipant)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("tourId") {
            required = true
            collectionId = Tour().collectionName()
            cascadeDelete = true
        }
        relation("userId") {
            required = true
            collectionId = User().collectionName()
            cascadeDelete = false
        }
        text("status") { required = true; max = 20 }

        // Booking info
        number("bookedAt") { required = true; min = 0.0; onlyInt = true }
        number("numberOfPeople") { required = true; min = 1.0; onlyInt = true }
        text("specialRequests") { required = false; max = 500 }
        text("paymentStatus") { required = false; max = 50 }

        // Notes
        text("notes") { required = false; max = 500 }
    }

    override suspend fun getSeedData(): List<TourParticipant> {
        val usersRepo = BaseRepository<User>()
        val toursRepo = BaseRepository<Tour>()

        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()
        val tours = toursRepo.getRecords<Tour>().getOrNull() ?: emptyList()

        if (users.isEmpty() || tours.isEmpty()) return emptyList()

        val participants = mutableListOf<TourParticipant>()
        val specialRequests = listOf(
            "Cần phòng tầng cao, view đẹp",
            "Ăn chay, không ăn thịt",
            "Cần giường đôi",
            "Có người cao tuổi, cần hỗ trợ di chuyển",
            "Có trẻ em nhỏ, cần ghế ngồi ô tô",
            "Dị ứng hải sản",
            "Cần phòng liền kề với gia đình",
            "Muốn tham gia thêm tour phụ",
            "Need vegetarian meals",
            ""
        )

        val paymentStatuses = listOf("PAID", "PAID", "PAID", "PENDING")

        // Tạo participants cho tours
        tours.forEachIndexed { tourIndex, tour ->
            // Số người đã book (60-90% capacity)
            val bookedSlots = ((tour.maxGroupSize * 0.6).toInt()..(tour.maxGroupSize * 0.9).toInt()).random()

            // Tạo participants
            var totalBooked = 0
            var participantIndex = 0

            while (totalBooked < bookedSlots && participantIndex < users.size) {
                val user = users[(tourIndex + participantIndex) % users.size]
                val numberOfPeople = (1..3).random() // Book cho 1-3 người

                if (totalBooked + numberOfPeople > tour.maxGroupSize) {
                    break // Không vượt quá capacity
                }

                val bookTime = tour.startDate - ((bookedSlots - totalBooked) * 2 * 24 * 60 * 60 * 1000L)

                participants.add(TourParticipant(
                    tourId = tour.id,
                    userId = user.id,
                    status = ParticipantStatus.APPROVED.name,
                    bookedAt = bookTime,
                    numberOfPeople = numberOfPeople,
                    specialRequests = specialRequests[(tourIndex + participantIndex) % specialRequests.size],
                    paymentStatus = paymentStatuses[(tourIndex + participantIndex) % paymentStatuses.size],
                    notes = ""
                ))

                totalBooked += numberOfPeople
                participantIndex++
            }
        }

        return participants
    }
}
