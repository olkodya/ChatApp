package com.example.chatapp.feature.coinDetail.domain

import com.example.chatapp.feature.coinDetail.data.CoinDetailRepository
import com.example.chatapp.feature.coinDetail.data.model.toEntity
import javax.inject.Inject

class GetCoinPriceHistoryUseCaseImpl @Inject constructor(
    private val repository: CoinDetailRepository
) : GetCoinPriceHistoryUseCase {

    override suspend fun invoke(coinId: String): List<CoinDetailEntity> {
        return repository.getCoinPricesHistory(coinId).data.mapIndexed { index, it ->
            it.toEntity(index)
        }
    }
}
