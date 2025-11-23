package com.lazytravel.data.models.enums

enum class PostType {
    TEXT,
    SINGLE_IMAGE,
    ALBUM,
    VIDEO,
    SHARE,
    POLL,
    CHECK_IN,
    TRIP_STORY;

    companion object {
        fun allValues(): List<String> = values().map { it.name }
    }
}

