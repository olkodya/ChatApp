package com.example.chatapp.feature.chat.presentation.compnents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.LoadingState
import com.example.chatapp.feature.chat.presentation.ChatScreenState
import com.example.chatapp.feature.chat.presentation.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    chatState: ChatScreenState,
    handleAction: (ChatViewModel.ChatAction) -> Unit,
) {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.background,
            actionIconContentColor = MaterialTheme.colorScheme.primary,
        ),

        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Button(
                        onClick = {
                            handleAction(ChatViewModel.ChatAction.OnBackClicked)
                        },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.shevron_left),
                            contentDescription = stringResource(R.string.profile_shevron_icon_content_description),
                        )
                    }

                    if (chatState.topBarState?.isLoading == true) {
                        LoadingState(Modifier, color = Color.White)
                    } else {
                        AsyncImage(
                            model = chatState.topBarState?.chatAvatarUrl,
                            contentDescription = "xsscs",
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 4.dp)
                                .weight(1f),
                        ) {

                            Text(
                                text = chatState.topBarState?.chatName.toString(),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            if (chatState.topBarState?.chatType != "d") {
                                Text(
                                    text = "Участники: " + chatState.topBarState?.numberOfMembers.toString(),
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {

                        },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.more),
                            contentDescription = stringResource(R.string.profile_shevron_icon_content_description),
                        )
                    }

                }
            }
        }
    )
}