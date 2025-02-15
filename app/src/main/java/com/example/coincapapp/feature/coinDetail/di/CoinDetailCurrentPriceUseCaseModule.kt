package com.example.coincapapp.feature.coinDetail.di

import com.example.coincapapp.feature.coinDetail.domain.GetCoinCurrentPriceUseCase
import com.example.coincapapp.feature.coinDetail.domain.GetCoinCurrentPriceUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@InstallIn(ViewModelComponent::class)
@Module
interface CoinDetailCurrentPriceUseCaseModule {

    @Binds
    fun bindCoinDetailCurrentPriceUseCaseModule(impl: GetCoinCurrentPriceUseCaseImpl): GetCoinCurrentPriceUseCase
}
