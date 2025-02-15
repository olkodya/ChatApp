package com.example.coincapapp.feature.coinDetail.domain

import com.example.coincapapp.feature.coinDetail.data.CoinDetailRealtimeClient
import com.example.coincapapp.feature.coinDetail.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCoinCurrentPriceUseCaseImpl @Inject constructor(
    private val client: CoinDetailRealtimeClient
) : GetCoinCurrentPriceUseCase {

    override suspend fun invoke(coinId: String): Flow<CoinDetailEntity> =
        client.getCoinCurrentPrice(coinId = coinId).map { it.toEntity(0) }

}
