package com.zero1labs.nutriscan.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchResponseDto(
    @SerializedName("count")
    val itemCount: Int,

    @SerializedName("page")
    val currentPage : Int,

    @SerializedName("page_count")
    val pageCount : Int,

    @SerializedName("products")
    val products : List<ProductDto>
)
