package com.mdev.feature_homepage.di

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_homepage.data.repository.HomePageRepositoryImpl
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun providesHomeRepository(
        firebaseRepository: FirebaseRepository,
        productRepository: ProductRepository
    ): HomePageRepository{
        return HomePageRepositoryImpl(firebaseRepository, productRepository)
    }
}