package com.example.chatapp.feature.coinList.data

import com.example.chatapp.feature.coinList.data.model.CoinListResponse

interface CoinRepository {

    suspend fun loadPagingCoins(
        searchQuery: String,
        offset: Int,
        limit: Int,
    ): CoinListResponse
}
