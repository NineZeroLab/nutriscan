package com.mdev.feature_login.data.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import com.mdev.core.utils.logger
import com.mdev.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): LoginRepository  {
    override suspend fun loginUserWithEmailAndPassword(email: String, password: String): Resource<AppUser> {
        return withContext(Dispatchers.IO){
            try {
                val authResult = firebaseRepository.loginUserByEmailAndPassword(email, password)
                logger(authResult.user?.uid.toString())
                if (authResult.user == null){
                    Resource.Error("Unknown Error: Unable to fetch User Details")
                }else{
                    val appUser = firebaseRepository.getCurrentUserDetails()
                    Resource.Success(appUser)
                }
            }catch (e: Exception) {
                Resource.Error(e.message.toString())
            }
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return firebaseRepository.isUserLoggedIn()
    }

}