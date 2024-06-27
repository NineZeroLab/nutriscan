package com.zero1labs.nutriscan.data.models

import com.zero1labs.nutriscan.utils.NutriScoreCalculator

class MainDetailsForView(
    val imageUrl: String,
    val productName: String,
    val productBrand: String,
    val healthCategory: HealthCategory
){
    companion object{
        fun getMainDetailsForView(product : Product) : MainDetailsForView {

            val nutriScoreGrade = if (product.nutriScoreGrade == "unknown") NutriScoreCalculator.getNutriScoreGrade(
                product.nutrients
            ) else product.nutriScoreGrade
            val healthCategory = getHealthCategory(nutriScoreGrade)
            return MainDetailsForView(
                imageUrl = product.imageUrl,
                productName = product.productName,
                productBrand = product.brand,
                healthCategory = healthCategory,
            )
        }
        private fun getHealthCategory(nutriScoreGrade: String) : HealthCategory {
            return when(nutriScoreGrade){
                "a" -> HealthCategory.HEALTHY
                "b" -> HealthCategory.GOOD
                "c" -> HealthCategory.FAIR
                "d" -> HealthCategory.POOR
                "e" -> HealthCategory.BAD
                else -> HealthCategory.UNKNOWN
            }
        }
    }
}