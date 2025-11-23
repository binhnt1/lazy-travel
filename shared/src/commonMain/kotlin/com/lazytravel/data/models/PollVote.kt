package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class PollVote(
    val pollId: String = "",
    val optionId: String = "",
    val userId: String = ""
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as PollVote)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("pollId") {
            required = true
            collectionId = "polls"
            cascadeDelete = true
        }
        relation("optionId") {
            required = true
            collectionId = "polloptions"
            cascadeDelete = true
        }
        relation("userId") {
            required = true
            collectionId = "users"
            cascadeDelete = false
        }
    }

    override suspend fun getSeedData(): List<PollVote> = listOf(
        PollVote(pollId = "poll1", optionId = "opt2", userId = "user1"),
        PollVote(pollId = "poll1", optionId = "opt4", userId = "user2"),
        PollVote(pollId = "poll1", optionId = "opt2", userId = "user3"),
        PollVote(pollId = "poll1", optionId = "opt1", userId = "user4"),
        PollVote(pollId = "poll2", optionId = "opt5", userId = "user1"),
        PollVote(pollId = "poll2", optionId = "opt5", userId = "user2"),
        PollVote(pollId = "poll2", optionId = "opt7", userId = "user3")
    )
}

