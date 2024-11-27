package com.mdev.client_firebase.data.remote.dto

import android.util.Log
import com.mdev.openfoodfacts_client.domain.model.NutrientType
import com.mdev.openfoodfacts_client.domain.model.ProductDetails

data class AnalyticsData(
    val userName: String = "",
    val scannedItems: Int = 0,
    val goodProducts: Int = 0,
    val badProducts: Int = 0,
    val topCategories: Map<String, Int> = emptyMap(),
    val averageNutrientPerProduct: Map<NutrientType?, Double> = emptyMap()
)




internal fun List<ProductDetails>.calculateAnalytics(userName: String): AnalyticsData{
    val topCategories = this.flatMap { it.categoriesHierarchy }
        .groupingBy { it }
        .eachCount()
        .filterKeys { it != "en:plant-based-foods-and-beverages" }

    Log.d("logger", "${topCategories.keys.map { it.trimCategoryString() }}")

    val averageNutrients = mutableMapOf<NutrientType,Double>()

    val allNutrients = this.flatMap { it.nutrients }
    val averageByNutrientType = allNutrients.groupBy { it.nutrientType }
        .mapValues { (_, nutrientList) ->
            nutrientList.map { it.contentPerHundredGram }.average()
        }

    averageByNutrientType.filterKeys { it != null }

    this.forEach { product ->
        product.nutrients.forEach { nutrient ->
            nutrient.nutrientType?.let {nutrientType ->
                averageNutrients[nutrientType] = averageNutrients
                    .getOrDefault(nutrientType,0.0)
                    .plus(nutrient.contentPerHundredGram.toDouble())
            }
        }
    }

    val goodProducts = this.filter { it.nutriScoreGrade == "a" || it.nutriScoreGrade == "b"}.size
    val badProducts = this.filter { it.nutriScoreGrade == "d" || it.nutriScoreGrade == "e"}.size


    return AnalyticsData(
        userName = userName,
        scannedItems = this.size,
        goodProducts = goodProducts,
        badProducts = badProducts,
        topCategories = topCategories.map { it.key.trimCategoryString() to it.value }.toMap(),
        averageNutrientPerProduct = averageByNutrientType
    )
}



fun String.trimCategoryString(): String{
    return this.replace("en:", "").replace("-", " ")
}