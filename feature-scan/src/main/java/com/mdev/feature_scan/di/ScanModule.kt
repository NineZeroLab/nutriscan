package com.mdev.feature_scan.di

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_scan.data.repository.ScanRepositoryImpl
import com.mdev.feature_scan.domain.repository.ScanRepository
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ScanModule {
    @Provides
    @Singleton
    fun providesScanRepository(
        firebaseRepository: FirebaseRepository,
        productRepository: ProductRepository
    ): ScanRepository{
        return ScanRepositoryImpl(productRepository, firebaseRepository)
    }
}