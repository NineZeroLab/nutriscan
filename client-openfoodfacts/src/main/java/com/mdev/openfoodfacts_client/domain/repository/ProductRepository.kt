package com.mdev.openfoodfacts_client.domain.repository

import com.mdev.openfoodfacts_client.data.remote.dto.AdditiveDto
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import com.mdev.openfoodfacts_client.domain.model.ProductDetails

interface ProductRepository{
    suspend fun getProductDetailsDtoById(productId: String): ProductDto?
    suspend fun getProductDetailsById(productId: String): ProductDetails?
    fun getAdditivesByENumber(eNumber: List<String>): List<AdditiveDto>
    suspend fun getRecommendedProducts(
        dietaryRestrictions: List<DietaryRestriction>,
        allergens: List<Allergen>,
        ): List<RecommendedProductDto>?
}
