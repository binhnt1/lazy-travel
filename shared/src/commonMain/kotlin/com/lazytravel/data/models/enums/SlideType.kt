package com.lazytravel.data.models.enums

enum class SlideType {
    IMAGE,
    VIDEO,
    TEXT,
    LOCATION;

    companion object {
        fun allValues(): List<String> = values().map { it.name }
    }
}

