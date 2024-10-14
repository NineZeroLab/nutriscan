package com.mdev.openfoodfacts_client.domain.repository

import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto

interface ProductRepository{
    suspend fun getProductDetailsById(productId: String): ProductDto?
}