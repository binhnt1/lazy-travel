package com.lazytravel.data.models.enums

enum class VideoQuality(val value: String) {
    P360("360P"),
    P480("480P"),
    P720("720P"),
    P1080("1080P"),
    P4K("4K");

    companion object {
        fun allValues(): List<String> = values().map { it.value }
    }
}

