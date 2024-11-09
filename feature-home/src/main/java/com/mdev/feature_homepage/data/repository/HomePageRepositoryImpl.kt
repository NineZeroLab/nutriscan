package com.mdev.feature_homepage.data.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import com.mdev.openfoodfacts_client.domain.model.ProductDetails
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class HomePageRepositoryImpl @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val productRepository: ProductRepository
): HomePageRepository {
    override suspend fun getSearchHistory(): Flow<List<ProductDetails>> {
        return firebaseRepository.getSearchHistoryWithDetails().map {
            it
        }
    }

    override suspend fun getRecommendedProducts(
        categories: List<String>,
        dietaryRestrictions: List<DietaryRestriction>,
        allergens: List<Allergen>
    ): List<RecommendedProductDto>? {
        return productRepository.getRecommendedProducts(
            categories = categories,
            dietaryRestrictions = dietaryRestrictions,
            allergens = allergens
        )
    }

    override suspend fun getUserDetails(): AppUser? {
        return firebaseRepository.getCurrentUserDetails()
    }

    override suspend fun logOut() {
        firebaseRepository.logout()
    }
}