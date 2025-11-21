package com.lazytravel.data.models.enums

enum class ReactionType {
    LIKE,
    LOVE,
    HAHA,
    WOW,
    SAD,
    ANGRY;

    companion object {
        fun allValues(): List<String> = values().map { it.name }
    }
}

