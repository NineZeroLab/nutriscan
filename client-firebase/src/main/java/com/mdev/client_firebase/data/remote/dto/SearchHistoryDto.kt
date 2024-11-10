package com.mdev.client_firebase.data.remote.dto

import com.google.firebase.Timestamp
import com.mdev.openfoodfacts_client.data.remote.dto.AdditiveDto
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import com.mdev.openfoodfacts_client.domain.model.NutrientDetails
import com.mdev.openfoodfacts_client.domain.model.ProductDetails

data class SearchHistoryItem(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val nutrients: List<NutrientDetails> = emptyList(),
    val brand: String = "",
    val nutriScoreGrade: String = "",
    val categoriesHierarchy: List<String> = emptyList(),
    val allergensHierarchy: List<Allergen> = emptyList(),
    val ingredientsAnalysis: List<DietaryRestriction> = emptyList(),
    val additivesOriginalTags: List<AdditiveDto> = emptyList(),
    val timestamp: Timestamp = Timestamp.now(),
)


fun ProductDetails.toSearchHistoryItem(): SearchHistoryItem {
    return SearchHistoryItem(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        nutrients = this.nutrients,
        brand = this.brand,
        nutriScoreGrade = this.nutriScoreGrade,
        categoriesHierarchy = this.categoriesHierarchy,
        allergensHierarchy = this.allergensHierarchy,
        ingredientsAnalysis = this.ingredientsAnalysis,
        additivesOriginalTags = this.additivesOriginalTags,
        timestamp = Timestamp.now()
    )
}