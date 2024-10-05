package com.zero1labs.nutriscan.domain.repository

import com.zero1labs.nutriscan.data.remote.dto.ProductDto
import com.zero1labs.nutriscan.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository{
    suspend fun getProductDetailsById(productId: String): Flow<Resource<ProductDto>>
}