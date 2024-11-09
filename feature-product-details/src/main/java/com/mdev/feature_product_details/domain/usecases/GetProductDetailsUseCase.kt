package com.mdev.feature_product_details.domain.usecases

import com.mdev.common.utils.Resource
import com.mdev.core.utils.logger
import com.mdev.feature_product_details.domain.model.ProductDetailsForView
import com.mdev.feature_product_details.domain.model.Considerations
import com.mdev.feature_product_details.domain.model.getProductConsiderations
import com.mdev.feature_product_details.domain.model.toProductDetails
import com.mdev.feature_product_details.domain.repository.ProductDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetProductDetailsUseCase @Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository,
    private val getUserConsiderationsUseCase: GetUserConsiderationsUseCase
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
                val userConsiderations = getUserConsiderationsUseCase() ?: Considerations()
                val productDetailsForView = ProductDetailsForView(
                    productDetails = productDetails,
                    productConsiderations = productConsiderations,
                    userConsiderations = userConsiderations
                )
                emit(Resource.Success(productDetailsForView))
            }
        }catch (e: Exception){
            logger("Error: ${e.message}")
            emit(Resource.Error(e.message.toString()))
        }
    }
}



