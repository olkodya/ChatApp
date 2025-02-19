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
    private var _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            try {
                val isAuth = checkAuthUseCase()
                Log.d("AuthViewModel", "Initial auth check: $isAuth")
                _isAuthenticated.value = isAuth
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error in initial auth check", e)
                _isAuthenticated.value = false
                _isLoading.value = false
            }
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
