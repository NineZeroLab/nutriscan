package com.mdev.feature_scan.domain.repository

import com.mdev.feature_scan.data.model.ProductDetailsForViewDto

interface ScanRepository {
    suspend fun getProductDetails(productId: String): ProductDetailsForViewDto?
}