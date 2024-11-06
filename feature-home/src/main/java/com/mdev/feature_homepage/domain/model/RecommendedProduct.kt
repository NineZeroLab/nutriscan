package com.mdev.feature_homepage.domain.model

import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto
import com.mdev.openfoodfacts_client.domain.model.HealthCategory

data class RecommendedProduct(
    val productId: String,
    val productName: String,
    val brandName: String,
    val imageUrl: String,
    val healthCategory: HealthCategory
)

fun RecommendedProductDto.toRecommendedProduct(): RecommendedProduct{
    return RecommendedProduct(
        productId = this.code ?: "",
        productName = this.productName ?: "",
        brandName = "Product Brand",
        imageUrl = this.imageUrl ?: "",
        healthCategory = getHealthCategory(this.nutriscoreGrade ?: "")
    )
}

fun getDummyRecommendedProducts (): List<RecommendedProduct> {
    val recommendedProduct = mutableListOf<RecommendedProduct>()
    for (i in 1..10){
        recommendedProduct.add(
            RecommendedProduct(
                productId = "12321312",
                productName = "Test",
                brandName = "Brand Test",
                imageUrl = "https://images.openfoodfacts.org/images/products/301/762/042/2003/front_en.633.400.jpg",
                healthCategory = HealthCategory.BAD
            )
        )
    }
    return recommendedProduct
}