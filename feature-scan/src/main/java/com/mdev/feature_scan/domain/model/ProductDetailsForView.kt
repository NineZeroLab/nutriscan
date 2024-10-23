package com.mdev.feature_scan.domain.model

import com.mdev.feature_scan.data.model.ProductDetailsForViewDto
import com.mdev.openfoodfacts_client.domain.model.HealthCategory

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