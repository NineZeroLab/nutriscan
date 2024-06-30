package com.zero1labs.nutriscan.di

import dagger.Module
import dagger.Provides
import com.google.gson.GsonBuilder
import com.zero1labs.nutriscan.repository.ApiRequests
import com.zero1labs.nutriscan.repository.AppRepository
import com.zero1labs.nutriscan.utils.AppResources.BASE_URL
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun providesAppApi() : Retrofit{
        val gson = GsonBuilder().setLenient().create()
        val connectTimeout : Long = 30
        val readTimeout : Long = 30
        val writeTimeout : Long = 30

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(connectTimeout,TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .build()

        val retrofit : Retrofit = Retrofit.Builder()
             .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit
    }

    @Provides
    @Singleton
    fun providesAppRepository(retrofit: Retrofit): AppRepository{
        return AppRepository(retrofit)
    }
}