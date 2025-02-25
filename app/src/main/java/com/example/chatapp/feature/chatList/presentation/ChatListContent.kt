package com.example.chatapp.feature.chatList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.chatapp.components.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListContent(
    fieldState: String,
    chatListState: ChatListScreenState,
    handleAction: (ChatListViewModel.ChatListAction) -> Unit
) {
    val screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp
    val headerHeight: Dp = screenHeight / 5



    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.size(headerHeight / 3))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Чаты", modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    )
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(
                            onClick = { Unit },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .size(24.dp)
                                .wrapContentWidth(),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Добавить",
//                                modifier = Modifier.size(24.dp)
                            )
//                            }
                        }
                    }

//                    IconButton(
//                        onClick = {  },
//                        modifier = Modifier.wrapContentSize(),
//                        enabled = true,
//                        colors = TODO(),
//                        interactionSource = TODO()
//                    ) {
//                        Text("ssdd")
////                        Icon(
////                            painterResource(android.R.drawable.star_on)
////                        )
//                    }

                }
                Spacer(Modifier.weight(1f))
                BasicTextField(
                    value = fieldState,
                    onValueChange = { value ->
                        handleAction(
                            ChatListViewModel.ChatListAction.OnSearchFieldEdited(
                                value
                            )
                        )
                    },
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                ) { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = fieldState,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = MutableInteractionSource(),
                        container = {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(8.dp))
                            )
                        },
                        placeholder = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Поиск",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Поиск по чатам",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        },
                        contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                            start = 12.dp,
                            top = 11.dp,
                            end = 12.dp,
                            bottom = 11.dp
                        ),
                    )
                }
            }
        }
        when (chatListState) {
            is ChatListScreenState.Content -> ChatList(state = chatListState)
            is ChatListScreenState.Error -> Unit
            ChatListScreenState.Loading -> LoadingState()
        }
    }
}

@Composable
private fun ChatList(state: ChatListScreenState.Content) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(
            state.rooms.size,
            key = { id -> state.rooms[id].id }) { index ->
            val chat = state.rooms[index]
            ChatItem(
                title = chat.name,
                message = chat.lastMassage,
                time = chat.lastMessageDate,
                isStarred = true
            )
        }
    }
}

@Composable
private fun ChatItem(
    title: String,
    message: String,
    time: String,
    avatar: String? = null,
    isStarred: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isStarred) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        } else {
            AsyncImage(
                model = avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(
                text = message,
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = time,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
//    ChatListContent(
////        viewModel.fieldState.collectAsState().value,
////        viewModel.chatListState.collectAsState().value
////    ) { viewModel.handleAction(it) }
//        )
}