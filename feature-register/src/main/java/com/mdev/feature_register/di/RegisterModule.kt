package com.mdev.feature_register.di

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_register.data.repository.RegisterRepositoryImpl
import com.mdev.feature_register.domain.repository.RegisterRepository
import com.mdev.feature_register.domain.usecases.RegisterWithEmailAndPasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RegisterModule {

    @Provides
    @Singleton
    fun providesRegisterRepository(firebaseRepository: FirebaseRepository): RegisterRepository {
       return RegisterRepositoryImpl(firebaseRepository)
    }

    @Provides
    @Singleton
    fun providesRegisterWithEmailAndPasswordUseCase(registerRepository: RegisterRepository): RegisterWithEmailAndPasswordUseCase{
        return RegisterWithEmailAndPasswordUseCase(registerRepository)
    }

}