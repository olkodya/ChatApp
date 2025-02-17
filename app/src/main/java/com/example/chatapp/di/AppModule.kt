package com.example.chatapp.di

import com.example.chatapp.feature.coinDetail.data.api.CoinDetailApi
import com.example.chatapp.feature.coinList.data.model.api.CoinApi
import com.example.chatapp.feature.exchangeList.data.api.ExchangeApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    private companion object {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
    }

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor {
            it.proceed(
                it.request()
                    .newBuilder()
//                    .addHeader("Api-Key", BuildConfig.API_KEY)
//                    .addHeader("Authorization", BuildConfig.AUTH_TOKEN)
                    .build()
            )
        }
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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.coincap.io/v2/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

    @Provides
    fun provideCoinApi(retrofit: Retrofit): CoinApi = retrofit.create()

    @Provides
    fun provideExchangeApi(retrofit: Retrofit): ExchangeApi = retrofit.create()

    @Provides
    fun provideCoinDetailApi(retrofit: Retrofit): CoinDetailApi = retrofit.create()

}



