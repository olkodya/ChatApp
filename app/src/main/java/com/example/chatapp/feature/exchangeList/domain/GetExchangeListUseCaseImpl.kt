package com.example.chatapp.feature.exchangeList.domain

import com.example.chatapp.feature.exchangeList.data.ExchangeRepository
import com.example.chatapp.feature.exchangeList.data.model.toEntity
import com.example.chatapp.feature.exchangeList.domain.entities.ExchangeEntity
import javax.inject.Inject

class GetExchangeListUseCaseImpl @Inject constructor(
    private val repository: ExchangeRepository,
) : GetExchangeListUseCase {

    override suspend fun invoke(): List<ExchangeEntity> {
        val entities = repository.loadExchanges().data.map {
            it.toEntity()
        }
        return entities
    }
}
