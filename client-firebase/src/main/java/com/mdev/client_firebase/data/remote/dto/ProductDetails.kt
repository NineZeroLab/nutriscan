package com.mdev.client_firebase.data.remote.dto

import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import com.mdev.openfoodfacts_client.domain.model.NutrientCategory
import com.mdev.openfoodfacts_client.domain.model.NutrientType
import com.mdev.openfoodfacts_client.domain.model.PointsLevel

data class ProductDetails(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val nutrients: List<Nutrient> = emptyList(),
    val brand: String = "",
    val nutriScoreGrade: String = "",
    val categoriesHierarchy: List<String> = emptyList(),
    val allergensHierarchy: List<String> = emptyList(),
    val ingredientsAnalysis: List<String> = emptyList(),
    val additivesOriginalTags: List<String> = emptyList()
)


// Move to Common
data class Nutrient(
    val nutrientType: NutrientType? = null,
    val contentPerHundredGram: Number = 0,
    val description: String = "",
    val pointsLevel: PointsLevel = PointsLevel.UNKNOWN,
    val nutrientCategory: NutrientCategory = NutrientCategory.UNKNOWN,
    val healthCategory: HealthCategory,
    val servingUnit: String,
    )