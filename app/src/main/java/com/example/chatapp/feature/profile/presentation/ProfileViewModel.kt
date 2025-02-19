package com.example.chatapp.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.feature.authorization.presentation.LoginScreenState
import com.example.chatapp.feature.authorization.presentation.LoginViewModel.LoginEvent
import com.example.chatapp.feature.profile.domain.GetProfileInfoUseCase
import com.example.chatapp.feature.profile.domain.LogoutUseCase
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
) : ViewModel() {

    private val mutableProfileState = MutableStateFlow(ProfileState("", ""))
    val profileState: StateFlow<ProfileState> = mutableProfileState.asStateFlow()

    private val mutableActions = Channel<ProfileEvent>()
    val action = mutableActions.receiveAsFlow()

    init {
        getProfileInfo()
    }

    fun handleAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnLogoutClick -> logout()
        }
    }

    private fun getProfileInfo() {
        viewModelScope.launch {
            val response = getProfileInfoUseCase()
            mutableProfileState.value =
                ProfileState(imageUrl = response.avatarUrl, name = response.name)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase()
                mutableActions.send(ProfileEvent.NavigateToLogin)
            } catch (ex: Exception) {

            }
        }
    }

    sealed class ProfileAction {
        data object OnLogoutClick : ProfileAction()
    }

    sealed class ProfileEvent {
        data object NavigateToLogin : ProfileEvent()
    }
}
