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
data class BuddyParticipant(
    @EncodeDefault val buddyId: String = "",              // Buddy ID (Trip ID)
    @EncodeDefault val userId: String = "",              // Người tham gia
    @EncodeDefault val status: String = ParticipantStatus.PENDING.name,
    @EncodeDefault val requestMessage: String = "",      // Lời nhắn khi đăng ký
    @EncodeDefault val requestedAt: Long = 0L,           // Thời gian đăng ký
    @EncodeDefault val approvedAt: Long = 0L,            // Thời gian duyệt
    @EncodeDefault val rejectionReason: String = "",     // Lý do từ chối
    @EncodeDefault val hostNotes: String = ""            // Ghi chú của host
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as BuddyParticipant)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("buddyId") {
            required = true
            collectionId = Buddy().collectionName()
            cascadeDelete = true
        }
        relation("userId") {
            required = true
            collectionId = User().collectionName()
            cascadeDelete = false
        }
        text("status") { required = true; max = 20 }

        // Request info
        text("requestMessage") { required = false; max = 500 }
        number("requestedAt") { required = true; min = 0.0; onlyInt = true }

        // Approval info
        number("approvedAt") { required = false; min = 0.0; onlyInt = true }
        text("rejectionReason") { required = false; max = 500 }

        // Notes
        text("hostNotes") { required = false; max = 500 }
    }

    override suspend fun getSeedData(): List<BuddyParticipant> {
        val usersRepo = BaseRepository<User>()
        val buddiesRepo = BaseRepository<Buddy>()

        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()
        val trips = buddiesRepo.getRecords<Buddy>().getOrNull() ?: emptyList()

        if (users.isEmpty() || trips.isEmpty()) return emptyList()

        val participants = mutableListOf<BuddyParticipant>()
        val messages = listOf(
            "Mình rất thích phong cách du lịch của bạn, muốn tham gia!",
            "Hi! Mình muốn join nhóm, có thể add mình được không?",
            "Chuyến đi này rất hợp với mình, mong được đi cùng!",
            "Hello! Very interested in this trip. Can I join?",
            "Mình có kinh nghiệm đi phượt, muốn tham gia nhóm!",
            "Có thể cho mình tham gia không? Mình rất thích destination này!",
            "Interested! Please accept my request.",
            "Mình và bạn mình muốn join, 2 người được không?",
            "First time traveling here, would love to join your group!",
            "Perfect timing! I'm free those dates. Count me in!"
        )

        // Tạo participants cho trips
        trips.forEachIndexed { tripIndex, trip ->
            // Số người đã tham gia - tính dựa trên status
            val currentCount = when (trip.status) {
                "FULL" -> trip.maxParticipants - 1  // Full = đã đủ người (trừ host)
                "URGENT" -> trip.maxParticipants - 2 // Urgent = còn 1-2 chỗ
                else -> (trip.maxParticipants * 0.5).toInt() // Available = ~50%
            }
            val hostId = trip.userId

            // Tạo approved participants
            repeat(currentCount) { i ->
                val user = users[(tripIndex + i + 1) % users.size]
                if (user.id == hostId) return@repeat // Skip nếu trùng host

                val requestTime = trip.startDate - ((currentCount - i) * 7 * 24 * 60 * 60 * 1000L)
                val approveTime = requestTime + (2 * 24 * 60 * 60 * 1000L)

                participants.add(BuddyParticipant(
                    buddyId = trip.id,
                    userId = user.id,
                    status = ParticipantStatus.APPROVED.name,
                    requestMessage = messages[(tripIndex + i) % messages.size],
                    requestedAt = requestTime,
                    approvedAt = approveTime,
                    rejectionReason = "",
                    hostNotes = ""
                ))
            }

            // Tạo một vài pending requests (chỉ cho trips chưa full)
            val availableSlots = trip.maxParticipants - currentCount - 1 // -1 for host
            if (availableSlots > 0 && tripIndex % 3 == 0) {
                repeat(2) { i ->
                    val user = users[(tripIndex + currentCount + i + 5) % users.size]
                    if (user.id == hostId) return@repeat

                    val requestTime = trip.startDate - ((i + 1) * 24 * 60 * 60 * 1000L)

                    participants.add(BuddyParticipant(
                        buddyId = trip.id,
                        userId = user.id,
                        status = ParticipantStatus.PENDING.name,
                        requestMessage = messages[(tripIndex + currentCount + i) % messages.size],
                        requestedAt = requestTime,
                        approvedAt = 0L,
                        rejectionReason = "",
                        hostNotes = ""
                    ))
                }
            }
        }

        return participants
    }
}

