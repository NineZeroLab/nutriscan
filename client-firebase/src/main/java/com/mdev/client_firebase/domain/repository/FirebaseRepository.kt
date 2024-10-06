package com.mdev.client_firebase.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    suspend fun getUserDetailsById(): Flow<Resource<AppUser>>
    suspend fun registerUserByEmailAndPassword(email: String, password: String): Flow<Resource<FirebaseUser>>
    suspend fun signInUserByEmailAndPassword(email: String, password: String): Flow<Resource<FirebaseUser>>
    suspend fun createUserDetails(appUser: AppUser): Flow<Resource<AppUser>>
    suspend fun signOutUser(): Flow<Resource<AppUser>>
}