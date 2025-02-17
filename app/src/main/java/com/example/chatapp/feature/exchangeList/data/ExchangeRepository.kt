package com.example.chatapp.feature.exchangeList.data

import com.example.chatapp.feature.exchangeList.data.model.ExchangeListResponse

interface ExchangeRepository {

    suspend fun loadExchanges(): ExchangeListResponse
}