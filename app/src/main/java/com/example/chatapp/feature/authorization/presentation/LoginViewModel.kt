package com.example.chatapp.feature.authorization.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.feature.authorization.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val mutableLoginState = MutableStateFlow(LoginScreenState())
    val loginState: StateFlow<LoginScreenState> = mutableLoginState.asStateFlow()

    private val mutableActions = Channel<LoginEvent>()
    val action = mutableActions.receiveAsFlow()

    fun handleAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            is LoginAction.OnLoginFieldChanged -> updateLoginField(action.value)
            is LoginAction.OnPasswordFieldChanged -> updatePasswordField(action.value)
        }
    }

    private fun updateLoginField(value: String) {
        mutableLoginState.value = loginState.value.copy(
            login = value,
        )
    }

    private fun updatePasswordField(value: String) {
        mutableLoginState.value = loginState.value.copy(
            password = value,
        )
    }

    private fun login() {
        try {
            viewModelScope.launch {
                loginUseCase(loginState.value.login,
                    loginState.value.password
                ).onSuccess{
                    mutableActions.send(LoginEvent.NavigateToMain)
                    mutableLoginState.value = loginState.value.copy(
                        error = null
                    )
                }.onFailure { error ->
                    mutableLoginState.value = loginState.value.copy(
                        error = error.message
                    )
                }
            }
        } finally {
            mutableLoginState.value = loginState.value.copy(isLoading = false)
        }

    }

    sealed class LoginAction {
        data class OnLoginFieldChanged(val value: String) : LoginAction()
        data class OnPasswordFieldChanged(val value: String) : LoginAction()
        data object OnLoginClick : LoginAction()
    }

    sealed class LoginEvent {
        data object NavigateToMain : LoginEvent()
    }
}
