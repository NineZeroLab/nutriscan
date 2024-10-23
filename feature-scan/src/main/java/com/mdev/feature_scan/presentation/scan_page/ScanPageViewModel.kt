package com.mdev.feature_scan.presentation.scan_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.common.utils.Resource
import com.mdev.common.utils.domain.model.Status
import com.mdev.core.utils.logger
import com.mdev.feature_scan.domain.model.ProductDetailsForView
import com.mdev.feature_scan.domain.usecase.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal data class ScanPageState(
    val scannedProducts: List<ProductDetailsForView> = emptyList(),
    val errorMessage: String? = null,
    val scanState: Status = Status.IDLE
)

@HiltViewModel
internal class ScanPageViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ScanPageState())
    val uiState = _uiState.asStateFlow()


    fun onEvent(event: ScanPageEvent){
        when(event){
            is ScanPageEvent.GetProductDetails -> getProductDetails(event.productId)
        }
    }

    private fun getProductDetails(productId: String) {

        getProductDetailsUseCase(productId).onEach { result ->
            when(result){
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            scanState = Status.FAILURE,
                            errorMessage = result.message
                        )
                    }
                    _uiState.update {
                        it.copy(
                            scanState = Status.IDLE
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(
                            scanState = Status.LOADING
                        )
                    }
                }
                is Resource.Success -> {
                    logger("found product ${result.data?.name}")
                    val productsList = _uiState.value.scannedProducts
                        .toMutableList()
                    result.data?.let {
                        productsList.add(it)
                    }
                    _uiState.update { state ->
                        state.copy(
                            scanState = Status.SUCCESS,
                            scannedProducts = productsList
                        )
                    }
                    _uiState.update {
                        it.copy(
                            scanState = Status.IDLE
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

}