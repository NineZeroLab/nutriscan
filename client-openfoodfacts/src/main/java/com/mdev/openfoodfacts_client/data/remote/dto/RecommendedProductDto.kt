package com.mdev.openfoodfacts_client.data.remote.dto


import com.google.gson.annotations.SerializedName

data class RecommendedProductDto(
    @SerializedName("allergens_tags")
    val allergensTags: List<String?>? = null,
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("ingredients_analysis_tags")
    val ingredientsAnalysisTags: List<String?>? = null,
    @SerializedName("nutriscore_grade")
    val nutriscoreGrade: String? = null,
    @SerializedName("product_name")
    val productName: String? = null,
    @SerializedName("brands")
    val brand: String? = null,
)