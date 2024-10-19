package com.mdev.openfoodfacts_client.data.remote.dto


import com.google.gson.annotations.SerializedName

data class RecommendedProductsSearchResponseDto(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("page_count")
    val pageCount: Int? = null,
    @SerializedName("page_size")
    val pageSize: Int? = null,
    @SerializedName("products")
    val recommendedProductDtos: List<RecommendedProductDto>? = null,
    @SerializedName("skip")
    val skip: Int? = null
)