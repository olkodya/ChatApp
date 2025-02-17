package com.example.chatapp.feature.authorization.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chatapp.feature.authorization.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel(){

    private val mutableLoginState = MutableStateFlow(LoginScreenState())
    val loginState = mutableLoginState.asStateFlow()

    private val mutableActions = Channel<LoginEvent>()

    sealed class LoginAction {
        data class OnLoginFieldChanged(val value: String) : LoginAction()
        data class OnPasswordFieldChanged(val value: String) : LoginAction()
        data object OnLoginClick : LoginAction()
    }

    sealed class LoginEvent {
        data object NavigateToMain : LoginEvent()
    }

}