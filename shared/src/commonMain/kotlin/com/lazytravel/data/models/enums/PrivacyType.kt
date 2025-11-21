package com.lazytravel.data.models.enums

enum class PrivacyType {
    PUBLIC,
    FRIENDS,
    PRIVATE;

    companion object {
        fun allValues(): List<String> = values().map { it.name }
    }
}

