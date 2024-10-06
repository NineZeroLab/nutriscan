package com.mdev.openfoodfacts_client.repository

import com.mdev.core.utils.Resource
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import kotlinx.coroutines.flow.Flow

interface ProductRepository{
    suspend fun getProductDetailsById(productId: String): Resource<ProductDto>
}