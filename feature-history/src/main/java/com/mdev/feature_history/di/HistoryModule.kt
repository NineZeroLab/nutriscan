package com.mdev.feature_history.di

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_history.data.repository.HistoryRepositoryImpl
import com.mdev.feature_history.domain.repository.HistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {

    @Provides
    @Singleton
    internal fun providesHistoryRepository(firebaseRepository: FirebaseRepository): HistoryRepository{
        return HistoryRepositoryImpl(firebaseRepository)
    }
}