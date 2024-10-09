package com.mdev.feature_profile.domain.usecases

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.Resource
import com.mdev.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProfileDetailsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<Resource<AppUser>> = flow{
        emit(Resource.Loading())
        try {
            when(val result = profileRepository.getProfileDetails()){
                is Resource.Error -> {
                    emit(Resource.Error(result.message.toString()))
                }
                is Resource.Loading -> {
                    emit(Resource.Loading())
                }
                is Resource.Success -> {
                    emit(Resource.Success(result.data))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}