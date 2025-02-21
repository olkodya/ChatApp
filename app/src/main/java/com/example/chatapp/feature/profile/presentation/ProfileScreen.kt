package com.example.chatapp.feature.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.components.AlertDialog

@Composable
fun ProfileScreen(
    onNavigateToLogin: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    var showLogoutDialog by remember { mutableStateOf(false) }
    if (showLogoutDialog) {
        AlertDialog(
            { showLogoutDialog = false },
            {
                showLogoutDialog = false
                viewModel.handleAction(ProfileViewModel.ProfileAction.OnConfirmDialogClick)
            },
            "Выйти из аккаунта",
            "Вы уверены, что хотите выйти?",
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { action ->
            when (action) {
                ProfileViewModel.ProfileEvent.NavigateToLogin -> onNavigateToLogin()
                ProfileViewModel.ProfileEvent.ShowLogoutDialog -> {
                    showLogoutDialog = true
                }

                ProfileViewModel.ProfileEvent.ShowLogoutError -> TODO()
            }
        }
    }

    ProfileContent(
        state = viewModel.profileState.collectAsState().value,
    ) {
        viewModel.handleAction(it)
    }
}
