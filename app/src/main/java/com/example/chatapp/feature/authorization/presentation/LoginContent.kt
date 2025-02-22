package com.example.chatapp.feature.authorization.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.feature.authorization.presentation.LoginViewModel.LoginAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    state: LoginScreenState, handleAction: (LoginAction) -> Unit
) {

    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }

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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(62.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
                text = stringResource(R.string.login_information_text),
                color = Color.White,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(32.dp))

            BasicTextField(
                value = state.login,
                onValueChange = { value -> handleAction(LoginAction.OnLoginFieldChanged(value)) },
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                singleLine = true,

                keyboardOptions = if (state.login.isNotEmpty()) KeyboardOptions(
                    imeAction = ImeAction.Next
                ) else KeyboardOptions(),
                keyboardActions = if (state.login.isNotEmpty()) KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ) else KeyboardActions(),
            ) { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = state.login,
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
                        Text(
                            stringResource(R.string.login_login_text_field),
                            fontSize = 14.sp,
                        )
                    },
                    contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                        start = 12.dp,
                        top = 11.dp,
                        end = 12.dp,
                        bottom = 11.dp
                    ),
                )
            }

            Spacer(modifier = Modifier.height(38.dp))

            BasicTextField(
                value = state.password,
                onValueChange = { value -> handleAction(LoginAction.OnPasswordFieldChanged(value)) },
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
            ) { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = state.password,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    interactionSource = MutableInteractionSource(),
                    container = {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(8.dp))
                        )
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.login_password_text_field),
                            fontSize = 14.sp,
                        )
                    },
                    contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                        start = 12.dp,
                        top = 11.dp,
                        end = 12.dp,
                        bottom = 11.dp
                    ),
                    trailingIcon = {
                        if (state.password.isNotEmpty())
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        if (passwordVisible) R.drawable.outline_visibility_off_24
                                        else R.drawable.outline_visibility_24
                                    ),
                                    contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                    },
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (!state.isEmpty) 1f else 0.5f)
                    .padding(bottom = 64.dp)
                    .height(48.dp),
                onClick = { if (!state.isEmpty) handleAction(LoginAction.OnLoginClick) },
                enabled = !state.isEmpty,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                } else {
                    Text(stringResource(R.string.login_button))
                }
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
    )
    LoginContent(state) {}
}
