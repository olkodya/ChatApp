package com.example.chatapp.feature.authorization.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.feature.authorization.presentation.LoginViewModel.LoginAction


@Composable
fun LoginContent(
    state: LoginScreenState, handleAction: (LoginAction) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.splash_screen),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(62.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Введите логин и пароль для входа",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.login,
                onValueChange = { value -> handleAction(LoginAction.OnLoginFieldChanged(value)) },
                placeholder = { Text(stringResource(R.string.login_text_field)) },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.password,
                shape = RoundedCornerShape(8.dp),
                onValueChange = { handleAction(LoginAction.OnPasswordFieldChanged(it)) },
                placeholder = { Text(stringResource(R.string.password_text_field)) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (!state.isEmpty) 1f else 0.5f),
                onClick = { if (!state.isEmpty) handleAction(LoginAction.OnLoginClick) },
                enabled = !state.isEmpty,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1E4B7B),
                    disabledContainerColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(8.dp)

            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.login_button))
                }
            }
            state.error?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error.getMessage(), color = MaterialTheme.colorScheme.error
                )
            }
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
