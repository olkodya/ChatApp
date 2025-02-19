package com.example.chatapp.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder

@Composable
fun ProfileContent(state: ProfileState) {

    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    val screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp
    val headerViewHeight: Dp = screenHeight / 4
    val profileAvatarHeight: Dp = 120.dp

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
            AsyncImage(
                modifier = Modifier
                    .padding(top = headerViewHeight - (profileAvatarHeight / 2))
                    .size(profileAvatarHeight)
                    .clip(CircleShape),
                model = ImageRequest.Builder(context)
                    .data(state.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile avatar",
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop,
                onLoading = { },
                onError = { }
            )

        }
        Text(modifier = Modifier.fillMaxWidth(), text = state.name, textAlign = TextAlign.Center)
        Button(modifier = Modifier.fillMaxWidth(), onClick = {}) { }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileContent(
        ProfileState(
            name = "Кукарцева Ольга",
            imageUrl = "https://eltex2025.rocket.chat/avatar/kurt_olg"
        )
    )
}