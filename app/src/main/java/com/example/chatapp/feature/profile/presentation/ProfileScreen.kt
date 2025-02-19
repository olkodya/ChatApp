package com.example.chatapp.feature.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel = hiltViewModel()

//    LaunchedEffect(Unit) {
//        viewModel.action.collect { action ->
//            when (action) {
//                LoginViewModel.LoginEvent.NavigateToMain -> onNavigateToMain()
//            }
//        }
//    }

    ProfileContent(
        state = viewModel.profileState.collectAsState().value,

        )

}