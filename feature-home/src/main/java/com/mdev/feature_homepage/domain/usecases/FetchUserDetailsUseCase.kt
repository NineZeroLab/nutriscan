package com.mdev.feature_homepage.domain.usecases

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.Resource
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FetchUserDetailsUseCase @Inject constructor(
    private val homePageRepository: HomePageRepository
) {
    internal operator fun invoke(): Flow<Resource<AppUser>> = flow{
        emit(Resource.Loading())
        try {
            val appUser = homePageRepository.getUserDetails()
            emit(Resource.Success(appUser))
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}