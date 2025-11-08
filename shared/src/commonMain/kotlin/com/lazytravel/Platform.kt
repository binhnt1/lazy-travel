package com.lazytravel

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
