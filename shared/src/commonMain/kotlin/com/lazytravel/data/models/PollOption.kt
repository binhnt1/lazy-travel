package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class PollOption(
    val pollId: String = "",
    val optionText: String = "",
    val orderIndex: Int = 0,
    val votesCount: Int = 0
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as PollOption)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("pollId") {
            required = true
            collectionId = "polls"
            cascadeDelete = true
        }
        text("optionText") { required = true; max = 200 }
        number("orderIndex") { required = true; min = 0.0; onlyInt = true }
        number("votesCount") { required = false; min = 0.0; onlyInt = true }
    }

    override suspend fun getSeedData(): List<PollOption> = listOf(
        PollOption(pollId = "poll1", optionText = "Đà Lạt", orderIndex = 0, votesCount = 45),
        PollOption(pollId = "poll1", optionText = "Phú Quốc", orderIndex = 1, votesCount = 67),
        PollOption(pollId = "poll1", optionText = "Sapa", orderIndex = 2, votesCount = 34),
        PollOption(pollId = "poll1", optionText = "Nha Trang", orderIndex = 3, votesCount = 89),

        PollOption(pollId = "poll2", optionText = "Du lịch biển", orderIndex = 0, votesCount = 78),
        PollOption(pollId = "poll2", optionText = "Leo núi", orderIndex = 1, votesCount = 45),
        PollOption(pollId = "poll2", optionText = "Khám phá thành phố", orderIndex = 2, votesCount = 56),
        PollOption(pollId = "poll2", optionText = "Du lịch văn hóa", orderIndex = 3, votesCount = 67),

        PollOption(pollId = "poll3", optionText = "Dưới 3 triệu", orderIndex = 0, votesCount = 89),
        PollOption(pollId = "poll3", optionText = "3-5 triệu", orderIndex = 1, votesCount = 123),
        PollOption(pollId = "poll3", optionText = "5-10 triệu", orderIndex = 2, votesCount = 98),
        PollOption(pollId = "poll3", optionText = "Trên 10 triệu", orderIndex = 3, votesCount = 32)
    )
}

