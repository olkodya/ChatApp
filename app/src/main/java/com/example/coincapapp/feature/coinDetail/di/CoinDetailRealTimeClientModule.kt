package com.example.coincapapp.feature.coinDetail.di

import com.example.coincapapp.feature.coinDetail.data.CoinDetailRealtimeClient
import com.example.coincapapp.feature.coinDetail.data.CoinDetailRealtimeClientImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface CoinDetailRealTimeClientModule {

    @Binds
    fun bindCoinDetailRealTimeModule(impl: CoinDetailRealtimeClientImpl): CoinDetailRealtimeClient
}
