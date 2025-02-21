package com.example.chatapp.di

import com.example.chatapp.di.model.NetworkException
import com.example.chatapp.di.model.ServerException
import com.example.chatapp.di.model.UnauthorizedException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = try {
            chain.proceed(chain.request())
        } catch (exception: Exception) {
            throw when (exception) {
                is UnknownHostException,
                is SocketTimeoutException,
                is IOException -> NetworkException(exception)

                else -> exception
            }
        }

        when (response.code) {
            401 -> throw UnauthorizedException()
            in 500..599 -> throw ServerException()
        }
        return response
    }
}
