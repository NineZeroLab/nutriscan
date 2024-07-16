package com.zero1labs.nutriscan.data.models

import android.util.Log
import com.zero1labs.nutriscan.data.models.remote.Product
import com.zero1labs.nutriscan.utils.HealthCategory
import com.zero1labs.nutriscan.utils.NutriScoreCalculator

class MainDetailsForView(
    val productId: String? = "",
    val imageUrl: String? = "",
    val productName: String = "",
    val productBrand: String = "",
    val healthCategory: HealthCategory = HealthCategory.UNKNOWN
){
    companion object{
        fun getMainDetailsForView(product : Product) : MainDetailsForView {

            val nutriScoreGrade = if (product.nutriScoreGrade == "unknown") NutriScoreCalculator.getNutriScoreGrade(
                product.nutrients
            ) else product.nutriScoreGrade
            val healthCategory = getHealthCategory(nutriScoreGrade)
            return MainDetailsForView(
                productId = product.productId,
                imageUrl = product.imageUrl ?: getProductImageUrl(productId = product.productId),
                productName = product.productName,
                productBrand = product.brand ?: "",
                healthCategory = healthCategory,
            )
        }
        private fun getHealthCategory(nutriScoreGrade: String?) : HealthCategory {
            return when(nutriScoreGrade){
                "a" -> HealthCategory.HEALTHY
                "b" -> HealthCategory.GOOD
                "c" -> HealthCategory.FAIR
                "d" -> HealthCategory.POOR
                "e" -> HealthCategory.BAD
                else -> HealthCategory.UNKNOWN
            }
        }
        private fun getProductImageUrl(productId: String) : String? {
            val imageBaseUrl = "https://images.openfoodfacts.net/images/products/"
            if (productId.length < 13) productId.padStart(length = 13 - productId.length , padChar = '0')
            val imageUrl = buildString {
                append(imageBaseUrl)
                append(productId.subSequence(0,3)).append('/')
                append(productId.subSequence(3,6)).append('/')
                append(productId.subSequence(6,9)).append('/')
                append(productId.subSequence(9,13)).append("/1.jpg")
            }
            Log.d("logger", imageUrl)
            return imageUrl
        }
    }
}