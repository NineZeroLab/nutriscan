package com.mdev.feature_profile.domain.usecases

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.Resource
import com.mdev.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateProfileDetailsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(appUser: AppUser): Flow<Resource<AppUser>> = flow {
        emit(Resource.Loading())
        try {
            when(val result = profileRepository.updateUserDetails(appUser)){
                is Resource.Error -> {
                    emit(Resource.Error(result.message.toString()))
                }
                is Resource.Loading -> {
                    emit(Resource.Loading())
                }
                is Resource.Success -> {
                    emit(Resource.Success(appUser))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}