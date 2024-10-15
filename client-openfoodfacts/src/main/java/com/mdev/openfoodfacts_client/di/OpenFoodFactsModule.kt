package com.mdev.openfoodfacts_client.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.mdev.openfoodfacts_client.data.remote.OpenFoodFactsApi
import com.mdev.openfoodfacts_client.data.repository.ProductRepositoryImpl
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import com.mdev.openfoodfacts_client.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OpenFoodFactsModule {

    @Provides
    @Singleton
    fun providesApiClient(): OpenFoodFactsApi{
        val gson = GsonBuilder().setLenient().create()
        val connectTimeout : Long = 10
        val readTimeout : Long = 10
        val writeTimeout : Long = 10

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OpenFoodFactsApi::class.java)
    }


    @Provides
    @Singleton
    fun providesProductRepository(
        api: OpenFoodFactsApi,
        @ApplicationContext context: Context
        ): ProductRepository{
        return ProductRepositoryImpl(
            openFoodFactsApi = api,
            context = context
        )
    }
}