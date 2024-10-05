package com.zero1labs.nutriscan.data.remote

import retrofit2.http.GET
import com.zero1labs.nutriscan.data.remote.dto.SearchResponseDto
import retrofit2.http.Headers
import retrofit2.http.Query

interface OpenFoodFactsApi {

    @Headers("Accept: application/json")
    @GET("search/")
    suspend fun getProductDetails(@Query("code") productId : String ,@Query("fields") fields : String) : SearchResponseDto

}