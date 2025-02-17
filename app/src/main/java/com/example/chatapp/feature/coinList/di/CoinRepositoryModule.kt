package com.example.chatapp.feature.coinList.di

import com.example.chatapp.feature.coinList.data.CoinRepository
import com.example.chatapp.feature.coinList.data.CoinRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface CoinRepositoryModule {

    @Binds
    fun bindCoinRepository(impl: CoinRepositoryImpl): CoinRepository
}
