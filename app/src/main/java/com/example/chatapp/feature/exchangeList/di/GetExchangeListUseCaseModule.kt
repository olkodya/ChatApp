package com.example.chatapp.feature.exchangeList.di

import com.example.chatapp.feature.exchangeList.domain.GetExchangeListUseCase
import com.example.chatapp.feature.exchangeList.domain.GetExchangeListUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface GetExchangeListUseCaseModule {

    @Binds
    fun bindGetExchangeListUseCase(impl: GetExchangeListUseCaseImpl): GetExchangeListUseCase
}
