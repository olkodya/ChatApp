package com.example.chatapp.di

import android.content.Context
import com.example.chatapp.feature.authorization.data.AuthPreferences
import com.example.chatapp.feature.authorization.data.api.LoginApi
import com.example.chatapp.feature.chatList.data.api.ChatListApi
import com.example.chatapp.feature.profile.data.api.ProfileApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    private companion object {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideAuthPreferences(context: Context): AuthPreferences = AuthPreferences(context)

    @Singleton
    @Provides
    @Named("RestOkHttpClient")
    fun provideOkHttp(
        authInterceptor: AuthInterceptor,
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addInterceptor(networkInterceptor)
        .let {
            it.addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }
        .build()

    @Singleton
    @Provides
    @Named("WebSocketOkHttpClient")
    fun provideSocketOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        @Named("RestOkHttpClient") okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://eltex2025.rocket.chat/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

    @Provides
    fun provideLoginApi(retrofit: Retrofit): LoginApi = retrofit.create()

    @Provides
    fun providesProfileApi(retrofit: Retrofit): ProfileApi = retrofit.create()


    @Provides
    fun providesChatListApi(retrofit: Retrofit): ChatListApi = retrofit.create()
}
