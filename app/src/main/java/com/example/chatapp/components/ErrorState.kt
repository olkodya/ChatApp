package com.example.chatapp.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.di.model.NetworkException
import com.example.chatapp.di.model.ServerException

fun Throwable.toErrorState(onRetryClick: () -> Unit): ErrorState = when (this) {
    is ServerException -> ErrorState(
        titleRes = R.string.server_error_state_title,
        messageRes = R.string.server_error_state_message,
        onRetryClick = onRetryClick,
    )

    is NetworkException -> ErrorState(
        titleRes = R.string.network_error_state_title,
        messageRes = R.string.network_error_state_message,
        onRetryClick = onRetryClick,
    )

    else -> ErrorState(
        titleRes = R.string.unknown_error_state_title,
        messageRes = R.string.unknown_error_state_message,
        onRetryClick = onRetryClick,
    )
}

@Immutable
data class ErrorState(
    @StringRes
    val titleRes: Int,
    @StringRes
    val messageRes: Int,
    val onRetryClick: () -> Unit,
)

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    state: ErrorState,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(state.titleRes),
            textAlign = TextAlign.Center,
            color = Color.Red,
        )
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(state.messageRes),
            textAlign = TextAlign.Center,
            color = Color.Red,
        )
        OutlinedButton(onClick = state.onRetryClick) {
            Text(text = stringResource(R.string.try_again_string))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorStatePreview() {
    ErrorState(
        modifier = Modifier,
        state = ErrorState(
            titleRes = R.string.unknown_error_state_title,
            messageRes = R.string.unknown_error_state_message,
            onRetryClick = {},
        ),
    )
}
