package com.mdev.feature_login.di

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_login.data.repository.LoginRepositoryImpl
import com.mdev.feature_login.domain.repository.LoginRepository
import com.mdev.feature_login.domain.usecase.LoginWithEmailAndPasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {
    @Provides
    @Singleton
    fun provideLoginRepository(firebaseRepository: FirebaseRepository): LoginRepository{
        return LoginRepositoryImpl(firebaseRepository)
    }

    @Provides
    @Singleton
    fun provideLoginWithEmailAndPasswordUseCase(loginRepository: LoginRepository): LoginWithEmailAndPasswordUseCase{
        return LoginWithEmailAndPasswordUseCase(loginRepository)
    }
}