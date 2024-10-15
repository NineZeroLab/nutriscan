package com.mdev.feature_login.data.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import com.mdev.feature_login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): LoginRepository  {
    override suspend fun loginUserWithEmailAndPassword(email: String, password: String): Resource<AppUser> {
        try {
            val authResult = firebaseRepository.loginUserByEmailAndPassword(email, password)
            authResult.user?.uid?.let {
                val appUser = firebaseRepository.getCurrentUserDetails()
                appUser?.let { return Resource.Success(it) }
            }
            return Resource.Error("Unknown Error: Unable to fetch User Details")
        }catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return firebaseRepository.isUserLoggedIn()
    }

}