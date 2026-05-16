package com.github.anyclaw.networking.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerItemsWrapper<T>(
    val items: List<T>
)
