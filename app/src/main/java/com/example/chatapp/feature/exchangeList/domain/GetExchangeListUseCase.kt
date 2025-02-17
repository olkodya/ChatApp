package com.example.chatapp.feature.exchangeList.domain

import com.example.chatapp.feature.exchangeList.domain.entities.ExchangeEntity

interface GetExchangeListUseCase {

    suspend operator fun invoke(): List<ExchangeEntity>
}
