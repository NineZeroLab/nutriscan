package com.mdev.feature_login.domain.usecase

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(): Flow<Resource<Boolean>> = flow{
        emit(Resource.Loading())
        try {
            val isLoggedIn = firebaseRepository.isUserLoggedIn()
            emit(Resource.Success(isLoggedIn))
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}