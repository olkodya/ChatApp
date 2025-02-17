package com.example.chatapp.feature.coinList.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.chatapp.feature.coinList.data.CoinRepository
import com.example.chatapp.feature.coinList.data.model.toEntity
import com.example.chatapp.feature.coinList.domain.entities.CoinEntity

class CoinListPagingSource(
    private val repository: CoinRepository,
    private val searchQuery: String
) : PagingSource<Int, CoinEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinEntity> {
        return try {
            val nextPage = params.key ?: 1
            val response = repository.loadPagingCoins(
                searchQuery = searchQuery,
                limit = params.loadSize,
                offset = (nextPage - 1) * params.loadSize,
            ).data
            val entities = response.map {
                it.toEntity()
            }
            LoadResult.Page(
                data = entities,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CoinEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
