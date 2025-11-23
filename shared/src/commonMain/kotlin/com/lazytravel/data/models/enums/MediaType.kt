package com.lazytravel.data.models.enums

enum class MediaType {
    IMAGE,
    VIDEO;

    companion object {
        fun allValues(): List<String> = values().map { it.name }
    }
}

