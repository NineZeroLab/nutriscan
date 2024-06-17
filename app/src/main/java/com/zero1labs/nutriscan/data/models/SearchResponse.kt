package com.zero1labs.nutriscan.data.models

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("count")
    val itemCount: Int,

    @SerializedName("page")
    val currentPage : Int,

    @SerializedName("page_count")
    val pageCount : Int,

    @SerializedName("products")
    val products : List<Product>
)
