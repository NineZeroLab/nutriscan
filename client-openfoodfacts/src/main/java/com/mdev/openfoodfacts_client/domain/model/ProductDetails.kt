package com.mdev.openfoodfacts_client.domain.model

import android.util.Log
import com.google.firebase.Timestamp
import com.mdev.openfoodfacts_client.data.remote.dto.AdditiveDto
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import java.time.LocalDateTime
import java.time.LocalTime

data class ProductDetails(
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
    val timestamp: Timestamp = Timestamp.now()
)


fun ProductDto.toProductDetails(additives: List<AdditiveDto> = emptyList()): ProductDetails{
    val nutrients = this.nutrients?.toNutrientDetailsList() ?: emptyList()
    val ingredientsAnalysisTags = getDietaryRestrictions(this.ingredientsAnalysisTags)

    return ProductDetails(
        id = this.productId,
        name = this.productName,
        imageUrl = this.imageUrl ?: getProductImageUrl(this.productId),
        nutrients = nutrients,
        brand = this.brand ?: "",
        nutriScoreGrade = this.nutriScoreGrade ?: "",
        categoriesHierarchy = this.categoriesHierarchy ?: emptyList(),
        ingredientsAnalysis = ingredientsAnalysisTags,
        additivesOriginalTags = additives
    )
}

private fun getProductImageUrl(productId: String) : String {
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