package com.zero1labs.nutriscan.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mdev.client_firebase.FirebaseClient
import com.mdev.client_firebase.data.repository.FirebaseRepositoryImpl
import com.mdev.openfoodfacts_client.OpenFoodFactsClient
import com.mdev.openfoodfacts_client.data.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun providesAppRepository(): ProductRepositoryImpl {
        val client = OpenFoodFactsClient()
        return client()
    }


    @Provides
    @Singleton
    fun providesFirebase(): Firebase{
        return Firebase
    }

    @Provides
    @Singleton
    fun providesFirebaseClient(): FirebaseRepositoryImpl{
        val client = FirebaseClient()
        return client()
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