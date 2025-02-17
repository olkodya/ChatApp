package com.example.chatapp.feature.authorization.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.feature.authorization.presentation.LoginViewModel.LoginAction


@Composable
fun LoginContent(
    state: LoginScreenState,
    handleAction: (LoginAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.login,
            onValueChange = { value -> handleAction(LoginAction.OnLoginFieldChanged(value)) },
            placeholder = { Text(stringResource(R.string.login_text_field)) },
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            onValueChange = { handleAction(LoginAction.OnPasswordFieldChanged(it)) },
            placeholder = { Text(stringResource(R.string.password_text_field)) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { if (!state.isEmpty) handleAction(LoginAction.OnLoginClick) },
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (!state.isEmpty) 1f else 0.5f),
            enabled = !state.isEmpty,
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = Color.White
            )

        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(R.string.login_button))
            }
        }

        state.error?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginPreview() {
    val state = LoginScreenState(
        login = "aads",
        password = "sfsfsf",
        isLoading = false,
        error = null,
    )
    LoginContent(state) {}
}



@Composable
@Preview(showBackground = true)
fun LoadingPreview() {
    val state = LoginScreenState(
        login = "asas",
        password = "sfsfsf",
        isLoading = true,
        error = null,

    )
    LoginContent(state) {}
}

@Composable
@Preview(showBackground = true)
fun ErrorPreview() {
    val state = LoginScreenState(
        login = "asas",
        password = "sfsfsf",
        isLoading = true,
        error = null,
    )
    LoginContent(state) {}
}