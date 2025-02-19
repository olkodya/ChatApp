package com.example.chatapp.feature.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    onNavigateToLogin: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            when (action) {
                ProfileViewModel.ProfileEvent.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    ProfileContent(
        state = viewModel.profileState.collectAsState().value,
    ) {
        viewModel.handleAction(it)
    }
}
