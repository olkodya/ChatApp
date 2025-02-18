package com.example.chatapp.feature.authorization.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onNavigateToMain: () -> Unit
) {
    val viewModel: LoginViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            when (action) {
                LoginViewModel.LoginEvent.NavigateToMain -> onNavigateToMain()
            }
        }
    }

    LoginContent(
        state = viewModel.loginState.collectAsState().value,

        ) {
        viewModel.handleAction(it)
    }
}