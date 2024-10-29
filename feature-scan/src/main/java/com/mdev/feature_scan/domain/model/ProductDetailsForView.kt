package com.mdev.feature_scan.domain.model

import com.mdev.feature_scan.data.model.ProductDetailsForViewDto
import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import com.mdev.openfoodfacts_client.domain.model.ProductDetails

internal data class ProductDetailsForView(
    val name: String,
    val brand: String?,
    val healthCategory: HealthCategory,
    val imageUrl: String
)




internal fun ProductDetailsForViewDto.toProductDetailsForView(): ProductDetailsForView{
    return ProductDetailsForView(
        name = this.name,
        brand = this.brand,
        healthCategory = this.healthCategory,
        imageUrl = this.imageUrl ?: "",
    )
}

internal fun ProductDetails.toProductDetailsForView(): ProductDetailsForView{
    return ProductDetailsForView(
        name = this.name,
        brand = this.brand,
        healthCategory = getHealthCategory(this.nutriScoreGrade),
        imageUrl = this.imageUrl
    )
}

private fun getHealthCategory(nutriScoreGrade: String?) : HealthCategory {
    return when(nutriScoreGrade){
        "a" ,
        "b" -> HealthCategory.GOOD
        "c" -> HealthCategory.FAIR
        "d" ,
        "e" -> HealthCategory.BAD
        else -> HealthCategory.UNKNOWN
    }
}