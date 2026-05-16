package com.github.anyclaw.networking.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteUser(
    @SerialName("login")
    val username: String,
    @SerialName("avatar_url")
    val avatar: String? = null,
    @SerialName("id")
    val id: Long
)
