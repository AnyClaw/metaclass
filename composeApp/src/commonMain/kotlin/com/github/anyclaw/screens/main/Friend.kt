package com.github.anyclaw.screens.main

data class Friend(
    val id: Int,
    val username: String = "",
    val lastEntry: Int = 0,
)

object MockFriends {
    val friends = List(20) { index ->
        Friend(
            id = index,
            username = "Friend_$index",
            lastEntry = index
        )
    }
}