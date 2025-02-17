package com.example.chatapp.feature.coinList.di

import com.example.chatapp.feature.coinList.domain.GetCoinListUseCase
import com.example.chatapp.feature.coinList.domain.GetCoinListUseCaseImpl
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
