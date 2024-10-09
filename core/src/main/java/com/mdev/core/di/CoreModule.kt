package com.mdev.core.di

import com.mdev.openfoodfacts_client.data.repository.ProductRepositoryImpl
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

}