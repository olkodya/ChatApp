package com.example.chatapp.feature.authorization.di

import com.example.chatapp.feature.authorization.domain.LoginUseCase
import com.example.chatapp.feature.authorization.domain.LoginUseCaseImpl
import com.example.chatapp.feature.coinDetail.domain.GetCoinPriceHistoryUseCase
import com.example.chatapp.feature.coinDetail.domain.GetCoinPriceHistoryUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface LoginUseCaseModule {

    @Binds
    fun bindLoginUseCase(impl: LoginUseCaseImpl): LoginUseCase
}