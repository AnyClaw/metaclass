package com.github.anyclaw

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform