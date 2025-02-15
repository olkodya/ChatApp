package com.example.coincapapp.feature.exchangeList.di

import com.example.coincapapp.feature.exchangeList.domain.GetExchangeListUseCase
import com.example.coincapapp.feature.exchangeList.domain.GetExchangeListUseCaseImpl
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
