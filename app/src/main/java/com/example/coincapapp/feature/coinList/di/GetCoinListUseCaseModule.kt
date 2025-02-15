package com.example.coincapapp.feature.coinList.di

import com.example.coincapapp.feature.coinList.domain.GetCoinListUseCase
import com.example.coincapapp.feature.coinList.domain.GetCoinListUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@InstallIn(ViewModelComponent::class)
@Module
interface GetCoinListUseCaseModule {

    @Binds
    fun bindGetCoinListUseCase(impl: GetCoinListUseCaseImpl): GetCoinListUseCase
}
