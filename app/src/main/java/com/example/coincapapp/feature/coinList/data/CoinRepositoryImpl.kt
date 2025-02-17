package com.example.coincapapp.feature.coinList.data

import com.example.coincapapp.feature.coinList.data.model.CoinListResponse
import com.example.coincapapp.feature.coinList.data.model.api.CoinApi
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinApi
) : CoinRepository {

    override suspend fun loadPagingCoins(
        searchQuery: String,
        offset: Int,
        limit: Int,
    ): CoinListResponse = api.loadPagingCoins(searchQuery, offset, limit)
}
