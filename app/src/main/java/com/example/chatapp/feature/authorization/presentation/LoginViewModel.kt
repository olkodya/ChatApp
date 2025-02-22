package com.example.chatapp.feature.authorization.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.R
import com.example.chatapp.di.model.NetworkException
import com.example.chatapp.di.model.UnauthorizedException
import com.example.chatapp.feature.authorization.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.IOException
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
            mutableLoginState.value = loginState.value.copy(isLoading = true)
            viewModelScope.launch {
                loginUseCase(
                    loginState.value.login, loginState.value.password
                ).onSuccess {
                    mutableActions.send(LoginEvent.NavigateToMain)
                    mutableLoginState.value = loginState.value.copy(
                        isLoading = false,
                    )
                }.onFailure { throwable ->
                    val message = when (throwable) {
                        is UnauthorizedException -> R.string.login_incorrect_password_or_login
                        is NetworkException -> R.string.network_error_state_message
                        else -> R.string.unknown_error_state_message
                    }
                    mutableActions.send(LoginEvent.ShowSnackbar(message))
                    mutableLoginState.value = loginState.value.copy(isLoading = false)
                }
            }
        } finally {
        }
    }

    sealed class LoginAction {
        data class OnLoginFieldChanged(val value: String) : LoginAction()
        data class OnPasswordFieldChanged(val value: String) : LoginAction()
        data object OnLoginClick : LoginAction()
    }

    sealed class LoginEvent {
        data object NavigateToMain : LoginEvent()
        data class ShowSnackbar(@StringRes val message: Int) : LoginEvent()
    }
}
