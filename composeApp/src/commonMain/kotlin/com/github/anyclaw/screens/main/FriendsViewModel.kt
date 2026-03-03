package com.github.anyclaw.screens.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FriendsViewModel : ViewModel() {
    private val mutableState = MutableStateFlow(FriendsUiState())

    val state: StateFlow<FriendsUiState>
        get() = mutableState

    fun onFriendClick(friendId: Int) {
        mutableState.update {
            it.copy(
                friends = it.friends.map { friend ->
                    if (friend.id == friendId) {
                        friend.copy(
                            lastEntry = friend.lastEntry + 1
                        )
                    }
                    else friend
                }
            )
        }
    }
}