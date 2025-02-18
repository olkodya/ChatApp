package com.example.chatapp.feature.authorization.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.authorization.domain.CheckAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkAuthUseCase: CheckAuthUseCase,
    private val authPreferences: AuthPreferences // добавляем AuthPreferences

) : ViewModel() {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    init {
        checkAuth()
        checkAuthData()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            _isAuthenticated.value = checkAuthUseCase()
            Log.d("AuthViewModel", isAuthenticated.value.toString())
        }
    }

    private fun checkAuthData() {
        viewModelScope.launch {
            val authData = authPreferences.authData.first()
            Log.d("AuthViewModel", "AuthData: $authData")
            Log.d("AuthViewModel", "Token: ${authData?.token}")
            Log.d("AuthViewModel", "UserId: ${authData?.userId}")
        }
    }
}