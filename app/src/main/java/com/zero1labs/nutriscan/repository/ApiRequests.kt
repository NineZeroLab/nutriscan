package com.zero1labs.nutriscan.repository

import retrofit2.http.GET
import retrofit2.Call
import com.zero1labs.nutriscan.data.models.SearchResponse
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiRequests {

    @Headers("Accept: application/json")
    @GET("search/")
    suspend fun getProductDetails(@Query("code") productId : String ,@Query("fields") fields : String) : Response<SearchResponse>

}