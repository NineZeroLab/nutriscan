package com.mdev.feature_homepage.domain.usecases

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.Resource
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val homePageRepository: HomePageRepository
) {
    operator fun invoke(): Flow<Resource<AppUser>> = flow {
        emit(Resource.Loading())
        try {
            val user = homePageRepository.getUserDetails()
            if (user == null){
                emit(Resource.Error("Unable to fetch user details"))
            }else{
                emit(Resource.Success(user))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }

    }
}