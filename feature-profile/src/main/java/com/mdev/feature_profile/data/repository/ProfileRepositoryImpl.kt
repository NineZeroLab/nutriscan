package com.mdev.feature_profile.data.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import com.mdev.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ProfileRepository{
    override suspend fun getProfileDetails(): Resource<AppUser> {
        val appUser = firebaseRepository.getCurrentUserDetails() ?: return Resource.Error("Unknown Error: user not found")
        return Resource.Success(appUser)
    }

    override suspend fun updateUserDetails(appUser: AppUser): Resource<Unit> {
        return firebaseRepository.updateUserDetails(appUser)
    }

    override suspend fun logout() {
    }
}