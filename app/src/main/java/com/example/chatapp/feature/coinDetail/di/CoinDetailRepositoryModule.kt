package com.example.chatapp.feature.coinDetail.di

import com.example.chatapp.feature.coinDetail.data.CoinDetailRepository
import com.example.chatapp.feature.coinDetail.data.CoinDetailRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface CoinDetailRepositoryModule {

    @Binds
    fun bindCoinDetailRepository(impl: CoinDetailRepositoryImpl): CoinDetailRepository
}
