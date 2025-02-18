package com.example.chatapp.feature.authorization.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class LoginScreenState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: LoginError? = null,
){
    val isEmpty = login.isEmpty() || password.isEmpty()
}

sealed class LoginError {
    object NetworkError : LoginError()
    object InvalidCredentials : LoginError()

    fun getMessage(): String = when (this) {
        NetworkError -> "Отсутствует соединение с сервером, проверьте ваше интернет соединение и повторите позднее."
        InvalidCredentials -> "Неправильный логин или пароль."
    }
}

class InvalidCredentialsException : Exception()