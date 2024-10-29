package com.mdev.feature_product_details.presentation.product_details_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.common.utils.Resource
import com.mdev.common.utils.domain.model.Status
import com.mdev.core.utils.logger
import com.mdev.feature_product_details.domain.model.ProductDetails
import com.mdev.feature_product_details.domain.model.RecommendedProduct
import com.mdev.feature_product_details.domain.model.UserConclusion
import com.mdev.feature_product_details.domain.model.Considerations
import com.mdev.feature_product_details.domain.usecases.GetProductDetailsUseCase
import com.mdev.feature_product_details.domain.usecases.GetRecommendedProductsUseCase
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
    val userConclusion: UserConclusion? = null,
    val userConsiderations: Considerations? = null,
    val productConsiderations: Considerations? = null,
    val recommendedProductsFetchState: Status = Status.IDLE,
    val recommendedProducts: List<RecommendedProduct> = mutableListOf()
)


@HiltViewModel
internal class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val getRecommendedProductsUseCase: GetRecommendedProductsUseCase
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
                    result.data?.productDetails?.let {productDetails ->
                        getRecommendedProducts(productDetails.categories)
                    }
                    _uiState.update {
                        ProductDetailsPageState(
                            productDetailsFetchState = Status.SUCCESS,
                            productDetails = result.data?.productDetails,
                            userConsiderations = result.data?.userConsiderations,
                            productConsiderations = result.data?.productConsiderations
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getRecommendedProducts(categories: List<String>){
        logger("categories ${categories.toString()}")
        getRecommendedProductsUseCase(categories).onEach {result ->
            when(result){
                is Resource.Error -> {
                    logger("Error fetching product details: ${result.message.toString()}")
                    _uiState.update {
                        it.copy(
                            recommendedProductsFetchState = Status.FAILURE,
                            errorMessage = result.message
                        )
                    }
                    _uiState.update {
                        it.copy(
                            recommendedProductsFetchState = Status.IDLE
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(
                            recommendedProductsFetchState = Status.LOADING
                        )
                    }
                }
                is Resource.Success -> {
                    logger(result.data?.map {
                        it.name
                    }.toString())
                    result.data?.let { product ->
                        _uiState.update {
                            it.copy(
                                recommendedProductsFetchState = Status.SUCCESS,
                                recommendedProducts = product
                            )
                        }
                        _uiState.update {
                            it.copy(
                                recommendedProductsFetchState = Status.IDLE
                            )
                        }
                    }

                }
            }
        }.launchIn(viewModelScope)
    }
}