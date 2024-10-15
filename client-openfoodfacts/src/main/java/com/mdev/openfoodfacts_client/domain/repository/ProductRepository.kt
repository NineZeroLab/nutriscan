package com.mdev.openfoodfacts_client.domain.repository

import com.mdev.openfoodfacts_client.data.remote.dto.AdditiveDto
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction

interface ProductRepository{
    suspend fun getProductDetailsById(productId: String): ProductDto?
    fun getAdditiveByENumber(eNumber: String): AdditiveDto?
    suspend fun getRecommendedProducts(
        dietaryRestrictions: List<DietaryRestriction>,
        allergens: List<Allergen>,
        ): List<RecommendedProductDto>?
}
