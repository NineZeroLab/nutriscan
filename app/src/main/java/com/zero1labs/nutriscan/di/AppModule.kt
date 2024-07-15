package com.zero1labs.nutriscan.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.LocalCacheSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.persistentCacheSettings
import dagger.Module
import dagger.Provides
import com.google.gson.GsonBuilder
import com.zero1labs.nutriscan.repository.AppRepository
import com.zero1labs.nutriscan.utils.AppResources.BASE_URL
import com.zero1labs.nutriscan.utils.NetworkUtils
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
object AppModule{

    @Provides
    @Singleton
    fun providesAppApi() : Retrofit{
        val gson = GsonBuilder().setLenient().create()
        val connectTimeout : Long = 5
        val readTimeout : Long = 5
        val writeTimeout : Long = 5

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

    @Provides
    @Singleton
    fun providesNetworkUtils(@ApplicationContext context: Context): NetworkUtils{
        return NetworkUtils(context)
    }
    @Provides
    @Singleton
    fun providesFirebase(): Firebase{
        return Firebase
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth(firebase: Firebase): FirebaseAuth{
        val auth = firebase.auth
        auth.useEmulator("10.0.2.2",9099)
        return auth
    }

    @Provides
    @Singleton
    fun providesFirebaseFirestore(firebase: Firebase): FirebaseFirestore{
        val firestore = Firebase.firestore
        firestore.useEmulator("10.0.2.2", 8080)
        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
        return firestore
    }
}