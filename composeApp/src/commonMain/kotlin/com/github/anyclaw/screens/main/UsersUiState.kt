package com.github.anyclaw.screens.main

import androidx.compose.runtime.Immutable
import com.github.anyclaw.networking.models.RemoteUser

@Immutable
data class UsersUiState(
    val query: String = "",
    val users: List<RemoteUser> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
)