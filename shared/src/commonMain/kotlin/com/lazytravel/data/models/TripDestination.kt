package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * TripDestination Model - Các destinations được đề xuất cho Trip
 * Mỗi Trip có nhiều TripDestinations để members vote
 * Sau khi vote, destination có votes cao nhất sẽ được chọn
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TripDestination(
    // Relation
    @EncodeDefault val tripId: String = "",              // relation to Trip

    // Destination info (2 options: manual or select from Place)
    @EncodeDefault val destinationName: String = "",     // "Phú Quốc", "Đà Lạt" (manual entry)
    @EncodeDefault val placeId: String = "",             // relation to Place (optional)
    @EncodeDefault val cityId: String = "",              // relation to City (optional)

    // Additional info
    @EncodeDefault val description: String = "",         // Lý do đề xuất destination này
    @EncodeDefault val imageUrl: String = "",            // Ảnh destination
    @EncodeDefault val estimatedBudget: Double = 0.0,    // Budget ước tính cho destination này (triệu VND)

    // Voting (linked to Poll system)
    @EncodeDefault val pollOptionId: String = "",        // Link to PollOption
    @EncodeDefault val votesCount: Int = 0,              // Số votes (denormalized for performance)

    // Metadata
    @EncodeDefault val proposedBy: String = "",          // userId - người đề xuất
    @EncodeDefault val orderIndex: Int = 0,              // Thứ tự hiển thị
    @EncodeDefault val isWinner: Boolean = false         // Destination thắng cuộc sau vote
) : BaseModel() {

    @kotlinx.serialization.Transient
    var expandedTrip: Trip? = null

    @kotlinx.serialization.Transient
    var expandedPlace: Place? = null

    @kotlinx.serialization.Transient
    var expandedCity: City? = null

    @kotlinx.serialization.Transient
    var expandedProposer: User? = null

    @kotlinx.serialization.Transient
    var expandedPollOption: PollOption? = null

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as TripDestination)
    }

    fun populateExpandedData() {
        val expandData = expand ?: return

        // Expand Trip
        expandData["tripId"]?.let { tripJson ->
            try {
                expandedTrip = json.decodeFromJsonElement(Trip.serializer(), tripJson)
            } catch (e: Exception) {
                println("❌ TripDestination: Failed to parse tripId: ${e.message}")
            }
        }

        // Expand Place
        expandData["placeId"]?.let { placeJson ->
            try {
                expandedPlace = json.decodeFromJsonElement(Place.serializer(), placeJson)
            } catch (e: Exception) {
                println("❌ TripDestination: Failed to parse placeId: ${e.message}")
            }
        }

        // Expand City
        expandData["cityId"]?.let { cityJson ->
            try {
                val city = json.decodeFromJsonElement(City.serializer(), cityJson)
                city.populateExpandedData()
                expandedCity = city
            } catch (e: Exception) {
                println("❌ TripDestination: Failed to parse cityId: ${e.message}")
            }
        }

        // Expand Proposer
        expandData["proposedBy"]?.let { userJson ->
            try {
                expandedProposer = json.decodeFromJsonElement(User.serializer(), userJson)
            } catch (e: Exception) {
                println("❌ TripDestination: Failed to parse proposedBy: ${e.message}")
            }
        }

        // Expand PollOption
        expandData["pollOptionId"]?.let { pollOptionJson ->
            try {
                expandedPollOption = json.decodeFromJsonElement(PollOption.serializer(), pollOptionJson)
            } catch (e: Exception) {
                println("❌ TripDestination: Failed to parse pollOptionId: ${e.message}")
            }
        }
    }

    override suspend fun getSeedData(): List<TripDestination> {
        // TODO: Implement seed data
        return emptyList()
    }

    override fun getSchema() = baseCollection(collectionName()) {
        // Relation
        relation("tripId") {
            required = true
            collectionId = Trip().collectionName()
            cascadeDelete = true
        }

        // Destination info
        text("destinationName") { required = true; max = 200 }
        relation("placeId") {
            required = false
            collectionId = Place().collectionName()
            cascadeDelete = false
        }
        relation("cityId") {
            required = false
            collectionId = City().collectionName()
            cascadeDelete = false
        }

        // Additional info
        text("description") { required = false; max = 1000 }
        text("imageUrl") { required = false; max = 500 }
        number("estimatedBudget") { required = false; min = 0.0 }

        // Voting
        relation("pollOptionId") {
            required = false
            collectionId = PollOption().collectionName()
            cascadeDelete = false
        }
        number("votesCount") { required = false; min = 0.0; onlyInt = true }

        // Metadata
        relation("proposedBy") {
            required = true
            collectionId = User().collectionName()
            cascadeDelete = false
        }
        number("orderIndex") { required = true; min = 0.0; onlyInt = true }
        bool("isWinner") { required = false }
    }
}
