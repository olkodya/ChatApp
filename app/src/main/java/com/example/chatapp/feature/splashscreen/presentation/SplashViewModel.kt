package com.example.chatapp.feature.splashscreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.feature.splashscreen.domain.IsAuthorizedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isAuthorizedUseCase: IsAuthorizedUseCase,
) : ViewModel() {

    private val mutableEvents = Channel<SplashEvent>()
    val events = mutableEvents.receiveAsFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            try {
                val isAuth = isAuthorizedUseCase()
                if (isAuth) {
                    mutableEvents.send(SplashEvent.NavigateToMain)
                } else {
                    mutableEvents.send(SplashEvent.NavigateToLogin)
                }
            } catch (_: Exception) {
                mutableEvents.send(SplashEvent.NavigateToLogin)
            }
        }
    }

    sealed class SplashEvent {
        data object NavigateToMain : SplashEvent()
        data object NavigateToLogin : SplashEvent()
    }
}