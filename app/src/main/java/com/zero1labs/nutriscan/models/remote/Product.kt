package com.zero1labs.nutriscan.data.models.remote

import com.google.gson.annotations.SerializedName
import com.zero1labs.nutriscan.data.models.remote.Ingredients
import com.zero1labs.nutriscan.data.models.remote.NutriScoreData
import com.zero1labs.nutriscan.data.models.remote.Nutrients

data class Product(
    @SerializedName("code")
    var productId : String,

    @SerializedName("product_name")
    val productName : String,

    @SerializedName("image_url")
    val imageUrl : String?,

    @SerializedName("nutriments")
    val nutrients: Nutrients?,

    @SerializedName("ingredients")
    val ingredients: List<Ingredients>?,

    @SerializedName("brands")
    val brand : String?,

    @SerializedName("nutriscore_grade")
    val nutriScoreGrade : String?,

    @SerializedName("nutriscore_data")
    val nutriScoreData: NutriScoreData?,

    @SerializedName("categories_hierarchy")
    val categoriesHierarchy: List<String>?,

    @SerializedName("allergens_hierarchy")
    val allergensHierarchy: List<String>?,

    @SerializedName("ingredients_analysis_tags")
    val ingredientsAnalysisTags: List<String>?,
)