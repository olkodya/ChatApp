package com.example.chatapp.feature.coinList.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.chatapp.feature.coinList.data.CoinRepository
import com.example.chatapp.feature.coinList.domain.paging.CoinListPagingSource
import com.example.chatapp.feature.coinList.presentation.CoinState
import com.example.chatapp.feature.coinList.presentation.toState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCoinListUseCaseImpl @Inject constructor(
    private val repository: CoinRepository,
) : GetCoinListUseCase {

    override suspend operator fun invoke(searchQuery: String): Flow<PagingData<CoinState>> {
        val config = PagingConfig(
            initialLoadSize = 100,
            pageSize = 100,
            enablePlaceholders = false,
            prefetchDistance = 10,
        )

        return Pager(config) {
            CoinListPagingSource(
                repository = repository,
                searchQuery = searchQuery,
            )
        }.flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toState()
            }
        }
    }
}
