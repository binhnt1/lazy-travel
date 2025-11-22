package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import com.lazytravel.data.models.enums.TripStatus
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Trip Model - Hành trình proposal (chưa chốt địa điểm)
 * Stage 1: Tạo trip với nhiều destinations để vote
 * Sau khi vote xong → convert sang Buddy (Schedule/Recruiting stage)
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Trip(
    // Creator & basic info
    @EncodeDefault val userId: String = "",              // relation to User (trip creator)
    @EncodeDefault val tripTitle: String = "",           // "Chuyến đi tháng 12/2024"
    @EncodeDefault val description: String = "",         // Mô tả chi tiết chuyến đi
    @EncodeDefault val coverImage: String = "",          // Ảnh cover
    @EncodeDefault val emoji: String = "",               // 🏖️, ⛰️, 🌸

    // Trip constraints
    @EncodeDefault val startDate: Long = 0L,             // Unix timestamp (milliseconds)
    @EncodeDefault val duration: Int = 3,                // Số ngày
    @EncodeDefault val budgetMin: Double = 0.0,          // Budget min (triệu VND)
    @EncodeDefault val budgetMax: Double = 0.0,          // Budget max (triệu VND)
    @EncodeDefault val estimatedBudget: Long = 0L,       // VND

    // Participants
    @EncodeDefault val maxParticipants: Int = 6,         // Số người tối đa
    @EncodeDefault val ageRange: String = "18-35",       // Độ tuổi
    @EncodeDefault val requirements: List<String> = emptyList(), // ["Hòa đồng", "Đúng giờ"]

    // Voting
    @EncodeDefault val pollId: String = "",              // relation to Poll
    @EncodeDefault val votingEndsAt: Long = 0L,          // Thời hạn vote (Unix timestamp)
    @EncodeDefault val allowMultipleVotes: Boolean = true, // Cho phép vote nhiều destinations

    // Status & metadata
    @EncodeDefault val status: String = TripStatus.DRAFT.name,
    @EncodeDefault val tags: List<String> = emptyList(), // ["Phượt", "Budget"]
    @EncodeDefault val region: String = "",              // "Miền Nam", "Miền Bắc", "Miền Trung"

    // After voting
    @EncodeDefault val winningDestinationId: String = "", // TripDestination ID thắng cuộc
    @EncodeDefault val convertedBuddyId: String = "",     // Buddy ID sau khi convert

    // Stats
    @EncodeDefault val viewCount: Int = 0,
    @EncodeDefault val memberCount: Int = 0              // Computed from TripParticipant
) : BaseModel() {

    @kotlinx.serialization.Transient
    var expandedUser: User? = null

    @kotlinx.serialization.Transient
    var expandedPoll: Poll? = null

    @kotlinx.serialization.Transient
    var expandedBuddy: Buddy? = null

    @kotlinx.serialization.Transient
    var expandedDestinations: List<TripDestination> = emptyList()

    @kotlinx.serialization.Transient
    var expandedParticipants: List<TripParticipant> = emptyList()

    // Computed properties
    val endDate: Long
        get() = startDate + (duration * 86400000L) // Add duration days in milliseconds

    val tripDuration: String
        get() = if (duration > 1) "$duration ngày ${duration - 1} đêm" else "$duration ngày"

    val isVotingActive: Boolean
        get() = status == TripStatus.VOTING.name &&
                votingEndsAt > 0 &&
                System.currentTimeMillis() < votingEndsAt

    val isConvertedToBuddy: Boolean
        get() = status == TripStatus.CONVERTED_TO_BUDDY.name && convertedBuddyId.isNotEmpty()

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Trip)
    }

    fun populateExpandedData() {
        val expandData = expand ?: return

        // Expand User
        expandData["userId"]?.let { userJson ->
            try {
                expandedUser = json.decodeFromJsonElement(User.serializer(), userJson)
            } catch (e: Exception) {
                println("❌ Trip: Failed to parse userId: ${e.message}")
            }
        }

        // Expand Poll
        expandData["pollId"]?.let { pollJson ->
            try {
                expandedPoll = json.decodeFromJsonElement(Poll.serializer(), pollJson)
            } catch (e: Exception) {
                println("❌ Trip: Failed to parse pollId: ${e.message}")
            }
        }

        // Expand Buddy (if converted)
        expandData["convertedBuddyId"]?.let { buddyJson ->
            try {
                expandedBuddy = json.decodeFromJsonElement(Buddy.serializer(), buddyJson)
            } catch (e: Exception) {
                println("❌ Trip: Failed to parse convertedBuddyId: ${e.message}")
            }
        }
    }

    override suspend fun getSeedData(): List<Trip> {
        // TODO: Implement seed data
        return emptyList()
    }

    override fun getSchema() = baseCollection(collectionName()) {
        // Creator & basic info
        relation("userId") {
            required = true
            collectionId = User().collectionName()
            cascadeDelete = false
        }
        text("tripTitle") { required = true; max = 200 }
        text("description") { required = true; max = 2000 }
        text("coverImage") { required = false; max = 500 }
        text("emoji") { required = false; max = 10 }

        // Trip constraints
        number("startDate") { required = true; min = 0.0; onlyInt = true }
        number("duration") { required = true; min = 1.0; onlyInt = true }
        number("budgetMin") { required = true; min = 0.0 }
        number("budgetMax") { required = true; min = 0.0 }
        number("estimatedBudget") { required = false; min = 0.0; onlyInt = true }

        // Participants
        number("maxParticipants") { required = false; min = 1.0; onlyInt = true }
        text("ageRange") { required = false; max = 50 }
        json("requirements") { required = false }

        // Voting
        relation("pollId") {
            required = false
            collectionId = Poll().collectionName()
            cascadeDelete = false
        }
        number("votingEndsAt") { required = false; min = 0.0; onlyInt = true }
        bool("allowMultipleVotes") { required = false }

        // Status & metadata
        text("status") { required = true; max = 50 }
        json("tags") { required = false }
        text("region") { required = false; max = 100 }

        // After voting
        relation("winningDestinationId") {
            required = false
            collectionId = "tripdestinations"
            cascadeDelete = false
        }
        relation("convertedBuddyId") {
            required = false
            collectionId = Buddy().collectionName()
            cascadeDelete = false
        }

        // Stats
        number("viewCount") { required = false; min = 0.0; onlyInt = true }
        number("memberCount") { required = false; min = 0.0; onlyInt = true }
    }
}
