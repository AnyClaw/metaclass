package com.github.anyclaw.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.github.anyclaw.dimens.padding_16
import com.github.anyclaw.dimens.padding_32
import com.github.anyclaw.dimens.padding_8
import com.github.anyclaw.networking.models.RemoteUser
import metaclass.composeapp.generated.resources.Res
import metaclass.composeapp.generated.resources.empty_search
import metaclass.composeapp.generated.resources.enter_username
import metaclass.composeapp.generated.resources.not_found
import metaclass.composeapp.generated.resources.retry
import metaclass.composeapp.generated.resources.user
import metaclass.composeapp.generated.resources.welcome
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    viewModel: UsersViewModel = viewModel { UsersViewModel() },
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            if (state.isLoading || state.isLoadingMore || !state.hasMorePages) {
                return@derivedStateOf false
            }

            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null &&
                    lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            viewModel.loadMore()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = padding_16)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = stringResource(Res.string.welcome),
                modifier = Modifier.padding(top = padding_32, bottom = padding_16)
            )

            Row(
                modifier = Modifier
                    .padding(bottom = padding_8)
                    .fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier
                        .padding(end = padding_8)
                        .fillMaxWidth(),
                    value = state.query,
                    onValueChange = viewModel::onQueryChanged,
                    placeholder = { Text(stringResource(Res.string.enter_username)) },
                    isError = state.error != null
                )
            }

            if (state.error != null && state.users.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Ошибка: ${state.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(
                        onClick = viewModel::retry,
                        modifier = Modifier.padding(top = padding_16)
                    ) {
                        Text(stringResource(Res.string.retry))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = padding_32)
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState
                ) {
                    items(
                        items = state.users,
                        key = { it.id }
                    ) { user ->
                        UserCard(user)
                    }

                    if (state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(padding_16),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
                if (state.isLoading && state.users.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                if (!state.isLoading && !state.isLoadingMore &&
                    state.users.isEmpty() && state.query.isNotBlank() && state.error == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.empty_search),
                            contentDescription = stringResource(Res.string.not_found),
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(stringResource(Res.string.not_found))
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(
    user: RemoteUser,
    onClick: (Long) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(bottom = padding_16)
            .fillMaxWidth(),
        onClick = { onClick(user.id) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = user.avatar,
                contentDescription = "${user.username} avatar",
                contentScale = ContentScale.Crop,
                error = painterResource(Res.drawable.user),
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .padding(padding_8)
            )
            Column(modifier = Modifier.padding(padding_16)) {
                Text(user.username)
                Text("ID: ${user.id}")
            }
        }
    }
}