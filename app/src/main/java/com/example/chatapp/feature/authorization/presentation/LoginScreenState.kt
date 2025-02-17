package com.example.chatapp.feature.authorization.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class LoginScreenState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
){
    val isEmpty = login == "" || password == ""
}