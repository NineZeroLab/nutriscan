package com.mdev.openfoodfacts_client

import com.google.gson.GsonBuilder
import com.mdev.core.utils.AppResources.BASE_URL
import com.mdev.openfoodfacts_client.data.remote.OpenFoodFactsApi
import com.mdev.openfoodfacts_client.data.repository.ProductRepositoryImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class OpenFoodFactsClient {
    operator fun invoke(): ProductRepositoryImpl {
        val gson = GsonBuilder().setLenient().create()
        val connectTimeout : Long = 10
        val readTimeout : Long = 10
        val writeTimeout : Long = 10

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .build()

        val api : OpenFoodFactsApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OpenFoodFactsApi::class.java)
        return ProductRepositoryImpl(api)
    }
}
