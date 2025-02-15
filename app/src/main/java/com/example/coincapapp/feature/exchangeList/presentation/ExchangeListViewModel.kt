package com.example.coincapapp.feature.exchangeList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coincapapp.feature.exchangeList.domain.GetExchangeListUseCase
import com.example.coincapapp.feature.exchangeList.domain.entities.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeListViewModel @Inject constructor(
    private val getExchangeListUseCase: GetExchangeListUseCase
) : ViewModel() {

    private val mutableExchangeListState =
        MutableStateFlow<ExchangeListState>(ExchangeListState.Loading)
    val exchangeListState = mutableExchangeListState.asStateFlow()

    private val mutableActions: Channel<ExchangeListEvent> = Channel()
    val action: Flow<ExchangeListEvent> = mutableActions.receiveAsFlow()

    init {
        loadExchanges()
    }

    fun handleAction(action: ExchangeListAction) {
        when (action) {
            is ExchangeListAction.OnExchangeUrlClicked -> openUrlInBrowser(action.exchangeUrl)
            ExchangeListAction.OnRetryClick -> loadExchanges()
            ExchangeListAction.OnSwipeToRefresh -> loadExchanges(true)
        }
    }

    private fun openUrlInBrowser(exchangeUrl: String) {
        viewModelScope.launch {
            mutableActions.send(
                ExchangeListEvent.OpenUrlInBrowser(exchangeUrl)
            )
        }
    }

    private fun loadExchanges(pullToRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                if (!pullToRefresh)
                    mutableExchangeListState.value = ExchangeListState.Loading
                else
                    mutableExchangeListState.value =
                        (exchangeListState.value as ExchangeListState.Content).copy(isRefreshing = true)
                val exchanges: List<ExchangeState> = getExchangeListUseCase().map {
                    it.toState()
                }
                mutableExchangeListState.value =
                    ExchangeListState.Content(
                        exchanges = exchanges.toImmutableList(),
                        isRefreshing = false
                    )
            } catch (ex: Exception) {
                mutableExchangeListState.value = ExchangeListState.Error(message = ex.message)
            }
        }
    }

    sealed class ExchangeListAction {
        data class OnExchangeUrlClicked(val exchangeUrl: String) : ExchangeListAction()
        data object OnRetryClick : ExchangeListAction()
        data object OnSwipeToRefresh : ExchangeListAction()
    }

    sealed class ExchangeListEvent {
        data class OpenUrlInBrowser(val exchangeUrl: String) : ExchangeListEvent()
    }
}
