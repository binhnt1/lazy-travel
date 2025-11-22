package com.lazytravel.data.models.enums

enum class ShareType {
    SHARE,
    REPOST,
    QUOTE;

    companion object {
        fun allValues(): List<String> = values().map { it.name }
    }
}

