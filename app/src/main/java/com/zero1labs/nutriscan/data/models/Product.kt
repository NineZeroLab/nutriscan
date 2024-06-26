package com.zero1labs.nutriscan.data.models

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("code")
    var productId : String,

    @SerializedName("product_name")
    val productName : String,

    @SerializedName("image_url")
    val imageUrl : String,

    @SerializedName("nutriments")
    val nutrients: Nutrients,

    @SerializedName("ingredients")
    val ingredients: List<Ingredients>,

    @SerializedName("brands")
    val brand : String,

    @SerializedName("nutriscore_grade")
    val nutriScoreGrade : String,

    @SerializedName("nutriscore_data")
    val nutriScoreData: NutriScoreData? = null,
)