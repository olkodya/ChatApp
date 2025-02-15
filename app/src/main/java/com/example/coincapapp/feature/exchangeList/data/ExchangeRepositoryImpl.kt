package com.example.coincapapp.feature.exchangeList.data

import com.example.coincapapp.feature.exchangeList.data.model.ExchangeListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val client: HttpClient,
) : ExchangeRepository {

    override suspend fun loadExchanges(): ExchangeListResponse = client
        .get("exchanges")
        .body()
}
