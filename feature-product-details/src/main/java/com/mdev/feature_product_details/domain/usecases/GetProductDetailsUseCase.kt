package com.mdev.feature_product_details.domain.usecases

import com.mdev.common.utils.Resource
import com.mdev.feature_product_details.domain.model.ProductDetailsForView
import com.mdev.feature_product_details.domain.model.getProductConsiderations
import com.mdev.feature_product_details.domain.model.toProductDetails
import com.mdev.feature_product_details.domain.repository.ProductDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetProductDetailsUseCase @Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository,
    private val calculateProductConclusionUseCase: CalculateProductConclusionUseCase,
) {
    operator fun invoke(productId: String): Flow<Resource<ProductDetailsForView>> = flow {
        emit(Resource.Loading())
        try {
            val product = productDetailsRepository.getProductDetails(productId)
            if (product == null){
                emit(Resource.Error("Product Not Found"))
            }else{
                val productDetails = product.toProductDetails()
                val productConsiderations = product.getProductConsiderations()
                val userConclusion = calculateProductConclusionUseCase(productConsiderations)
                val productDetailsForView = ProductDetailsForView(
                    productDetails = productDetails,
                    userConclusion = userConclusion
                )
                emit(Resource.Success(productDetailsForView))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}



