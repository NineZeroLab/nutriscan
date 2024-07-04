package com.zero1labs.nutriscan.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero1labs.nutriscan.data.models.MainDetailsForView
import com.zero1labs.nutriscan.data.models.remote.Product
import com.zero1labs.nutriscan.data.models.SearchHistoryListItem
import com.zero1labs.nutriscan.repository.AppRepository
import com.zero1labs.nutriscan.utils.NetworkUtils
import com.zero1labs.nutriscan.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class ProductDetailsState(
    val product: Product? = null,
    val error: String? = null,
    val productScanState: ProductScanState = ProductScanState.NotStarted,
    val searchHistory : List<SearchHistoryListItem>  = mutableListOf(),
    val isOnline: Boolean = true
)

enum class ProductScanState{
    Success,
    Failure,
    Loading,
    NotStarted,
}

@HiltViewModel
class AppViewModel @Inject constructor (
    private val networkUtils: NetworkUtils,
    private val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailsState())
    val uiState : StateFlow<ProductDetailsState> = _uiState.asStateFlow()

    fun onEvent(event : AppEvent) {
        when (event) {
            is AppEvent.GetProductDetails -> TODO()
            is AppEvent.OnStartScan -> {
                Log.d("logger", "network connection : ${networkUtils.isNetworkAvailable()}")
                    viewModelScope.launch {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isOnline = networkUtils.isNetworkAvailable()
                            )
                        }
                }
                if (!uiState.value.isOnline) return
                viewModelScope.launch {
                    _uiState.update { currentState ->
                        currentState.copy(
                            productScanState = ProductScanState.Loading
                        )
                    }
                    val response: Resource<Product> =
                        appRepository.getProductDetailsById(event.productId)
                    when (response) {
                        is Resource.Success -> {
                            _uiState.update { currentState ->
                                Log.d("logger","product scan success from viewmodel")
                                val searchHistoryListItem = response.data?.let { product ->
                                    SearchHistoryListItem(
                                        mainDetailsForView = MainDetailsForView.getMainDetailsForView(product),
                                        timeStamp = LocalDateTime.now()
                                    )
                                }
                                currentState.copy(
                                    productScanState = ProductScanState.Success,
                                    product = response.data,
                                    searchHistory = currentState.searchHistory.toMutableList().apply {
                                        if (searchHistoryListItem != null) {
                                            add(element = searchHistoryListItem, index = 0)
                                        }
                                    }
                                )
                            }
                        }
                        is Resource.Error -> _uiState.update { currentState ->
                            Log.d("logger","product scan failure from viewmodel")
                            currentState.copy(
                                productScanState = ProductScanState.Failure,
                                error = response.message,
                            )
                        }
                    }
                    _uiState.update { currentState ->
                        currentState.copy(
                            productScanState = ProductScanState.NotStarted
                        )
                    }
                }
            }
        }
    }


}