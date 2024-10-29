package com.mdev.feature_scan.domain.repository

import com.mdev.feature_scan.data.model.ProductDetailsForViewDto
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.domain.model.ProductDetails

interface ScanRepository {
    suspend fun getProductDetails(productId: String): ProductDetails?
    suspend fun addProductToSearchHistory(productDetails: ProductDetails)
}