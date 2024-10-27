package com.mdev.feature_scan.domain.usecase

import com.mdev.common.utils.Resource
import com.mdev.feature_scan.data.model.toProductDetailsForViewDto
import com.mdev.feature_scan.domain.model.ProductDetailsForView
import com.mdev.feature_scan.domain.model.toProductDetailsForView
import com.mdev.feature_scan.domain.repository.ScanRepository
import com.mdev.openfoodfacts_client.domain.model.toProductDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetProductDetailsUseCase @Inject constructor(
    private val scanRepository: ScanRepository
) {
    operator fun invoke(productId: String): Flow<Resource<ProductDetailsForView>> = flow{
        emit(Resource.Loading())
        try {
            val productDetails = scanRepository.getProductDetails(productId)
            if (productDetails == null){
                emit(Resource.Error("Unable to fetch product Details :("))
            }else{
                scanRepository.addProductToSearchHistory(productDetails)
                emit(Resource.Success(productDetails.toProductDetailsForView()))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}