package com.example.chatapp.di

import androidx.core.net.toUri
import coil3.intercept.Interceptor
import coil3.request.ImageResult
import com.example.chatapp.feature.authorization.data.AuthPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoilInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences,
) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val authToken = runBlocking { authPreferences.authData.first()?.token }
        val userId = runBlocking { authPreferences.authData.first()?.userId }
        val newUri = request.data.toString().toUri()
            .buildUpon()
            .appendQueryParameter("rc_uid", userId)
            .appendQueryParameter("rc_token", authToken)
            .build()
        val newRequest =
            request.newBuilder()
                .data(newUri)
                .build()
        return chain.withRequest(newRequest).proceed()
    }
}
