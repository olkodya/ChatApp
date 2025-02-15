package com.example.coincapapp.feature.coinDetail.domain

import kotlinx.coroutines.flow.Flow

interface GetCoinCurrentPriceUseCase {

    suspend operator fun invoke(coinId: String): Flow<CoinDetailEntity>
}
