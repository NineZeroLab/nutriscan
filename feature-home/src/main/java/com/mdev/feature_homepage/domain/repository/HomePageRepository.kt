package com.mdev.feature_homepage.domain.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.data.remote.dto.SearchHistoryItem
import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import kotlinx.coroutines.flow.Flow

interface HomePageRepository {
    suspend fun getSearchHistory(): Flow<List<SearchHistoryItem>>
    suspend fun getRecommendedProducts(
        categories: List<String>,
        dietaryRestrictions: List<DietaryRestriction>,
        allergens: List<Allergen>
    ): List<RecommendedProductDto>?
    suspend fun getUserDetails(): AppUser?
    suspend fun logOut()
}