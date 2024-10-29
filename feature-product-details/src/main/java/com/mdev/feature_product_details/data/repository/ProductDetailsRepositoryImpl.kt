package com.mdev.feature_product_details.data.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_product_details.domain.repository.ProductDetailsRepository
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import javax.inject.Inject

internal class ProductDetailsRepositoryImpl @Inject constructor(
    private val  productRepository: ProductRepository,
    private val firebaseRepository: FirebaseRepository
): ProductDetailsRepository {
    override suspend fun getProductDetails(productId: String): ProductDto? {
        return productRepository.getProductDetailsDtoById(productId)
    }

    override suspend fun getUserPreference(): AppUser? {
        return firebaseRepository.getCurrentUserDetails()
    }

    override suspend fun getRecommendedProducts(
        dietaryRestrictions: List<DietaryRestriction>,
        allergens: List<Allergen>
    ): List<RecommendedProductDto>? {
        return productRepository.getRecommendedProducts(
            dietaryRestrictions = dietaryRestrictions,
            allergens = allergens
        )
    }
}