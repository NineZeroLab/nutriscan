package com.mdev.feature_product_details.presentation.profilePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.common.utils.Resource
import com.mdev.common.utils.domain.model.Status
import com.mdev.core.utils.logger
import com.mdev.feature_product_details.domain.model.ProductDetails
import com.mdev.feature_product_details.domain.model.UserConclusion
import com.mdev.feature_product_details.domain.usecases.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal data class ProductDetailsPageState(
    val productDetailsFetchState: Status = Status.IDLE,
    val productDetails: ProductDetails? = null,
    val errorMessage: String? = null,
    val userConclusion: UserConclusion? = null
)


@HiltViewModel
internal class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailsPageState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ProductDetailsPageEvent){
        when(event){
            is ProductDetailsPageEvent.GetProductDetails -> getProductDetails(event.productId)
        }
    }

    private fun getProductDetails(productId: String) {
        logger("fetching product details from viewModel...")
        getProductDetailsUseCase(productId).onEach { result ->
            when(result){
                is Resource.Error -> {
                    _uiState.update {
                        ProductDetailsPageState(
                            productDetailsFetchState = Status.FAILURE,
                            errorMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update {
                        ProductDetailsPageState(
                            productDetailsFetchState = Status.LOADING
                        )
                    }
                }
                is Resource.Success -> {
                    _uiState.update {
                        ProductDetailsPageState(
                            productDetailsFetchState = Status.SUCCESS,
                            productDetails = result.data?.productDetails,
                            userConclusion = result.data?.userConclusion
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}