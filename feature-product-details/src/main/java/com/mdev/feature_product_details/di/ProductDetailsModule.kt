package com.mdev.feature_product_details.di

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_product_details.data.repository.ProductDetailsRepositoryImpl
import com.mdev.feature_product_details.domain.repository.ProductDetailsRepository
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductDetailsModule {
    @Provides
    @Singleton
    internal fun providesProductDetailsRepository(
        firebaseRepository: FirebaseRepository,
        productRepository: ProductRepository
    ): ProductDetailsRepository{
        return ProductDetailsRepositoryImpl(
            productRepository = productRepository,
            firebaseRepository = firebaseRepository
        )
    }
}