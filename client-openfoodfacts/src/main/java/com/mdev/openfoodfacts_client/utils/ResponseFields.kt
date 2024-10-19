package com.mdev.openfoodfacts_client.utils

object ResponseFields {
    private val productDetailsFields = mutableListOf<String>(
        "code",
        "product_name",
        "image_url",
        "nutriments",
        "ingredients",
        "nutriscore_data",
        "brands",
        "nutriscore_grade",
        "categories_hierarchy",
        "allergens_hierarchy",
        "ingredients_analysis_tags",
        "additives_original_tags"
    )
    private val recommendProductFields = mutableListOf(
        "code",
        "product_name",
        "image_url",
        "nutriscore_grade",
    )

    fun getProductDetailsFields(): String{
        return productDetailsFields.joinToString(",")
    }

    fun getRecommendedProductFields(): String{
        return recommendProductFields.joinToString(",")
    }

}