package com.mdev.openfoodfacts_client.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("code")
    var productId : String,

    @SerializedName("product_name")
    val productName : String,

    @SerializedName("image_url")
    val imageUrl : String?,

    @SerializedName("nutriments")
    val nutrients: NutrientsDto?,

    @SerializedName("ingredients")
    val ingredients: List<IngredientsDto>?,

    @SerializedName("brands")
    val brand : String?,

    @SerializedName("nutriscore_grade")
    val nutriScoreGrade : String?,

    @SerializedName("nutriscore_data")
    val nutriScoreData: NutriScoreDataDto?,

    @SerializedName("categories_hierarchy")
    val categoriesHierarchy: List<String>?,

    @SerializedName("allergens_hierarchy")
    val allergensHierarchy: List<String>?,

    @SerializedName("ingredients_analysis_tags")
    val ingredientsAnalysisTags: List<String>?,
)