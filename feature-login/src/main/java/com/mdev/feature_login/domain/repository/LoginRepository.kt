package com.mdev.feature_login.domain.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.Resource

interface LoginRepository {
    suspend fun loginUserWithEmailAndPassword(email: String, password: String): Resource<AppUser>
    suspend fun isUserLoggedIn(): Boolean
}