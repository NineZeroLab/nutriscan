package com.mdev.feature_product_details.domain.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction

internal interface ProductDetailsRepository {
    suspend fun getProductDetails(productId: String): ProductDto?
    suspend fun getUserPreference(): AppUser?
    suspend fun getRecommendedProducts(
        categories: List<String>,
        dietaryRestrictions: List<DietaryRestriction>,
        allergens: List<Allergen>
    ): List<RecommendedProductDto>?
}