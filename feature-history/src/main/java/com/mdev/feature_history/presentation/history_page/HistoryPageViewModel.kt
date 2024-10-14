package com.mdev.feature_history.presentation.history_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import com.mdev.common.utils.Resource
import com.mdev.feature_history.domain.usecases.GetSearchHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class HistoryPageState(
    val searchHistory: List<ProductDetailsDto> = mutableListOf()
)

@HiltViewModel
class HistoryPageViewModel @Inject constructor(
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(HistoryPageState())
    val uiState = _uiState.asStateFlow()

    init {
        collectSearchHistory()
    }

    private fun collectSearchHistory() {
        getSearchHistoryUseCase().onEach { result ->
            when(result){
                is Resource.Success -> {
                    result.data?.let { searchHistory ->
                        _uiState.update {
                            HistoryPageState(
                                searchHistory = searchHistory
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        HistoryPageState()
                    }
                }
                is Resource.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }
}