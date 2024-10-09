package com.mdev.openfoodfacts_client.data.repository

import com.mdev.openfoodfacts_client.data.remote.OpenFoodFactsApi
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import com.mdev.openfoodfacts_client.utils.ResponseFields


class ProductRepositoryImpl (
    private val openFoodFactsApi: OpenFoodFactsApi
): ProductRepository {
    override suspend fun getProductDetailsById(productId: String): ProductDto? {
        val searchResponse = openFoodFactsApi.getProductDetails(
            productId = productId,
            fields = ResponseFields.getFields()
        )
        return searchResponse.products.getOrNull(0)
    }
}