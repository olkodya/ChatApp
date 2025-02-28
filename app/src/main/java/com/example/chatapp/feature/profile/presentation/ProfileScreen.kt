package com.example.chatapp.feature.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.R
import com.example.chatapp.components.AlertDialog

@Composable
fun ProfileScreen(
    onNavigateToLogin: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    var showLogoutDialog by remember { mutableStateOf(false) }
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            onConfirmation = {
                showLogoutDialog = false
                viewModel.handleAction(ProfileViewModel.ProfileAction.OnConfirmDialogClick)
            },
            dialogTitle = stringResource(R.string.profile_logout_dialog_title),
            dialogText = stringResource(R.string.profile_logout_dialog_body),
            confirmButtonText = stringResource(R.string.profile_logout_dialog_confirm_text),
            dismissButtonText = stringResource(R.string.profile_logout_dialog_dismiss_text),
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { action ->
            when (action) {
                ProfileViewModel.ProfileEvent.NavigateToLogin -> onNavigateToLogin()
                ProfileViewModel.ProfileEvent.ShowLogoutDialog -> {
                    showLogoutDialog = true
                }

                ProfileViewModel.ProfileEvent.ShowLogoutError -> {}
            }
        }
    }

    ProfileContent(
        state = viewModel.profileState.collectAsState().value,
    ) {
        viewModel.handleAction(it)
    }
}
