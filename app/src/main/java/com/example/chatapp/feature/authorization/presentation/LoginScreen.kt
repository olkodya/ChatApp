package com.example.chatapp.feature.authorization.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.R
import com.example.chatapp.components.ChatSnackbar
import com.example.chatapp.components.ChatSnackbarVisuals

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    onNavigateToMain: () -> Unit
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            when (action) {
                LoginViewModel.LoginEvent.NavigateToMain -> onNavigateToMain()
                is LoginViewModel.LoginEvent.ShowSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        ChatSnackbarVisuals(messageRes = action.message)
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                ChatSnackbar(snackbarData = data)
            }
        },
    ) {
        LoginContent(
            state = viewModel.loginState.collectAsState().value,

            ) {
            viewModel.handleAction(it)
        }
    }
}