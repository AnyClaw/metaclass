package com.github.anyclaw.app

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {

    @Serializable
    data object Greeting : Destination

    @Serializable
    data object Login : Destination

    @Serializable
    data object Main : Destination
}