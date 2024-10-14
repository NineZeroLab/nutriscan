package com.mdev.feature_profile.di

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_profile.data.repository.ProfileRepositoryImpl
import com.mdev.feature_profile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    @Singleton
    fun providesProfileRepository(firebaseRepository: FirebaseRepository): ProfileRepository{
        return ProfileRepositoryImpl(firebaseRepository)
    }
}