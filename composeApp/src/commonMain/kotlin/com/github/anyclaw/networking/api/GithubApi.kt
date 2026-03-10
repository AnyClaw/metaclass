package com.github.anyclaw.networking.api

import com.github.anyclaw.networking.models.RemoteUser
import com.github.anyclaw.networking.models.ServerItemsWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class GithubApi(private val httpClient: HttpClient) {
    suspend fun searchUsers(
        query: String,
        page: Int = 1,
        perPage: Int = 30
    ): ServerItemsWrapper<RemoteUser> {
        return httpClient.get("search/users") {
            parameter("q", query)
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
    }
}