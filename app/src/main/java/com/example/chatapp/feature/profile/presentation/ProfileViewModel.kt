package com.example.chatapp.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.components.toErrorState
import com.example.chatapp.di.model.UnauthorizedException
import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.profile.domain.GetProfileInfoUseCase
import com.example.chatapp.feature.profile.domain.LogoutUseCase
import com.example.chatapp.feature.profile.domain.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val authPreferences: AuthPreferences,
) : ViewModel() {

    private val mutableProfileState =
        MutableStateFlow<ProfileScreenState>(ProfileScreenState.Loading)
    val profileState: StateFlow<ProfileScreenState> = mutableProfileState.asStateFlow()

    private val mutableEvent = Channel<ProfileEvent>()
    val event = mutableEvent.receiveAsFlow()

    init {
        getProfileInfo()
    }

    fun handleAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnLogoutClick -> showLogoutDialog()
            ProfileAction.OnConfirmDialogClick -> logoutUser()
        }
    }

    private fun getProfileInfo() {
        viewModelScope.launch {
            runCatching {
                mutableProfileState.value = ProfileScreenState.Loading
                getProfileInfoUseCase(
                    authPreferences.getAuthData()?.userId ?: "",
                    authPreferences.getAuthData()?.token ?: ""
                )
            }.onFailure { throwable ->
                if (throwable is UnauthorizedException) {
                     logoutUser()
                     return@launch
                }

                val errorState = throwable.toErrorState(
                    onRetryClick = { getProfileInfo() }
                )
                mutableProfileState.value = ProfileScreenState.Error(state = errorState)
            }.onSuccess { response ->
                mutableProfileState.value =
                    ProfileScreenState.Content(
                        profileInfo = response.toState()
                    )
            }
        }
    }

    private fun showLogoutDialog() {
        viewModelScope.launch {
            mutableEvent.send(ProfileEvent.ShowLogoutDialog)
        }
    }

    private fun logoutUser() {
        viewModelScope.launch {
            try {
                logoutUseCase()
                mutableEvent.send(ProfileEvent.NavigateToLogin)
            } catch (_: Exception) {
                mutableEvent.send(ProfileEvent.ShowLogoutError)
            }
        }
    }

    sealed class ProfileAction {
        data object OnLogoutClick : ProfileAction()
        data object OnConfirmDialogClick : ProfileAction()
    }

    sealed class ProfileEvent {
        data object ShowLogoutDialog : ProfileEvent()
        data object ShowLogoutError : ProfileEvent()
        data object NavigateToLogin : ProfileEvent()
    }
}
