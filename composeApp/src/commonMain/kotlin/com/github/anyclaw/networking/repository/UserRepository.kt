package com.github.anyclaw.networking.repository

import com.github.anyclaw.networking.Networking
import com.github.anyclaw.networking.api.GithubApi
import com.github.anyclaw.networking.models.RemoteUser

class UserRepository {
    private val githubApi = GithubApi(Networking.httpClient)

    suspend fun searchUsers(query: String, page: Int = 1): Result<List<RemoteUser>> {
        return runCatching {
            githubApi.searchUsers(query, page).items
        }
    }
}