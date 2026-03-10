package com.github.anyclaw.screens.main

import androidx.compose.runtime.Immutable

@Immutable
data class FriendsUiState(
    val friends: List<Friend> = MockFriends.friends
)