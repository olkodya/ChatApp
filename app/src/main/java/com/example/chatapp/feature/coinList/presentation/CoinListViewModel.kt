package com.example.chatapp.feature.coinList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.chatapp.feature.coinList.domain.GetCoinListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CoinListViewModel @Inject constructor(
    val getCoinListUseCase: GetCoinListUseCase,
) : ViewModel() {

    private val mutableFieldState: MutableStateFlow<String> = MutableStateFlow("")
    val fieldState: StateFlow<String> = mutableFieldState.asStateFlow()

    private val mutableCoinsPagingState: MutableStateFlow<PagingData<CoinState>> =
        MutableStateFlow(PagingData.empty())
    val coinsPagingState: StateFlow<PagingData<CoinState>> = mutableCoinsPagingState.asStateFlow()

    private val mutableActions: Channel<CoinListEvent> = Channel()
    val action: Flow<CoinListEvent> = mutableActions.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        loadAssets(fieldState.value)
        viewModelScope.launch {
            fieldState
                .drop(1)
                .debounce(1_500)
                .collectLatest {
                    loadAssets(fieldState.value)
                }
        }
    }

    fun handleAction(action: CoinListAction) {
        when (action) {
            is CoinListAction.OnSearchFieldEdited -> fieldChanged(action.query)
            is CoinListAction.OnCoinClicked -> {
                viewModelScope.launch {
                    mutableActions.send(
                        CoinListEvent.NavigateToCoinDetail(
                            action.coinId, action.coinName, action.price
                        )
                    )
                }
            }
        }
    }

    private fun loadAssets(searchQuery: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            getCoinListUseCase(searchQuery)
                .cachedIn(viewModelScope)
                .collect {
                    mutableCoinsPagingState.value = it
                }
        }
    }

    private fun fieldChanged(query: String) {
        mutableFieldState.value = query
    }

    sealed class CoinListAction {
        data class OnSearchFieldEdited(val query: String) : CoinListAction()
        data class OnCoinClicked(
            val coinId: String,
            val coinName: String,
            val price: BigDecimal,
        ) : CoinListAction()
    }

    sealed class CoinListEvent {
        data class NavigateToCoinDetail(
            val coinId: String,
            val coinName: String,
            val price: BigDecimal,
        ) : CoinListEvent()
    }
}
