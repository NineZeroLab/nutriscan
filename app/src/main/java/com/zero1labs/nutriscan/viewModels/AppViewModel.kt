package com.zero1labs.nutriscan.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero1labs.nutriscan.data.models.Product
import com.zero1labs.nutriscan.repository.AppRepository
import com.zero1labs.nutriscan.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductDetailsState(
    val product: Product? = null,
    val error: String? = null,
    val productScanState: ProductScanState = ProductScanState.NotStarted,
    val shouldNavigate :Boolean = false
)

enum class ProductScanState{
    Success,
    Failure,
    Loading,
    NotStarted,
}

@HiltViewModel
class AppViewModel @Inject constructor (
  private val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailsState())
    val uiState : StateFlow<ProductDetailsState> = _uiState.asStateFlow()

    fun setNavigation(value : Boolean){
        _uiState.update {
            it.copy(
                shouldNavigate = value
            )
        }
    }

    fun onEvent(event : AppEvent) {
        when (event) {

            is AppEvent.GetProductDetails -> TODO()
            is AppEvent.OnStartScan -> {
                viewModelScope.launch {
                    _uiState.update { currentState ->
                        currentState.copy(
                            productScanState = ProductScanState.Loading
                        )
                    }

                    val response: Resource<Product> =
                        appRepository.getProductDetailsById(event.productId)
                    when (response) {
                        is Resource.Success -> _uiState.update { currentState ->
                            Log.d("logger","product scan success from viewmodel")
                            currentState.copy(
                                productScanState = ProductScanState.Success,
                                product = response.data,
                                shouldNavigate = true
                            )
                        }

                        is Resource.Error -> _uiState.update { currentState ->
                            Log.d("logger","product scan failure from viewmodel")
                            currentState.copy(
                                productScanState = ProductScanState.Failure,
                                error = response.message,
                                shouldNavigate = true
                    )
                        }
                    }
                }
            }
        }
    }
}