package com.mdev.feature_scan.domain.usecase

import com.mdev.common.utils.Resource
import com.mdev.feature_scan.domain.model.ScanItemForView
import com.mdev.feature_scan.domain.model.toScanItemForView
import com.mdev.feature_scan.domain.repository.ScanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetProductDetailsUseCase @Inject constructor(
    private val scanRepository: ScanRepository
) {
    operator fun invoke(productId: String): Flow<Resource<ScanItemForView>> = flow{
        emit(Resource.Loading())
        try {
            val productDetails = scanRepository.getProductDetails(productId)
            if (productDetails == null){
                emit(Resource.Error("Unable to fetch product Details :("))
            }else{
                scanRepository.addProductToSearchHistory(productDetails)
                emit(Resource.Success(productDetails.toScanItemForView()))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}