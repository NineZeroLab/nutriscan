package com.mdev.feature_product_details.domain.model

import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto

internal data class RecommendedProduct(
    val name: String?,
    val imageUrl: String?,
    val nutriScoreGrade: String?
)


internal fun RecommendedProductDto.toRecommendedProduct(): RecommendedProduct{
    return RecommendedProduct(
        name = this.productName,
        imageUrl = this.imageUrl,
        nutriScoreGrade = this.nutriscoreGrade
    )
}

internal fun List<RecommendedProductDto>.toRecommendedProducts(): List<RecommendedProduct>{
    return this.map {
        it.toRecommendedProduct()
    }
}