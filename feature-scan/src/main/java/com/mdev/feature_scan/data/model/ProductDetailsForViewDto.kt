package com.mdev.feature_scan.data.model

import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.domain.model.HealthCategory

data class ProductDetailsForViewDto(
    val id: String,
    val name: String,
    val brand: String?,
    val nutriScoreGrade: String?,
    val imageUrl: String?,
    val healthCategory: HealthCategory,
)




internal fun ProductDto.toProductDetailsForViewDto(): ProductDetailsForViewDto{
    return ProductDetailsForViewDto(
        id = this.productId,
        name = this.productName,
        brand = this.brand,
        imageUrl = this.imageUrl,
        nutriScoreGrade = this.nutriScoreGrade,
        //TODO: change default value
        healthCategory = HealthCategory.UNKNOWN
    )
}