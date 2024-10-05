package com.zero1labs.nutriscan.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import com.google.gson.GsonBuilder
import com.zero1labs.nutriscan.data.remote.OpenFoodFactsApi
import com.zero1labs.nutriscan.data.repository.ProductRepositoryImpl
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
    fun providesAppApi() : OpenFoodFactsApi {
        val gson = GsonBuilder().setLenient().create()
        val connectTimeout : Long = 10
        val readTimeout : Long = 10
        val writeTimeout : Long = 10

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(connectTimeout,TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .build()

        val api : OpenFoodFactsApi = Retrofit.Builder()
             .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OpenFoodFactsApi::class.java)
        return api
    }

    @Provides
    @Singleton
    fun providesAppRepository(openFoodFactsApi: OpenFoodFactsApi): ProductRepositoryImpl {

        return ProductRepositoryImpl(openFoodFactsApi)
    }


    @Provides
    @Singleton
    fun providesFirebase(): Firebase{
        return Firebase
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth(firebase: Firebase): FirebaseAuth{
        val auth = FirebaseAuth.getInstance()
//        auth.useEmulator("10.0.2.2",9099)
        return auth
    }

    @Provides
    @Singleton
    fun providesFirebaseFirestore(firebase: Firebase): FirebaseFirestore{
        val firestore = FirebaseFirestore.getInstance()
//        firestore.useEmulator("10.0.2.2", 8080)
        return firestore
    }
}