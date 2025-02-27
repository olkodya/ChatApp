package com.example.chatapp.feature.chatCreation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.EmptyState
import com.example.chatapp.components.ErrorState
import com.example.chatapp.components.LoadingState
import com.example.chatapp.components.SearchTextField
import com.example.chatapp.ui.theme.AppTheme
import retrofit2.http.Query

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChatBottomSheetContent(
    userListState: CreateChatState,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    handleAction: (CreateChatViewModel.CreateChatAction) -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        contentWindowInsets = { WindowInsets.ime },
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    TextButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = {
                            handleAction(CreateChatViewModel.CreateChatAction.OnCancelButtonClick)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.create_chat_cancel_button_text),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.create_chat_title),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            SearchTextField(
                value = userListState.searchQuery,
                onValueChange = { value ->
                    handleAction(
                        CreateChatViewModel.CreateChatAction.OnSearchUsersFieldEdited(
                            value
                        )
                    )
                },
                placeholder = stringResource(R.string.create_chat_search_placeholder),
                paddingValues = PaddingValues(bottom = 12.dp, start = 16.dp, end = 16.dp)
            )

            Text(
                text = stringResource(R.string.create_chat_contacts_title),
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            when {
                userListState.isLoading -> LoadingState()
                userListState.errorState != null -> {
                    ErrorState(state = userListState.errorState)
                }

                userListState.filteredUsersByQuery.isEmpty() && userListState.searchQuery.isNotEmpty() -> NotFoundMessage(
                    userListState.searchQuery
                )

                userListState.filteredUsersByQuery.isEmpty() && userListState.searchQuery.isEmpty() -> EmptyState(
                    message = stringResource(R.string.create_chat_user_list_empty)
                )

                else -> UsersList(
                    users = userListState.filteredUsersByQuery,
                    handleAction = handleAction
                )
            }
        }
    }
}

@Composable
fun NotFoundMessage(query: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,

        ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    append(stringResource(R.string.create_chat_not_found_first))
                    append(" ")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    append(stringResource(R.string.create_chat_nor_found_query).format(query))
                    append(" ")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    append(stringResource(R.string.create_chat_not_found_second))
                }
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            softWrap = true
        )
    }
}

@Composable
fun UsersList(
    users: List<UserState>,
    handleAction: (CreateChatViewModel.CreateChatAction) -> Unit,
) {
    LazyColumn {
        items(
            count = users.size,
            key = { id -> users[id].id },
        ) { index ->
            val user = users[index]
            val isLastItem = index == users.size - 1
            UserItem(
                modifier = Modifier.then(
                    if (isLastItem) {
                        Modifier.navigationBarsPadding()
                    } else {
                        Modifier
                    }
                ),
                user = user,
                handleAction = handleAction,
            )
        }
    }
}

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: UserState,
    handleAction: (CreateChatViewModel.CreateChatAction) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { handleAction(CreateChatViewModel.CreateChatAction.OnUserClicked(username = user.username)) }
            .padding(vertical = 8.dp, horizontal = 16.dp),
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

@Preview(showBackground = true)
@Composable
fun NotFoundMessagePreview() {
    AppTheme {
        NotFoundMessage(
            query = "xcx"
        )
    }

}