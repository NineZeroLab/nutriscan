package com.mdev.feature_product_details.domain.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto

interface ProductDetailsRepository {
    suspend fun getProductDetails(productId: String): ProductDto?
    suspend fun getUserPreference(): AppUser?
}