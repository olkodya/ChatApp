package com.example.coincapapp.feature.coinDetail.di

import com.example.coincapapp.feature.coinDetail.domain.GetCoinPriceHistoryUseCase
import com.example.coincapapp.feature.coinDetail.domain.GetCoinPriceHistoryUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@InstallIn(ViewModelComponent::class)
@Module
interface CoinGetCoinPriceHistoryUseCaseModule {

    @Binds
    fun bindCoinGetCoinPriceHistoryUseCase(impl: GetCoinPriceHistoryUseCaseImpl): GetCoinPriceHistoryUseCase
}
