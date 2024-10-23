package com.mdev.feature_scan.domain.usecase

import com.mdev.common.utils.Resource
import com.mdev.feature_scan.data.model.toProductDetailsForViewDto
import com.mdev.feature_scan.domain.model.ProductDetailsForView
import com.mdev.feature_scan.domain.model.toProductDetailsForView
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetProductDetailsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(productId: String): Flow<Resource<ProductDetailsForView>> = flow{
        emit(Resource.Loading())
        try {
            val product = productRepository.getProductDetailsById(productId)
            val productDetailsDto = product?.toProductDetailsForViewDto()
            //TODO: send product details to firebase
            if (productDetailsDto == null){
                emit(Resource.Error("Unable to fetch product Details :("))
            }else{
                emit(Resource.Success(productDetailsDto.toProductDetailsForView()))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}