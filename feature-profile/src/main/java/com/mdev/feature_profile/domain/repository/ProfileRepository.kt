package com.mdev.feature_profile.domain.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.Resource

interface ProfileRepository {
    suspend fun getProfileDetails(): Resource<AppUser>
    suspend fun updateUserDetails(appUser: AppUser): Resource<Unit>
}