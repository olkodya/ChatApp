package com.example.chatapp.feature.chatList.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.chatapp.components.LoadingState
import com.example.chatapp.components.SearchTextField
import com.example.chatapp.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListScreen() {
    val viewModel: ChatListViewModel = hiltViewModel()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val userListState = viewModel.userListState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ChatListViewModel.ChatListEvent.NavigateToChat -> TODO()
                ChatListViewModel.ChatListEvent.ShowBottomSheet -> {
                    showBottomSheet = true
                }
            }
        }
    }
    Scaffold() { contentPadding ->
        ChatListContent(
            fieldState = viewModel.chatsFieldState.collectAsState().value,
            chatListState = viewModel.chatListState.collectAsState().value,
            handleAction = { viewModel.handleAction(it) }
        )
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "Отменить",
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable {
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                }
                                .weight(1f)
                        )
                        Text(
                            text = "Новый чат",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    SearchTextField(
                        value = viewModel.usersFieldState.collectAsState().value,
                        onValueChange = { value ->
                            viewModel.handleAction(
                                ChatListViewModel.ChatListAction.OnSearchUsersFieldEdited(
                                    value
                                )
                            )
                        },
                        placeholder = "Поиск по контактам",
                        paddingValues = PaddingValues(bottom = 12.dp)
                    )

                    Text(
                        text = "Контакты",
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    when (userListState) {
                        is UserListState.Content -> UsersList(userListState.users) {
                            viewModel.handleAction(
                                it
                            )
                        }

                        is UserListState.Error -> {}
                        UserListState.Loading -> LoadingState()
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UsersList(
    users: List<UserState>, handleAction: (ChatListViewModel.ChatListAction) -> Unit
) {
    LazyColumn() {
        items(
            count = users.size,
            key = { id -> users[id].id },
        ) { index ->
            val user = users[index]
            UserItem(user = user, handleAction)
        }
    }
}

@Composable
fun UserItem(user: UserState, handleAction: (ChatListViewModel.ChatListAction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { handleAction(ChatListViewModel.ChatListAction.OnUserClicked(username = user.username))}
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Text(
            text = user.name,
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            fontSize = 16.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun BottomSheetPreview() {
    AppTheme {
        ChatListScreen()
    }
}






