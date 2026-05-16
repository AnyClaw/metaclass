package com.github.anyclaw.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.anyclaw.networking.repository.UserRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class UsersViewModel : ViewModel() {
    private val repository = UserRepository()
    private val mutableState = MutableStateFlow(UsersUiState())

    private val queryFlow = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val retryFlow = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val loadMoreFlow = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val state: StateFlow<UsersUiState> = mutableState

    init {
        queryFlow
            .debounce(500)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                flow {
                    performSearch(query, page = 1)
                    emit(Unit)
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)

        retryFlow
            .filter { it.isNotBlank() }
            .flatMapLatest { query ->
                flow {
                    performSearch(query, page = 1)
                    emit(Unit)
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)

        viewModelScope.launch {
            loadMoreFlow.collect {
                val currentState = mutableState.value
                if (currentState.isLoading || currentState.isLoadingMore ||
                    !currentState.hasMorePages || currentState.query.isBlank()) {
                    return@collect
                }

                mutableState.update { it.copy(isLoadingMore = true) }
                val nextPage = currentState.currentPage + 1

                repository.searchUsers(
                    query = currentState.query,
                    page = nextPage
                ).onSuccess { users ->
                    mutableState.update { state ->
                        state.copy(
                            users = state.users + users,
                            isLoadingMore = false,
                            currentPage = nextPage,
                            hasMorePages = users.size >= 30
                        )
                    }
                }.onFailure { e ->
                    if (e is CancellationException) throw e
                    mutableState.update {
                        it.copy(
                            isLoadingMore = false,
                            error = e.message ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }

    private suspend fun performSearch(query: String, page: Int) {
        mutableState.update {
            it.copy(
                isLoading = true,
                error = null,
                users = emptyList(),
                currentPage = page,
                hasMorePages = true
            )
        }

        repository.searchUsers(query, page)
            .onSuccess { users ->
                mutableState.update { state ->
                    state.copy(
                        isLoading = false,
                        users = users,
                        hasMorePages = users.size >= 30
                    )
                }
            }.onFailure { e ->
                if (e is CancellationException) throw e
                Napier.e("Search error", e, tag = "Network")
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        users = emptyList(),
                        error = e.message ?: "Unknown error"
                    )
                }
            }
    }

    fun onQueryChanged(newQuery: String) {
        mutableState.update {
            it.copy(query = newQuery)
        }
        viewModelScope.launch {
            queryFlow.emit(newQuery)
        }
    }

    fun retry() {
        val currentQuery = mutableState.value.query
        if (currentQuery.isNotBlank()) {
            viewModelScope.launch {
                retryFlow.emit(currentQuery)
            }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            loadMoreFlow.emit(Unit)
        }
    }
}