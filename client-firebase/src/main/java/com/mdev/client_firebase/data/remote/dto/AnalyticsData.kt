package com.mdev.client_firebase.data.remote.dto

import com.mdev.openfoodfacts_client.domain.model.NutrientType
import com.mdev.openfoodfacts_client.domain.model.ProductDetails

data class AnalyticsData(
    val scannedItems: Int = 0,
    val topCategories: Map<String, Int> = getDefaultTopCategories(),
    val averageNutrientPerProduct: Map<NutrientType,Double> = emptyMap()
)


internal fun getDefaultTopCategories(): Map<String, Int> {
    return mapOf(
        "en:snacks" to 1,
    )
}


internal fun List<ProductDetails>.calculateAnalytics(): AnalyticsData{
    val topCategories = this.flatMap { it.categoriesHierarchy }
        .groupingBy { it }
        .eachCount()

    val averageNutrients = mutableMapOf<NutrientType,Double>()
    this.forEach { product ->
        product.nutrients.forEach { nutrient ->
            nutrient.nutrientType?.let {nutrientType ->
                averageNutrients[nutrientType] = averageNutrients
                    .getOrDefault(nutrientType,0.0)
                    .plus(nutrient.contentPerHundredGram)
            }
        }
    }
    return AnalyticsData(
        scannedItems = this.size,
        topCategories = topCategories,
        averageNutrientPerProduct = averageNutrients
    )
}