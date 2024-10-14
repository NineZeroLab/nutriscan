package com.mdev.feature_login.domain.usecase

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import com.mdev.core.utils.logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(): Flow<Resource<AppUser>> = flow{
        emit(Resource.Loading())
        try {
            val appUser = firebaseRepository.getCurrentUserDetails()
            logger("Current user: ${appUser?.name}")
            emit(Resource.Success(appUser))
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}