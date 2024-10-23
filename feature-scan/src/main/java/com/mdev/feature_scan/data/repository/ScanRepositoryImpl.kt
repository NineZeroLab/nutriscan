package com.mdev.feature_scan.data.repository

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_scan.data.model.ProductDetailsForViewDto
import com.mdev.feature_scan.data.model.toProductDetailsForViewDto
import com.mdev.feature_scan.domain.repository.ScanRepository
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository

class ScanRepositoryImpl(
    private val productRepository: ProductRepository,
    private val firebaseRepository: FirebaseRepository
): ScanRepository {
    override suspend fun getProductDetails(productId: String): ProductDetailsForViewDto? {
        return productRepository.getProductDetailsById(productId)?.toProductDetailsForViewDto()
    }


}