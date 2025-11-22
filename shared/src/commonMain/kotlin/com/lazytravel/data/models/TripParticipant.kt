package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import com.lazytravel.data.models.enums.ParticipantStatus
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * TripParticipant Model - Members tham gia Trip
 * Quản lý members trong Trip (proposal stage)
 * Khi convert Trip → Buddy, các TripParticipant (APPROVED) sẽ tự động convert sang BuddyParticipant
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TripParticipant(
    // Relations
    @EncodeDefault val tripId: String = "",              // relation to Trip
    @EncodeDefault val userId: String = "",              // relation to User

    // Participation info
    @EncodeDefault val status: String = ParticipantStatus.PENDING.name, // PENDING, APPROVED, REJECTED
    @EncodeDefault val role: String = "MEMBER",          // CREATOR, MEMBER

    // Request info
    @EncodeDefault val requestMessage: String = "",      // Lời nhắn xin tham gia
    @EncodeDefault val requestedAt: Long = 0L,           // Timestamp đăng ký

    // Approval info
    @EncodeDefault val approvedAt: Long = 0L,            // Timestamp duyệt
    @EncodeDefault val approvedBy: String = "",          // userId (trip creator)
    @EncodeDefault val rejectionReason: String = "",     // Lý do từ chối

    // Voting tracking
    @EncodeDefault val hasVoted: Boolean = false,        // Đã vote destination chưa?
    @EncodeDefault val votedAt: Long = 0L,               // Timestamp vote

    // Notes
    @EncodeDefault val creatorNotes: String = ""         // Ghi chú của trip creator
) : BaseModel() {

    @kotlinx.serialization.Transient
    var expandedTrip: Trip? = null

    @kotlinx.serialization.Transient
    var expandedUser: User? = null

    @kotlinx.serialization.Transient
    var expandedApprover: User? = null

    val isPending: Boolean
        get() = status == ParticipantStatus.PENDING.name

    val isApproved: Boolean
        get() = status == ParticipantStatus.APPROVED.name

    val isRejected: Boolean
        get() = status == ParticipantStatus.REJECTED.name

    val isCreator: Boolean
        get() = role == "CREATOR"

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as TripParticipant)
    }

    fun populateExpandedData() {
        val expandData = expand ?: return

        // Expand Trip
        expandData["tripId"]?.let { tripJson ->
            try {
                expandedTrip = json.decodeFromJsonElement(Trip.serializer(), tripJson)
            } catch (e: Exception) {
                println("❌ TripParticipant: Failed to parse tripId: ${e.message}")
            }
        }

        // Expand User
        expandData["userId"]?.let { userJson ->
            try {
                expandedUser = json.decodeFromJsonElement(User.serializer(), userJson)
            } catch (e: Exception) {
                println("❌ TripParticipant: Failed to parse userId: ${e.message}")
            }
        }

        // Expand Approver
        expandData["approvedBy"]?.let { approverJson ->
            try {
                expandedApprover = json.decodeFromJsonElement(User.serializer(), approverJson)
            } catch (e: Exception) {
                println("❌ TripParticipant: Failed to parse approvedBy: ${e.message}")
            }
        }
    }

    override suspend fun getSeedData(): List<TripParticipant> {
        // TODO: Implement seed data
        return emptyList()
    }

    override fun getSchema() = baseCollection(collectionName()) {
        // Relations
        relation("tripId") {
            required = true
            collectionId = Trip().collectionName()
            cascadeDelete = true
        }
        relation("userId") {
            required = true
            collectionId = User().collectionName()
            cascadeDelete = false
        }

        // Participation info
        text("status") { required = true; max = 20 }
        text("role") { required = true; max = 20 }

        // Request info
        text("requestMessage") { required = false; max = 500 }
        number("requestedAt") { required = true; min = 0.0; onlyInt = true }

        // Approval info
        number("approvedAt") { required = false; min = 0.0; onlyInt = true }
        relation("approvedBy") {
            required = false
            collectionId = User().collectionName()
            cascadeDelete = false
        }
        text("rejectionReason") { required = false; max = 500 }

        // Voting tracking
        bool("hasVoted") { required = false }
        number("votedAt") { required = false; min = 0.0; onlyInt = true }

        // Notes
        text("creatorNotes") { required = false; max = 500 }
    }
}
