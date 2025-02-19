package com.example.chatapp.di

import com.example.chatapp.feature.authorization.data.AuthPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val authToken = runBlocking { authPreferences.authData.first()?.token }
        val userId = runBlocking { authPreferences.authData.first()?.userId }
        val newRequest = if (!authToken.isNullOrEmpty()) {
            request.newBuilder()
                .addHeader("X-Auth-Token", authToken)
                .addHeader("X-User-Id", userId ?: "")
                .build()
        } else {
            request
        }
        return chain.proceed(newRequest)
    }
}