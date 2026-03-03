package com.github.anyclaw.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.anyclaw.dimens.padding_32
import com.github.anyclaw.dimens.padding_64
import metaclass.composeapp.generated.resources.Res
import metaclass.composeapp.generated.resources.user
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreen(
    viewModel: FriendsViewModel = viewModel { FriendsViewModel() },
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = padding_64)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = "Список друзей",
                modifier = Modifier.padding(vertical = padding_32)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = state.friends,
                    key = { it.id }
                ) { friend ->
                    FriendCard(friend, viewModel::onFriendClick)
                }
            }
        }
    }
}

@Composable
fun FriendCard(
    friend: Friend,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        onClick = { onClick(friend.id) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.user),
                contentDescription = "Sample icon",
                modifier = Modifier
                    .size(64.dp)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(friend.username)
                Text("Был в сети ${friend.lastEntry} минут назад")
            }
        }
    }
}