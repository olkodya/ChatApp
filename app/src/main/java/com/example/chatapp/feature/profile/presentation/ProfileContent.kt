package com.example.chatapp.feature.profile.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import com.example.chatapp.components.ErrorState
import com.example.chatapp.components.LoadingState
import com.example.chatapp.feature.profile.presentation.ProfileViewModel.ProfileAction
import com.example.chatapp.ui.theme.AppTheme

@Composable
fun ProfileContent(
    state: ProfileScreenState,
    handleAction: (ProfileAction) -> Unit
) {


    when (state) {
        is ProfileScreenState.Content -> ProfileInfo(state, handleAction)
        is ProfileScreenState.Error -> ErrorState(state = state.state)
        ProfileScreenState.Loading -> LoadingState()
    }
}


@Composable
fun ProfileInfo(state: ProfileScreenState.Content, handleAction: (ProfileAction) -> Unit) {
    val screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp
    val headerViewHeight: Dp = screenHeight / 4
    val profileAvatarHeight: Dp = 120.dp
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .height(headerViewHeight)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .padding(top = headerViewHeight - (profileAvatarHeight / 2)),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(profileAvatarHeight + 6.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                )
                AsyncImage(
                    modifier = Modifier
                        .size(profileAvatarHeight)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    model = ImageRequest.Builder(context)
                        .data(state.profileInfo.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile avatar",
                    imageLoader = imageLoader,
                    contentScale = ContentScale.Crop,
                    onLoading = { },
                    onError = { }
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth(),
            text = state.profileInfo.name,
            textAlign = TextAlign.Center,
        )

        Card(
            modifier = Modifier
                .padding(top = 46.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = { handleAction(ProfileAction.OnLogoutClick) },
            shape = RoundedCornerShape(10.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Icon(
                    painter = painterResource(com.example.chatapp.R.drawable.exit),
                    contentDescription = "",
                )
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
//                    contentDescription = "Logout icon",
//                    tint = MaterialTheme.colorScheme.onSurface
//                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Выйти",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))

                androidx.compose.material3.Icon(
                    painter = painterResource(com.example.chatapp.R.drawable.shevron_right),
                    contentDescription = "",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    AppTheme {
        ProfileContent(
//            ProfileScreenState(
//                name = "Кукарцева Ольга",
//                imageUrl = "https://eltex2025.rocket.chat/avatar/kurt_olg"
//            ),
            state = ProfileScreenState.Content(profileInfo = ProfileState(name = "Кукарцева Ольга", imageUrl = "")),
            handleAction = { }
        )
    }
}